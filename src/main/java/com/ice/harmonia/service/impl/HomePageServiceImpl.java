package com.ice.harmonia.service.impl;

import com.ice.harmonia.entity.Artist;
import com.ice.harmonia.entity.ArtistAlias;
import com.ice.harmonia.mapper.ArtistAliasMapper;
import com.ice.harmonia.mapper.ArtistMapper;
import com.ice.harmonia.record.FeaturedArtistResponse;
import com.ice.harmonia.repository.ArtistAliasRepository;
import com.ice.harmonia.repository.ArtistRepository;
import com.ice.harmonia.service.HomePageService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class HomePageServiceImpl implements HomePageService {

    private final Logger logger = LoggerFactory.getLogger(HomePageServiceImpl.class);

    private final ArtistAliasRepository artistAliasRepository;

    private final ArtistMapper artistMapper;

    private final ArtistRepository artistRepository;

    private final ArtistAliasMapper artistAliasMapper;

    private static final LocalDate EPOCH_BASE = LocalDate.of(2026, 1, 1);

    public HomePageServiceImpl(ArtistAliasRepository artistAliasRepository, ArtistMapper artistMapper, ArtistRepository artistRepository, ArtistAliasMapper artistAliasMapper) {
        this.artistAliasRepository = artistAliasRepository;
        this.artistMapper = artistMapper;
        this.artistRepository = artistRepository;
        this.artistAliasMapper = artistAliasMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public FeaturedArtistResponse getAliasArtistOfTheDay() {
        return fetchAliasArtistOfTheDay();
    }

    @Override
    @Transactional(readOnly = true)
    public FeaturedArtistResponse getArtistOfTheDay() {
        return fetchArtistOfTheDay();
    }


    private FeaturedArtistResponse fetchAliasArtistOfTheDay() {
        long totalArtistAliases = artistAliasRepository.countByIsActiveTrueAndIsVisibleInRotationTrue();
       // long totalArtistAliases = artistAliasRepository.count();

        if (totalArtistAliases == 0) {
            logger.error("No artist aliases available in the catalog.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No artists available in the catalog.");
        }

        int targetIndex = getTargetIndex(totalArtistAliases);

        ArtistAlias artistAlias = artistAliasRepository.findAllByIsActiveTrueAndIsVisibleInRotationTrue(PageRequest.of(targetIndex, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseGet(() -> artistAliasRepository.findAllByIsActiveTrueAndIsVisibleInRotationTrue(PageRequest.of(0, 1))
                        .getContent()
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Catalog sync error while fetching artist.")));



        return artistAliasMapper.mapEntityToFeaturedArtistResponse(artistAlias);
    }

    private FeaturedArtistResponse fetchArtistOfTheDay() {
        long totalArtists = artistRepository.count();

        if (totalArtists == 0) {
            logger.error("No artist available in the catalog.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No artists available in the catalog.");
        }

        int targetIndex = getTargetIndex(totalArtists);

        Artist artist = artistRepository.findAll(PageRequest.of(targetIndex, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Catalog sync error while fetching artist."));


        return artistMapper.mapEntityToFeaturedArtistResponse(artist);
    }

    private int getTargetIndex(long totalItems) {
        long daysElapsed = ChronoUnit.DAYS.between(EPOCH_BASE, LocalDate.now());
        return (int) (daysElapsed % totalItems);
    }

}
