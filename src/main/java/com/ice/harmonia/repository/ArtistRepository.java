package com.ice.harmonia.repository;

import com.ice.harmonia.entity.Artist;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findArtistByExternalId(@NotNull(message = "External Artist ID is required to maintain across services") UUID uuid);

}
