package com.ice.harmonia.service.impl;

import com.ice.harmonia.entity.Artist;
import com.ice.harmonia.entity.ArtistAlias;
import com.ice.harmonia.entity.Track;
import com.ice.harmonia.entity.TrackArtistId;
import com.ice.harmonia.entity.TrackArtistManifest;
import com.ice.harmonia.mapper.TrackDtoMapper;
import com.ice.harmonia.mapper.TrackResponseMapper;
import com.ice.harmonia.record.AliasModificationRequest;
import com.ice.harmonia.record.TrackArtistInfo;
import com.ice.harmonia.record.TrackCreationRequest;
import com.ice.harmonia.record.response.TrackDto;
import com.ice.harmonia.record.response.TrackResponse;
import com.ice.harmonia.repository.ArtistAliasRepository;
import com.ice.harmonia.repository.ArtistRepository;
import com.ice.harmonia.repository.TrackArtistManifestRepository;
import com.ice.harmonia.repository.TrackRepository;
import com.ice.harmonia.service.MusicCatalogTrackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

import static com.ice.harmonia.util.MusicCatalogTrackServiceHelper.validateTrackArtistInfos;
import static java.lang.String.format;

@Service
public class MusicCatalogTrackServiceImpl implements MusicCatalogTrackService {

    private final Logger logger = LoggerFactory.getLogger(MusicCatalogTrackServiceImpl.class);

    private final TrackRepository trackRepository;

    private final ArtistRepository artistRepository;

    private final ArtistAliasRepository artistAliasRepository;

    private final TrackArtistManifestRepository trackArtistManifestRepository;

    private final TrackResponseMapper trackResponseMapper;

    private final TrackDtoMapper trackDtoMapper;

