package com.ice.harmonia.record.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TrackDto(
        Long id,
        UUID externalId,
        String title,
        String genre,
        int durationInSeconds
) {
}
