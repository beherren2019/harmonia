package com.ice.harmonia.service;

import com.ice.harmonia.record.AliasModificationRequest;
import com.ice.harmonia.record.TrackCreationRequest;
import com.ice.harmonia.record.response.TrackDto;
import com.ice.harmonia.record.response.TrackResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MusicCatalogTrackService {

    TrackResponse createTrack(@Valid @NotNull TrackCreationRequest trackCreationRequest);

    TrackResponse updateAliasName(
            @NotNull Long trackId,
            @NotNull Long artistId,
            @NotNull Long aliasId,
            @Valid AliasModificationRequest aliasModificationRequest);

    Page<TrackDto> getTracksByArtistId(@NotNull Long artistId, boolean isAlias, Pageable pageable);
}
