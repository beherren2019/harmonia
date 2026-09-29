package com.ice.harmonia.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TrackArtistInfo(
        @NotNull(message = "External Artist ID is required to maintain across services")
        UUID artistExternalId,

        @NotBlank(message = "Legal name of artist is required")
        String artistLegalName,

        @NotNull(message = "Artist alias name for current track must be specified")
        String artistAliasName
) {
}
