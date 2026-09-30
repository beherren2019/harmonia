package com.ice.harmonia.controller;

import com.ice.harmonia.record.AliasModificationRequest;
import com.ice.harmonia.record.TrackCreationRequest;
import com.ice.harmonia.record.response.TrackDto;
import com.ice.harmonia.record.response.TrackResponse;
import com.ice.harmonia.service.MusicCatalogTrackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/music-catalog")
public class MusicCatalogTrackController implements MusicCatalogTrackApi {

    private final Logger logger = LoggerFactory.getLogger(MusicCatalogTrackController.class);

    private final MusicCatalogTrackService musicCatalogTrackService;

    public MusicCatalogTrackController(MusicCatalogTrackService musicCatalogTrackService) {
        this.musicCatalogTrackService = musicCatalogTrackService;
    }

    @Override
    @PostMapping(value = "/tracks",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrackResponse> addTrack(
            @RequestBody @Valid TrackCreationRequest trackCreationRequest) {

        TrackResponse response = musicCatalogTrackService
                .createTrack(trackCreationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        logger.info("Track created: {}", response.id());

        return ResponseEntity.created(location).body(response);
    }

    @Override
    @PatchMapping(
            value = "track/{trackId}/artist/{artistId}/alias/{aliasId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TrackResponse> changeArtistAliasName(
            @PathVariable @NotNull Long trackId,
            @PathVariable @NotNull Long artistId,
            @PathVariable @NotNull Long aliasId,
            @RequestBody @Valid AliasModificationRequest aliasModificationRequest) {

        TrackResponse response = musicCatalogTrackService
                .updateAliasName(trackId, artistId, aliasId, aliasModificationRequest);

        logger.info("Artist Alias Name changed: {}", response.id());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(value = "/artist/{artistId}/tracks",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TrackDto> getTracksByArtist(
            @PathVariable @NotNull Long artistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        logger.info("Getting Tracks by Artist: {}", artistId);

        return musicCatalogTrackService
                .getTracksByArtistId(artistId, pageable);
    }

    @Override
    @GetMapping(value = "/alias/{aliasId}/tracks",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TrackDto> getTracksByArtistAlias(
            @PathVariable @NotNull Long aliasId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        logger.info("Getting Tracks by Artist Alias: {}", aliasId);

        return musicCatalogTrackService
                .getTracksByArtistAliasId(aliasId, pageable);
    }
}
