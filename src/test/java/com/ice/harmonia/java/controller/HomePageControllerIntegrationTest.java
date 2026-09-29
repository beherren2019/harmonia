package com.ice.harmonia.java.controller;

import com.ice.harmonia.java.AbstractIntegrationTest;
import com.ice.harmonia.record.FeaturedArtistResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomePageControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("POST /api/v1/homepage/artist-of-the-day - Success with an existing artist")
    void shouldGetArtistOfTheDay_whenArtists_alreadyExists() throws Exception {
        // Assign
        boolean isAlias = false;

        // Act
        String responseString = mockMvc.perform(
                get("/api/v1/homepage/artist-of-the-day")
                        .param("isAlias", String.valueOf(isAlias))
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
    @DisplayName("POST /api/v1/homepage/artist-of-the-day - Success with an existing artist")
    void shouldGetArtistOfTheDay_whenArtistAliases_alreadyExists() throws Exception {
        // Assign
        boolean isAlias = true;

        // Act
        String responseString = mockMvc.perform(
                        get("/api/v1/homepage/artist-of-the-day")
                                .param("isAlias", String.valueOf(isAlias))
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