    public MusicCatalogTrackServiceImpl(TrackRepository trackRepository,
                                   ArtistRepository artistRepository,
                                   ArtistAliasRepository artistAliasRepository, TrackArtistManifestRepository trackArtistManifestRepository,
                                   TrackResponseMapper trackResponseMapper, TrackDtoMapper trackDtoMapper) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.artistAliasRepository = artistAliasRepository;
        this.trackArtistManifestRepository = trackArtistManifestRepository;
        this.trackResponseMapper = trackResponseMapper;
        this.trackDtoMapper = trackDtoMapper;
    }


    @Override
    @Transactional
    public TrackResponse createTrack(@NotNull TrackCreationRequest trackCreationRequest) {

        validateTrackArtistInfos(trackCreationRequest.trackArtistInfos());

        TrackArtistInfo trackArtistInfo = trackCreationRequest
                .trackArtistInfos()
                .getFirst();

        Artist artist = getArtist(trackArtistInfo);

        ArtistAlias artistAlias = getArtistAlias(artist, trackArtistInfo.artistAliasName());

        trackRepository.findTrackByTitleAndGenre(trackCreationRequest.title(), trackCreationRequest.genre())
                .ifPresent(existingTrack -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            format("Conflict! Title {%s} and Genre {%s} already exists in the database",
                                    trackCreationRequest.title(), trackCreationRequest.genre()));
                });

        Track track = createNewTrack(trackCreationRequest);

        logger.info("Track created: {}", track.getId());

        TrackArtistManifest trackArtistManifest = createNewTrackArtistManifest(track, artist, artistAlias);

        logger.info("TrackArtistManifest created: {}", trackArtistManifest.getId());

        return trackResponseMapper.mapEntityToResponse(track,
                artist,
                artistAlias);
    }

    @Override
    @Transactional
    public TrackResponse updateAliasName(
            @NotNull Long trackId,
            @NotNull Long artistId,
            @NotNull Long aliasId,
            @Valid AliasModificationRequest aliasModificationRequest) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        format("Not found! Track {%s} not found", trackId.toString())));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        format("Not found! Artist {%s} not found", artistId.toString())));

        ArtistAlias artistAlias = artistAliasRepository.findById(aliasId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        format("Not found! Artist alias {%s} not found", aliasId.toString())));

        TrackArtistManifest existingArtistAliasManifest = trackArtistManifestRepository
                .findByTrackAndArtistAndArtistAlias(track, artist, artistAlias)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Not found! Track Artist relation not found"));

        if (!artistAlias.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    format("Conflict! Artist alias {%s} is not active",
                            artistAlias.getAliasName()));
        }

        if (artistAlias.getAliasName().equals(aliasModificationRequest.aliasName())) {
            logger.error("Alias name {} is same as the existing one. No need to update", aliasModificationRequest.aliasName());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    format("Conflict! Alias name {%s} already exists in the database",
                            aliasModificationRequest.aliasName()));
        }

        artistAlias.setAliasName(aliasModificationRequest.aliasName());

        ArtistAlias createdAlias = artistAliasRepository.save(artistAlias);
        logger.info("Alias name updated: {}", createdAlias.getId());

        existingArtistAliasManifest.setAliasNameSnapshot(aliasModificationRequest.aliasName());
        logger.info("TrackArtistManifest updated: Track {}, Artist {}, Alias {}",
                existingArtistAliasManifest.getTrack().getId(),
                existingArtistAliasManifest.getArtist().getId(),
                aliasId);

        return trackResponseMapper.mapEntityToResponse(track,
                artist,
                createdAlias);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrackDto> getTracksByArtistId(Long artistId, Pageable pageable) {
        return fetchTracksByArtist(artistId,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrackDto> getTracksByArtistAliasId(Long aliasId, Pageable pageable) {
        return fetchTracksByAliasId(aliasId, pageable);
    }

    private Page<TrackDto> fetchTracksByAliasId(Long aliasId, Pageable pageable) {

        ArtistAlias artistAlias = artistAliasRepository
                .findById(aliasId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        format("NotFound! Artist alias {%s} not found", aliasId)));

        if (!artistAlias.getIsActive()) {
            logger.error("Artist alias {} name {} is not active",
                    artistAlias.getId(),
                    artistAlias.getAliasName());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    format("Conflict! Artist alias {%s} is not active", artistAlias.getAliasName()));
        }

        Page<TrackArtistManifest> trackArtistManifest = trackArtistManifestRepository
                .findTrackArtistManifestsByArtistAlias(artistAlias, pageable);

        return trackDtoMapper.pageToTrackDtoPage(trackArtistManifest);
    }

    private Page<TrackDto> fetchTracksByArtist(Long artistId, Pageable pageable) {
        Artist artist = artistRepository
                .findById(artistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        format("NotFound! Artist {%s} not found", artistId.toString())));

        Page<TrackArtistManifest> trackArtistManifest = trackArtistManifestRepository
                .findTrackArtistManifestsByArtist(artist, pageable);

        return trackDtoMapper.pageToTrackDtoPage(trackArtistManifest);
    }

    private Artist createNewArtist(TrackArtistInfo trackArtistInfo) {
        Artist artist = Artist.builder()
                .externalId(UUID.randomUUID())
                .externalId(trackArtistInfo.artistExternalId())
                .name(trackArtistInfo.artistLegalName())
                .createdBy("USER")
                .createdAt(Instant.now())
                .build();

        return artistRepository.save(artist);
    }

    private Artist getArtist(TrackArtistInfo trackArtistInfo) {
       return artistRepository.findArtistByExternalId(trackArtistInfo.artistExternalId())
                .map(existingArtist -> {
                    if (!existingArtist.getName().equals(trackArtistInfo.artistLegalName()))  {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                format("Conflict! Existing artist name {%s} does not match the provided name {%s}",
                                        existingArtist.getName(), trackArtistInfo.artistLegalName()));
                    }
                    return existingArtist;
                }).orElseGet(() -> createNewArtist(trackArtistInfo));
    }

    private ArtistAlias getArtistAlias(Artist artist, String artistAliasName) {

        return artistAliasRepository.
                findArtistAliasesByArtistAndAliasName(artist, artistAliasName)
                .map(existingAlias -> {
                    if (!existingAlias.getIsActive()) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                format("Conflict! Artist alias {%s} is not active",
                                        existingAlias.getAliasName()));
                    }
                    return existingAlias;
                }).orElseGet(() -> createNewArtistAlias(artist, artistAliasName));

    }

    private ArtistAlias createNewArtistAlias(Artist artist, String artistAliasName) {
        ArtistAlias newArtistAlias = ArtistAlias.builder()
                .externalId(UUID.randomUUID())
                .aliasName(artistAliasName)
                .artist(artist)
                .isActive(true)
                .isVisibleInRotation(true)
                .createdBy("USER")
                .createdAt(Instant.now())
                .build();

        return artistAliasRepository.save(newArtistAlias);
    }
    private TrackArtistManifest createNewTrackArtistManifest(Track track, Artist artist, ArtistAlias artistAlias) {

        TrackArtistId trackArtistId = TrackArtistId.builder()
                .trackId(track.getId())
                .artistId(artist.getId())
                .build();

        TrackArtistManifest trackArtistManifest = TrackArtistManifest.builder()
                .id(trackArtistId)
                .track(track)
                .artist(artist)
                .artistAlias(artistAlias)
                .aliasNameSnapshot(artistAlias.getAliasName())
                .build();

        return trackArtistManifestRepository.save(trackArtistManifest);
    }

    private Track createNewTrack(@NotNull TrackCreationRequest trackCreationRequest) {
        Track track = Track.builder()
                .externalId(UUID.randomUUID())
                .title(trackCreationRequest.title())
                .genre(trackCreationRequest.genre())
                .durationInSeconds(trackCreationRequest.durationInSeconds())
                .createdBy("USER")
                .createdAt(Instant.now())
                .build();

        return trackRepository.save(track);
    }
}
