-- Seed Initial Artists
INSERT INTO artists (id, external_id, name, created_at, created_by) VALUES
(100, 'a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', 'Peter Parker', CURRENT_TIMESTAMP, 'USER'),
(101, 'b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', 'Barry Allen', CURRENT_TIMESTAMP, 'USER');

-- Seed Initial Artist Monikers/Aliases
INSERT INTO artist_aliases (id, external_id, artist_id, alias_name, is_visible_in_rotation, is_active, created_at, created_by) VALUES
(10, '8a5c3b2e-1234-5678-abcd-ef1234567890', 100, 'Spider Man', TRUE, TRUE,CURRENT_TIMESTAMP, 'USER'),
(11, '9b6d4c3f-2345-6789-bcde-f0123456789a', 100, 'Spider Boy', FALSE, FALSE, CURRENT_TIMESTAMP, 'USER'),
(12, 'c3b9b472-3580-482d-9861-125674c1071d', 101, 'Flash', TRUE, TRUE, CURRENT_TIMESTAMP, 'USER');

-- Seed Initial Tracks
INSERT INTO tracks(id, external_id, title, genre, duration_in_seconds, created_at, created_by) VALUES
(500, 'cef9dd07-3c20-42bb-9cd7-7307c1673f2a', 'Sing in Rain', 'Light Music', 150,CURRENT_TIMESTAMP, 'USER'),
(501, 'c4395dc3-7064-4383-acf2-c47a253a373f', 'My Life', 'Hard music', 22, CURRENT_TIMESTAMP, 'USER'),
(502, 'dd929726-6496-4332-9123-40686e5b035e', 'Wonder in World', 'Romance', 100, CURRENT_TIMESTAMP, 'USER');

-- Seed Initial track_artist_manifest
INSERT INTO track_artist_manifest(track_id, artist_id, alias_id, alias_name_snapshot) VALUES
(500, 100, 10, 'Spider Man'),
(501, 100, 10, 'Spider Man'),
(502, 100, 11, 'Spider Boy');
