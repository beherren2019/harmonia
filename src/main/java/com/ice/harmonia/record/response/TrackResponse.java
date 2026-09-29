package com.ice.harmonia.record.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record TrackResponse(
        Long id,
        UUID externalId,
        String title,
        String genre,
        int durationInSeconds,
        List<ArtistResponse> artistResponse
) {
}
