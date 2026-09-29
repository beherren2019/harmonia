package com.ice.harmonia.record.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ArtistResponse(
        Long id,
        UUID externalId,
        String name,
        List<ArtistAliasResponse> alias
) {
}
