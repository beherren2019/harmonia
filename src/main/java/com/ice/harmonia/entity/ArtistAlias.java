package com.ice.harmonia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "artist_aliases")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistAlias implements Identifiable, AuditSnapshotProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true, nullable = false, updatable = false)
    private UUID externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "alias_name", nullable = false)
    private String aliasName;

    @Column(name = "is_visible_in_rotation", nullable = false, columnDefinition = "boolean default true")
    private Boolean isVisibleInRotation = true;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;

    public String toAuditSnapshotLog() {
        return this.aliasName;
    }
}
