package com.ice.harmonia.mapper;

import com.ice.harmonia.entity.Artist;
import com.ice.harmonia.entity.ArtistAlias;
import com.ice.harmonia.entity.Track;
import com.ice.harmonia.record.response.ArtistResponse;
import com.ice.harmonia.record.response.TrackResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrackResponseMapper {

    private final ArtistMapper artistMapper;

    public TrackResponseMapper(ArtistMapper artistMapper) {
        this.artistMapper = artistMapper;
    }

    public TrackResponse mapEntityToResponse(Track track, Artist artist, ArtistAlias artistAlias) {

        ArtistResponse artistResponse = artistMapper.mapEntityToResponse(artist, artistAlias);

        return TrackResponse.builder()
                .id(track.getId())
                .title(track.getTitle())
                .genre(track.getGenre())
                .externalId(track.getExternalId())
                .durationInSeconds(track.getDurationInSeconds())
                .artistResponse(List.of(artistResponse))
                .build();
    }
}
