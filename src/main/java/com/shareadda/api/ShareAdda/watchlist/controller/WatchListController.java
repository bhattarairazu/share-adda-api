package com.shareadda.api.ShareAdda.watchlist.controller;

import com.shareadda.api.ShareAdda.watchlist.dto.WatchListResponseDto;
import com.shareadda.api.ShareAdda.watchlist.dto.WatchlistRequestDto;
import com.shareadda.api.ShareAdda.watchlist.service.impl.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/watchlist")
public class WatchListController {
    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    public ResponseEntity<WatchListResponseDto> postWatchList(@Valid @RequestBody WatchlistRequestDto watchlistRequestDto) throws IOException {
        return new ResponseEntity<>(watchlistService.save(watchlistRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WatchListResponseDto> getWatchList(@PathVariable String userId) throws IOException {
        return new ResponseEntity<>(watchlistService.findByUserId(userId),HttpStatus.OK);
    }
}
