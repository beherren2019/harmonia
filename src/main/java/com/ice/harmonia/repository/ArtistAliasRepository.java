package com.ice.harmonia.repository;

import com.ice.harmonia.entity.Artist;
import com.ice.harmonia.entity.ArtistAlias;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistAliasRepository extends JpaRepository<ArtistAlias, Long> {

    Optional<ArtistAlias> findArtistAliasByArtistAndAliasName(Artist artist, String aliasName);

    List<ArtistAlias> findArtistAliasesByArtist(Artist artist);

    Page<ArtistAlias> findAllByIsActiveTrueAndIsVisibleInRotationTrue(Pageable pageable);

    Optional<ArtistAlias> findArtistAliasesByArtistAndAliasName(Artist artist, String aliasName);
}
