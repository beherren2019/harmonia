package com.ice.harmonia.service;

import com.ice.harmonia.record.FeaturedArtistResponse;

public interface HomePageService {

    FeaturedArtistResponse getAliasArtistOfTheDay();

    FeaturedArtistResponse getArtistOfTheDay();
}
