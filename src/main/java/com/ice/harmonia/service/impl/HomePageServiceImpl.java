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
    public FeaturedArtistResponse getArtistOfTheDay(boolean isAlias) {
        if (isAlias) {
            return fetchAliasArtistOfTheDay();
        } else {
            return fetchArtistOfTheDay();
        }
    }

    private FeaturedArtistResponse fetchAliasArtistOfTheDay() {
        long totalArtistAliases = artistAliasRepository.count();

        if (totalArtistAliases == 0) {
            logger.error("No artist aliases available in the catalog.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No artists available in the catalog.");
        }

        // 1. Calculate how many days have passed since our base date
        long daysElapsed = ChronoUnit.DAYS.between(EPOCH_BASE, LocalDate.now());

        // 2. Use modulo to get a 0-indexed position (e.g., if 10 artists, returns 0 to 9)
        int targetIndex = (int) (daysElapsed % totalArtistAliases);

        // 3. Directly fetch the artist at that offset position (Handles ID/index gaps perfectly)
        ArtistAlias artistAlias = artistAliasRepository.findAllByIsActiveTrueAndIsVisibleInRotationTrue(PageRequest.of(targetIndex, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Catalog sync error while fetching artist."));


        return artistAliasMapper.mapEntityToFeaturedArtistResponse(artistAlias);
    }

    private FeaturedArtistResponse fetchArtistOfTheDay() {
        long totalArtists = artistRepository.count();

        if (totalArtists == 0) {
            logger.error("No artist available in the catalog.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No artists available in the catalog.");
        }

        // 1. Calculate how many days have passed since our base date
        long daysElapsed = ChronoUnit.DAYS.between(EPOCH_BASE, LocalDate.now());

        // 2. Use modulo to get a 0-indexed position (e.g., if 10 artists, returns 0 to 9)
        int targetIndex = (int) (daysElapsed % totalArtists);

        // 3. Directly fetch the artist at that offset position (Handles ID/index gaps perfectly)
        Artist artist = artistRepository.findAll(PageRequest.of(targetIndex, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Catalog sync error while fetching artist."));


        return artistMapper.mapEntityToFeaturedArtistResponse(artist);
    }
}
