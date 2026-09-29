package com.ice.harmonia.repository;

import com.ice.harmonia.entity.Track;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findTrackByTitleAndGenre(@NotBlank String title, @NotBlank String genre);
}
