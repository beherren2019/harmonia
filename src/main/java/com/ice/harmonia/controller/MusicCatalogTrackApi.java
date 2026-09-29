package com.ice.harmonia.controller;

import com.ice.harmonia.record.AliasModificationRequest;
import com.ice.harmonia.record.TrackCreationRequest;
import com.ice.harmonia.record.response.TrackDto;
import com.ice.harmonia.record.response.TrackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag( name = "Music Meta Data API", description = "Managing Musician's Tracks")
public interface MusicCatalogTrackApi {

    @Operation(summary = "Add a new Track")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Track added successfully"),
            @ApiResponse(responseCode = "409", description = "There is a conflict to add a Track"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<TrackResponse> addTrack(@RequestBody @Valid TrackCreationRequest trackCreationRequest);


    @Operation(summary = "Change Artist Alias Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist Alias Name changed successfully"),
            @ApiResponse(responseCode = "404", description = "Track not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "409", description = "There is a conflict to change Artist Alias Name")
    })
    ResponseEntity<TrackResponse> changeArtistAliasName(
            @PathVariable @NotNull Long trackId,
            @PathVariable @NotNull Long artistId,
            @PathVariable @NotNull Long aliasId,
            @RequestBody @Valid AliasModificationRequest aliasModificationRequest);

    @Operation(summary = "Get Tracks by User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tracks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "There is a conflict to get Tracks")
    })
    Page<TrackDto> getTracksByArtist(
            @PathVariable @NotNull Long artistId,
            @RequestParam(required = false, defaultValue = "true") boolean isAlias,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);

}
