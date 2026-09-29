package com.ice.harmonia.mapper;

import com.ice.harmonia.entity.TrackArtistManifest;
import com.ice.harmonia.record.response.TrackDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TrackDtoMapper {

    public Page<TrackDto>  pageToTrackDtoPage(Page<TrackArtistManifest> page) {
        return page.map(entity -> TrackDto.builder()
                .id(entity.getTrack().getId())
                .externalId(entity.getTrack().getExternalId())
                .title(entity.getTrack().getTitle())
                .genre(entity.getTrack().getGenre())
                .durationInSeconds(entity.getTrack().getDurationInSeconds())
                .build());
    }
}
