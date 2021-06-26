package com.shareadda.api.ShareAdda.watchlist.service.impl;


import com.shareadda.api.ShareAdda.watchlist.domain.Watchlist;
import com.shareadda.api.ShareAdda.watchlist.dto.WatchListResponseDto;
import com.shareadda.api.ShareAdda.watchlist.dto.WatchlistRequestDto;

import java.io.IOException;

public interface WatchlistService {
    WatchListResponseDto save(WatchlistRequestDto watchlist) throws IOException;

    WatchListResponseDto findByUserId(String userId) throws IOException;

}
