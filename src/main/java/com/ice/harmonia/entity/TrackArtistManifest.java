package com.ice.harmonia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "track_artist_manifest")
//@IdClass(TrackArtistId.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackArtistManifest {

    @EmbeddedId
    private TrackArtistId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trackId")
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistId")
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alias_id")
    private ArtistAlias artistAlias;

    @Column(name = "alias_name_snapshot")
    private String aliasNameSnapshot;
}
