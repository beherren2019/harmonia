package com.ice.harmonia.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record TrackCreationRequest(
        @NotBlank(message = "Title cannot be empty")
        String title,

        @NotBlank(message = "Genre cannot be empty")
        String genre,

        @Positive(message = "Duration must be positive number of seconds")
        int durationInSeconds,

        @NotNull(message = "Track must have an artist associated")
        List<TrackArtistInfo> trackArtistInfos
) {
}
