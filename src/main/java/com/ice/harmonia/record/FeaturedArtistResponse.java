package com.ice.harmonia.record;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record FeaturedArtistResponse(
        LocalDate date,
        Long artistId,
        Long aliasId,
        UUID artistExternalId,
        String artistLegalName,
        String artistAliasName,
        UUID artistAliasExternalId,
        boolean isVisibleInRotation
) {
}
