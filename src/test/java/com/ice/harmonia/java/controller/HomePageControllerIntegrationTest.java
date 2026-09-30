package com.ice.harmonia.java.controller;

import com.ice.harmonia.java.AbstractIntegrationTest;
import com.ice.harmonia.record.FeaturedArtistResponse;
import com.ice.harmonia.service.HomePageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HomePageControllerIntegrationTest extends AbstractIntegrationTest {

    @MockitoBean
    private HomePageService homePageService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/homepage/artist/artist-of-the-day - Success with an existing artist")
    void shouldGetArtistOfTheDay_whenArtists_alreadyExists() throws Exception {

        //Arrange
        FeaturedArtistResponse mockResponse = new FeaturedArtistResponse(null,
                101L, null,
                UUID.fromString("b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e"),
                "Barry Allen",
                null, null, true
        );

        Mockito.when(homePageService.getArtistOfTheDay()).thenReturn(mockResponse);


        // Act
        String responseString = mockMvc.perform(
                        get("/api/v1/homepage/artist/artist-of-the-day")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        FeaturedArtistResponse featuredArtistResponse = objectMapper.readValue(responseString, FeaturedArtistResponse.class);

        //Assert
        assertNotNull(featuredArtistResponse);
        assertEquals(UUID.fromString("b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e"), featuredArtistResponse.artistExternalId());
        assertEquals(101L, featuredArtistResponse.artistId());
        assertEquals("Barry Allen", featuredArtistResponse.artistLegalName());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/homepage/alias/artist-of-the-day - Success with an existing artist")
    void shouldGetArtistOfTheDay_whenArtistAliases_alreadyExists() throws Exception {

        //Arrange
        FeaturedArtistResponse mockResponse = new FeaturedArtistResponse(LocalDate.now(),
                101L, 12L,
                UUID.fromString("b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e"),
                "Barry Allen",
                "Flash",
                UUID.fromString("c3b9b472-3580-482d-9861-125674c1071d"),
                true
        );

        Mockito.when(homePageService.getAliasArtistOfTheDay()).thenReturn(mockResponse);

        // Act
        String responseString = mockMvc.perform(
                        get("/api/v1/homepage/alias/artist-of-the-day")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        FeaturedArtistResponse featuredArtistResponse = objectMapper.readValue(responseString, FeaturedArtistResponse.class);

        //Assert
        assertNotNull(featuredArtistResponse);
        assertEquals(UUID.fromString("b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e"), featuredArtistResponse.artistExternalId());
        assertEquals(101L, featuredArtistResponse.artistId());
        assertEquals("Barry Allen", featuredArtistResponse.artistLegalName());
        assertEquals(UUID.fromString("c3b9b472-3580-482d-9861-125674c1071d"), featuredArtistResponse.artistAliasExternalId());
        assertEquals(12L, featuredArtistResponse.aliasId());
        assertEquals("Flash", featuredArtistResponse.artistAliasName());
    }

}
