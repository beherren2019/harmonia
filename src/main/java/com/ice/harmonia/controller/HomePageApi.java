package com.ice.harmonia.controller;

import com.ice.harmonia.record.FeaturedArtistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag( name = "Homepage API", description = "Managing Homepage APIs")
public interface HomePageApi {

    @Operation(summary = "Get Artist of the Day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist of the Day retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "There is a conflict to get Artist of the Day"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<FeaturedArtistResponse> getArtistOfTheDay();

    @Operation(summary = "Get Artist of the Day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist of the Day retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "There is a conflict to get Artist of the Day"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<FeaturedArtistResponse> getArtistAliasOfTheDay();

}
