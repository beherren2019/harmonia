package com.ice.harmonia.record.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ArtistAliasResponse(
        Long id,
        UUID externalId,
        String aliasName,
        boolean isActive,
        boolean isVisibleInRotation
) {
}
