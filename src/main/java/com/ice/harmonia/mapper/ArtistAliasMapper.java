package com.ice.harmonia.mapper;

import com.ice.harmonia.entity.ArtistAlias;
import com.ice.harmonia.record.FeaturedArtistResponse;
import com.ice.harmonia.record.response.ArtistAliasResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ArtistAliasMapper {

    public ArtistAliasResponse mapEntityToResponse(ArtistAlias artistAlias) {

        return ArtistAliasResponse.builder()
                .id(artistAlias.getId())
                .aliasName(artistAlias.getAliasName())
                .externalId(artistAlias.getExternalId())
                .isActive(artistAlias.getIsActive())
                .isVisibleInRotation(artistAlias.getIsVisibleInRotation())
                .build();
    }

    public FeaturedArtistResponse mapEntityToFeaturedArtistResponse(ArtistAlias artistAlias) {

        return FeaturedArtistResponse.builder()
                .date(LocalDate.now())
                .artistId(artistAlias.getArtist().getId())
                .artistLegalName(artistAlias.getArtist().getName())
                .artistExternalId(artistAlias.getArtist().getExternalId())
                .aliasId(artistAlias.getId())
                .artistAliasExternalId(artistAlias.getExternalId())
                .artistAliasName(artistAlias.getAliasName())
                .build();
    }
}
