package com.ice.harmonia.repository;

import com.ice.harmonia.entity.Artist;
import com.ice.harmonia.entity.Track;
import com.ice.harmonia.entity.TrackArtistManifest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackArtistManifestRepository extends JpaRepository<TrackArtistManifest, Long> {

    Page<TrackArtistManifest> findTrackArtistManifestsByArtist(Artist artist, Pageable pageable);

    Optional<TrackArtistManifest> findByTrackAndArtistAndAliasId(Track track, Artist artist, Long aliasId);

    Page<TrackArtistManifest> findTrackArtistManifestsByAliasId(Long aliasId, Pageable pageable);
}
