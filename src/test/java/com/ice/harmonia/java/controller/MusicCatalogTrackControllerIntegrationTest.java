package com.ice.harmonia.java.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ice.harmonia.java.AbstractIntegrationTest;
import com.ice.harmonia.java.JsonPage;
import com.ice.harmonia.record.response.ArtistAliasResponse;
import com.ice.harmonia.record.response.ArtistResponse;
import com.ice.harmonia.record.response.TrackDto;
import com.ice.harmonia.record.response.TrackResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MusicCatalogTrackControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/music-catalog/tracks - Success with an existing artist")
    void shouldCreateTrackSuccessfully_whenArtist_alreadyExists() throws Exception {
        // Assign
        String requestJson = """
            {
              "title": "TitleTrackTest001",
              "genre": "TrackGenreTest001",
              "durationInSeconds": 326,
              "trackArtistInfos": [
                {
                    "artistExternalId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
                    "artistLegalName": "Peter Parker",
                    "artistAliasName": "Spider Man"
                }
              ]
            }
            """;

        // Act
        String responseString = mockMvc.perform(post("/api/v1/music-catalog/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();


        TrackResponse trackResponse = objectMapper.readValue(responseString, TrackResponse.class);

        //Assert
        assertNotNull(trackResponse.id());
        assertNotNull(trackResponse.externalId());
        assertEquals("TitleTrackTest001", trackResponse.title());
        assertEquals("TrackGenreTest001", trackResponse.genre());
        assertEquals(326, trackResponse.durationInSeconds());

        assertNotNull(trackResponse.artistResponse());
        assertEquals(1, trackResponse.artistResponse().size());

        ArtistResponse artistResponse = trackResponse.artistResponse().getFirst();

        assertEquals(100L, artistResponse.id());
        assertEquals(UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d"), artistResponse.externalId());
        assertEquals("Peter Parker", artistResponse.name());

        assertNotNull(artistResponse.alias());
        assertEquals(1, artistResponse.alias().size());

        ArtistAliasResponse artistAliasResponse = artistResponse.alias().getFirst();

        assertEquals(10L, artistAliasResponse.id());
        assertEquals(UUID.fromString("8a5c3b2e-1234-5678-abcd-ef1234567890"), artistAliasResponse.externalId());
        assertEquals("Spider Man", artistAliasResponse.aliasName());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/music-catalog/tracks - Success with an new artist/alias persona")
    void shouldCreateTrackSuccessfully_whenNewArtistAlias_requiredToCreate() throws Exception {
        // Assign
        String requestJson = """
            {
              "title": "TitleTrackTest002",
              "genre": "TrackGenreTest002",
              "durationInSeconds": 343,
              "trackArtistInfos": [
                {
                    "artistExternalId": "e529d4bc-afb3-4744-8f32-61987f563a72",
                    "artistLegalName": "Bruce Wayne",
                    "artistAliasName": "Bat Man"
                }
              ]
            }
            """;

        // Act
        String responseString = mockMvc.perform(post("/api/v1/music-catalog/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();


        TrackResponse trackResponse = objectMapper.readValue(responseString, TrackResponse.class);

        //Assert
        assertNotNull(trackResponse.id());
        assertNotNull(trackResponse.externalId());
        assertEquals("TitleTrackTest002", trackResponse.title());
        assertEquals("TrackGenreTest002", trackResponse.genre());
        assertEquals(343, trackResponse.durationInSeconds());

        assertNotNull(trackResponse.artistResponse());
        assertEquals(1, trackResponse.artistResponse().size());

        ArtistResponse artistResponse = trackResponse.artistResponse().getFirst();

        assertNotNull(artistResponse.id());
        assertEquals(UUID.fromString("e529d4bc-afb3-4744-8f32-61987f563a72"), artistResponse.externalId());
        assertEquals("Bruce Wayne", artistResponse.name());

        assertNotNull(artistResponse.alias());
        assertEquals(1, artistResponse.alias().size());

        ArtistAliasResponse artistAliasResponse = artistResponse.alias().getFirst();

        assertNotNull(artistAliasResponse.id());
        assertNotNull(artistAliasResponse.externalId());
        assertEquals("Bat Man", artistAliasResponse.aliasName());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/music-catalog/tracks - Success with an existing artist and new alias")
    void shouldCreateTrackSuccessfully_whenExistingArtistAndNewAlias_required() throws Exception {
        // Assign
        String requestJson = """
            {
              "title": "TitleTrackTest003",
              "genre": "TrackGenreTest003",
              "durationInSeconds": 219,
              "trackArtistInfos": [
                {
                    "artistExternalId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
                    "artistLegalName": "Peter Parker",
                    "artistAliasName": "Amazing Spider Man"
                }
              ]
            }
            """;

        // Act
        String responseString = mockMvc.perform(post("/api/v1/music-catalog/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();


        TrackResponse trackResponse = objectMapper.readValue(responseString, TrackResponse.class);

        //Assert
        assertNotNull(trackResponse.id());
        assertNotNull(trackResponse.externalId());
        assertEquals("TitleTrackTest003", trackResponse.title());
        assertEquals("TrackGenreTest003", trackResponse.genre());
        assertEquals(219, trackResponse.durationInSeconds());

        assertNotNull(trackResponse.artistResponse());
        assertEquals(1, trackResponse.artistResponse().size());

        ArtistResponse artistResponse = trackResponse.artistResponse().getFirst();

        assertNotNull(artistResponse.id());
        assertEquals(UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d"), artistResponse.externalId());
        assertEquals("Peter Parker", artistResponse.name());

        assertNotNull(artistResponse.alias());
        assertEquals(1, artistResponse.alias().size());

        ArtistAliasResponse artistAliasResponse = artistResponse.alias().getFirst();

        assertNotNull(artistAliasResponse.id());
        assertNotNull(artistAliasResponse.externalId());
        assertEquals("Amazing Spider Man", artistAliasResponse.aliasName());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/music-catalog/tracks - Failure with an existing track")
    void shouldCreateTrackThrowException_whenTrackTitleAndGenre_alreadyAvailable() throws Exception {
        // Assign
        String requestJson = """
            {
              "title": "Sing in Rain",
              "genre": "Light Music",
              "durationInSeconds": 219,
              "trackArtistInfos": [
                {
                    "artistExternalId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
                    "artistLegalName": "Peter Parker",
                    "artistAliasName": "Spider Man"
                }
              ]
            }
            """;

        // Act
        MvcResult result  = mockMvc.perform(post("/api/v1/music-catalog/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Conflict! Title {Sing in Rain} and Genre {Light Music} already exists in the database", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/music-catalog/tracks - Failure with an existing artist has new name")
    void shouldCreateTrackThrowException_whenExistingArtist_hasNewName() throws Exception {
        // Assign
        String requestJson = """
            {
              "title": "Sing in Rain",
              "genre": "Light Music",
              "durationInSeconds": 219,
              "trackArtistInfos": [
                {
                    "artistExternalId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
                    "artistLegalName": "Bruce Wayne",
                    "artistAliasName": "Spider Man"
                }
              ]
            }
            """;

        // Act
        MvcResult result  = mockMvc.perform(post("/api/v1/music-catalog/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Conflict! Existing artist name {Peter Parker} does not match the provided name {Bruce Wayne}", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /api/v1/music-catalog/tracks - Failure with an existing artist alias is inactive")
    void shouldCreateTrackThrowException_whenExistingArtistAlias_isNotActive() throws Exception {
        // Assign
        String requestJson = """
            {
              "title": "Sing in Rain",
              "genre": "Light Music",
              "durationInSeconds": 219,
              "trackArtistInfos": [
                {
                    "artistExternalId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
                    "artistLegalName": "Peter Parker",
                    "artistAliasName": "Spider Boy"
                }
              ]
            }
            """;

        // Act
        MvcResult result  = mockMvc.perform(post("/api/v1/music-catalog/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Conflict! Artist alias {Spider Boy} is not active", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId} - Success with an existing artist alias")
    void shouldUpdateTrackAliasSuccessfully_whenArtist_alreadyExists() throws Exception {
        // Assign
        Long trackId = 500L;
        Long artistId = 100L;
        Long artistAliasId = 10L;

        String requestJson = """
            {
              "aliasName": "Sand Man"
            }
            """;

        // Act
        String responseString = mockMvc.perform(
                patch("/api/v1/music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId}", trackId, artistId, artistAliasId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        TrackResponse trackResponse = objectMapper.readValue(responseString, TrackResponse.class);

        //Assert
        assertEquals(500L, trackResponse.id());
        assertEquals(UUID.fromString("cef9dd07-3c20-42bb-9cd7-7307c1673f2a"), trackResponse.externalId());
        assertEquals("Sing in Rain", trackResponse.title());
        assertEquals("Light Music", trackResponse.genre());
        assertEquals(150, trackResponse.durationInSeconds());

        assertNotNull(trackResponse.artistResponse());
        assertEquals(1, trackResponse.artistResponse().size());

        ArtistResponse artistResponse = trackResponse.artistResponse().getFirst();

        assertEquals(100L, artistResponse.id());
        assertEquals(UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d"), artistResponse.externalId());
        assertEquals("Peter Parker", artistResponse.name());

        assertNotNull(artistResponse.alias());
        assertEquals(1, artistResponse.alias().size());

        ArtistAliasResponse artistAliasResponse = artistResponse.alias().getFirst();

        assertEquals(10L, artistAliasResponse.id());
        assertEquals(UUID.fromString("8a5c3b2e-1234-5678-abcd-ef1234567890"), artistAliasResponse.externalId());
        assertEquals("Sand Man", artistAliasResponse.aliasName());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId} - Failure with no track available")
    void shouldUpdateTrackThrowException_whenTrack_isUnavailable() throws Exception {
        // Assign
        Long trackId = -500L;
        Long artistId = 100L;
        Long artistAliasId = 10L;

        String requestJson = """
            {
              "aliasName": "Sand Man"
            }
            """;

        // Act
        MvcResult result  = mockMvc.perform(
                patch("/api/v1/music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId}", trackId, artistId, artistAliasId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();
        assertNotNull(exception);
        assertEquals("Not found! Track {-500} not found", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId} - Failure with no artist available")
    void shouldUpdateTrackThrowException_whenArtist_isUnavailable() throws Exception {
        // Assign
        Long trackId = 500L;
        Long artistId = -100L;
        Long artistAliasId = 10L;

        String requestJson = """
            {
              "aliasName": "Sand Man"
            }
            """;

        // Act
        MvcResult result  = mockMvc.perform(
                        patch("/api/v1/music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId}", trackId, artistId, artistAliasId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isNotFound())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Not found! Artist {-100} not found", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId} - Failure with no artist alias available")
    void shouldUpdateTrackThrowException_whenArtistAlias_isUnavailable() throws Exception {
        // Assign
        Long trackId = 500L;
        Long artistId = 100L;
        Long artistAliasId = -10L;

        String requestJson = """
            {
              "aliasName": "Sand Man"
            }
            """;

        // Act
        MvcResult result  = mockMvc.perform(
                        patch("/api/v1/music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId}", trackId, artistId, artistAliasId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isNotFound())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Not found! Artist alias {-10} not found", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId} - Failure with no track artist relationship available")
    void shouldUpdateTrackThrowException_whenTrackArtistAliasRelationship_isUnavailable() throws Exception {
        // Assign
        Long trackId = 500L;
        Long artistId = 100L;
        Long artistAliasId = 11L;

        String requestJson = """
            {
              "aliasName": "Sand Man"
            }
            """;

        // Act
        MvcResult result  = mockMvc.perform(
                        patch("/api/v1/music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId}", trackId, artistId, artistAliasId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isNotFound())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Not found! Track Artist relation not found", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId} - Failure with no alias in active")
    void shouldUpdateTrackThrowException_whenAlias_isNotActive() throws Exception {
        // Assign
        Long trackId = 502L;
        Long artistId = 100L;
        Long artistAliasId = 11L;

        String requestJson = """
                {
                  "aliasName": "Sand Man"
                }
                """;

        // Act
        MvcResult result = mockMvc.perform(
                        patch("/api/v1/music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId}", trackId, artistId, artistAliasId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isConflict())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Conflict! Artist alias {Spider Boy} is not active", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId} - Failure with no alias name change")
    void shouldUpdateTrackThrowException_whenAliasName_isUnChanged() throws Exception {
        // Assign
        Long trackId = 500L;
        Long artistId = 100L;
        Long artistAliasId = 10L;

        String requestJson = """
                {
                  "aliasName": "Spider Man"
                }
                """;

        // Act
        MvcResult result = mockMvc.perform(
                        patch("/api/v1/music-catalog/track/{trackId}/artist/{artistId}/alias/{aliasId}", trackId, artistId, artistAliasId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isConflict())
                .andReturn();

        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) result.getResolvedException();

        assertNotNull(exception);
        assertEquals("Conflict! Alias name {Spider Man} already exists in the database", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/artist/{artistId}/tracks - Success with an existing artist")
    void shouldGetTracksSuccessfully_whenArtist_alreadyExists() throws Exception {
        // Assign
        Long artistId = 100L;

        // Act
        String responseString = mockMvc.perform(
                        get("/api/v1/music-catalog/artist/{artistId}/tracks", artistId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        Page<TrackDto> trackDtoPage = objectMapper.readValue(
                responseString,
                new TypeReference<JsonPage<TrackDto>>() {}
        );

        List<TrackDto> tracks = trackDtoPage.getContent();
        long total = trackDtoPage.getTotalElements();

        //Assert
        assertNotNull(tracks);
        assertEquals(3, total);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/alias/{aliasId}/tracks - Success with an existing alias")
    void shouldGetTracksSuccessfully_whenArtistAlias_alreadyExists() throws Exception {
        // Assign
        Long artistId = 10L;

        // Act
        String responseString = mockMvc.perform(
                        get("/api/v1/music-catalog/alias/{aliasId}/tracks", artistId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        Page<TrackDto> trackDtoPage = objectMapper.readValue(
                responseString,
                new TypeReference<JsonPage<TrackDto>>() {}
        );

        List<TrackDto> tracks = trackDtoPage.getContent();
        long total = trackDtoPage.getTotalElements();

        //Assert
        assertNotNull(tracks);
        assertEquals(2, total);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/alias/{aliasId}/tracks - Failure with an alias inactive")
    void shouldGetTracksSuccessfully_whenArtistAlias_isInActive() throws Exception {
        // Assign
        Long artistId = 11L;

        // Act
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v1/music-catalog/alias/{aliasId}/tracks", artistId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();


        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) mvcResult.getResolvedException();

        assertNotNull(exception);
        assertEquals("Conflict! Artist alias {Spider Boy} is not active", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/alias/{aliasId}/tracks - Failure with an alias unavailable")
    void shouldGetTracksSuccessfully_whenArtistAlias_isNotAvailable() throws Exception {
        // Assign
        Long artistId = -11L;

        // Act
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v1/music-catalog/alias/{aliasId}/tracks", artistId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();


        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) mvcResult.getResolvedException();

        assertNotNull(exception);
        assertEquals("NotFound! Artist alias {-11} not found", exception.getReason());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST music-catalog/artist/{artistId}/tracks - Failure with an artist unavailable")
    void shouldGetTracksSuccessfully_whenArtist_isNotAvailable() throws Exception {
        // Assign
        Long artistId = -11L;

        // Act
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v1/music-catalog/artist/{artistId}/tracks", artistId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();


        // 3. Assert
        ResponseStatusException exception = (ResponseStatusException) mvcResult.getResolvedException();

        assertNotNull(exception);
        assertEquals("NotFound! Artist {-11} not found", exception.getReason());
    }
}
