package com.ice.harmonia.mapper;

import com.ice.harmonia.entity.Artist;
import com.ice.harmonia.entity.ArtistAlias;
import com.ice.harmonia.record.FeaturedArtistResponse;
import com.ice.harmonia.record.response.ArtistAliasResponse;
import com.ice.harmonia.record.response.ArtistResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ArtistMapper {

    private final ArtistAliasMapper artistAliasMapper;

    public ArtistMapper(ArtistAliasMapper artistAliasMapper) {
        this.artistAliasMapper = artistAliasMapper;
    }

    public ArtistResponse mapEntityToResponse(Artist artist, ArtistAlias artistAlias) {

        ArtistAliasResponse artistAliasResponse = artistAliasMapper.mapEntityToResponse(artistAlias);

        return ArtistResponse.builder()
                .id(artist.getId())
                .externalId(artist.getExternalId())
                .name(artist.getName())
                .alias(List.of(artistAliasResponse))
                .build();
    }


    public FeaturedArtistResponse mapEntityToFeaturedArtistResponse(Artist artist) {

        return FeaturedArtistResponse.builder()
                .date(LocalDate.now())
                .artistId(artist.getId())
                .artistLegalName(artist.getName())
                .artistExternalId(artist.getExternalId())
                .build();
    }
}
