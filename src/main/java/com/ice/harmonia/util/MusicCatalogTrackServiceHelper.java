package com.ice.harmonia.util;

import com.ice.harmonia.record.TrackArtistInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MusicCatalogTrackServiceHelper {

    private final static Logger logger = LoggerFactory.getLogger(MusicCatalogTrackServiceHelper.class);

    private MusicCatalogTrackServiceHelper() {}

    public static void validateTrackArtistInfos(List<TrackArtistInfo> trackArtistInfos) {
        if (trackArtistInfos == null || trackArtistInfos.isEmpty()) {
            throw new RuntimeException("Track must have an artist associated");
        }

        if (trackArtistInfos.size() > 1) {
            throw new RuntimeException("Track must have only one artist associated on creation");
        }

        logger.info("Track Artist Info validation success!");
    }
}
