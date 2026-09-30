package com.ice.harmonia.controller;


import com.ice.harmonia.record.FeaturedArtistResponse;
import com.ice.harmonia.service.HomePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomePageController implements HomePageApi {

    private final Logger logger = LoggerFactory.getLogger(HomePageController.class);

    private final HomePageService homePageService;

    public HomePageController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @Override
    @GetMapping(
            value = "/homepage/artist/artist-of-the-day",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FeaturedArtistResponse> getArtistOfTheDay() {

        FeaturedArtistResponse response = homePageService.getArtistOfTheDay();

        logger.info("Artist of the Day: {}", response.artistId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(
            value = "/homepage/alias/artist-of-the-day",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FeaturedArtistResponse> getArtistAliasOfTheDay() {

        FeaturedArtistResponse response = homePageService.getAliasArtistOfTheDay();

        logger.info("Artist of the Day: {}", response.artistId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
