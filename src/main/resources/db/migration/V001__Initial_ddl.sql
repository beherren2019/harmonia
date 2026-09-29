-- 1. CORE ARTISTS TABLE
CREATE TABLE IF NOT EXISTS artists (
    id BIGSERIAL PRIMARY KEY,
    external_id UUID DEFAULT gen_random_uuid(), -- Exposed to the API layer
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255) NOT NULL DEFAULT 'ADMIN',
    updated_by VARCHAR(255),
    UNIQUE (external_id)
);

-- Create an explicit ENUM to categorize database operation types safely
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'audit_action_type') THEN
    CREATE TYPE audit_action_type AS ENUM ('INSERT', 'UPDATE', 'DELETE');
  END IF;
END $$;

-- 1.1. ARTISTS AUDIT TABLE
CREATE TABLE IF NOT EXISTS artists_audit (
    audit_id BIGSERIAL PRIMARY KEY,
    artist_id BIGINT NOT NULL,
    external_id UUID NOT NULL,
    action_type audit_action_type NOT NULL,
    old_state JSONB,
    new_state JSONB,
    changed_by VARCHAR(255) NOT NULL DEFAULT 'SYSTEM',
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_artists_audit_source ON artists_audit(artist_id);
CREATE INDEX IF NOT EXISTS idx_artists_audit_time ON artists_audit(changed_at DESC);

-- 2. ARTIST ALIASES TABLE
CREATE TABLE IF NOT EXISTS artist_aliases (
    id BIGSERIAL PRIMARY KEY,
    external_id UUID NOT NULL DEFAULT gen_random_uuid(),
    artist_id BIGINT NOT NULL,
    alias_name VARCHAR(255) NOT NULL,
    is_visible_in_rotation BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255) NOT NULL DEFAULT 'ADMIN',
    updated_by VARCHAR(255),

    CONSTRAINT FK_artist FOREIGN KEY(artist_id) REFERENCES artists(id) ON DELETE CASCADE,
    UNIQUE(external_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_aliases_external_id ON artist_aliases(external_id);
CREATE INDEX IF NOT EXISTS idx_aliases_rotation ON artist_aliases(is_visible_in_rotation, id);

-- 2.1 ARTIST ALIASES AUDIT TABLE
CREATE TABLE IF NOT EXISTS artist_aliases_audit (
    audit_id BIGSERIAL PRIMARY KEY,
    alias_id BIGINT NOT NULL,
    external_id UUID NOT NULL,
    artist_id BIGINT NOT NULL,
    action_type audit_action_type NOT NULL,
    old_state JSONB,
    new_state JSONB,
    changed_by VARCHAR(255) NOT NULL DEFAULT 'SYSTEM',
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_aliases_audit_source ON artist_aliases_audit(alias_id);
CREATE INDEX IF NOT EXISTS idx_aliases_audit_artist ON artist_aliases_audit(artist_id);
CREATE INDEX IF NOT EXISTS idx_aliases_audit_time ON artist_aliases_audit(changed_at DESC);

-- 3. TRACKS TABLE
CREATE TABLE IF NOT EXISTS tracks (
    id BIGSERIAL PRIMARY KEY,
    external_id UUID NOT NULL DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(255),
    duration_in_seconds INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255) NOT NULL DEFAULT 'ADMIN',
    updated_by VARCHAR(255),
    UNIQUE(external_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_tracks_external_id ON tracks(external_id);

-- 3.1 TRACKS AUDIT TABLE
CREATE TABLE IF NOT EXISTS tracks_audit (
    audit_id BIGSERIAL PRIMARY KEY,
    track_id BIGINT NOT NULL,
    external_id UUID NOT NULL,
    action_type audit_action_type NOT NULL,
    old_state JSONB,
    new_state JSONB,
    changed_by VARCHAR(255) NOT NULL DEFAULT 'SYSTEM',
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tracks_audit_source ON tracks_audit(track_id);
CREATE INDEX IF NOT EXISTS idx_tracks_audit_time ON tracks_audit(changed_at DESC);

-- 4. THREE-WAY LOOKUP MANIFEST (With Disaster Recovery Snapshot)
CREATE TABLE IF NOT EXISTS track_artist_manifest (
    track_id BIGINT NOT NULL REFERENCES tracks(id) ON DELETE CASCADE,
    artist_id BIGINT NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    alias_id BIGINT REFERENCES artist_aliases(id) ON DELETE SET NULL,
    alias_name_snapshot VARCHAR(255) NOT NULL,
    PRIMARY KEY (track_id, artist_id)
);

CREATE INDEX IF NOT EXISTS idx_manifest_track ON track_artist_manifest(track_id);
CREATE INDEX IF NOT EXISTS idx_manifest_alias ON track_artist_manifest(alias_id);

-- 4.1 THREE-WAY MANIFEST AUDIT TABLE
CREATE TABLE IF NOT EXISTS track_artist_manifest_audit (
    audit_id BIGSERIAL PRIMARY KEY,
    track_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
    action_type audit_action_type NOT NULL,
    old_state JSONB,
    new_state JSONB,
    changed_by VARCHAR(255) NOT NULL DEFAULT 'SYSTEM',
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_manifest_audit_composite ON track_artist_manifest_audit(track_id, artist_id);
CREATE INDEX IF NOT EXISTS idx_manifest_audit_time ON track_artist_manifest_audit(changed_at DESC);

