package com.ice.harmonia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackArtistId implements Serializable {

    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "artist_id")
    private Long artistId;
}
