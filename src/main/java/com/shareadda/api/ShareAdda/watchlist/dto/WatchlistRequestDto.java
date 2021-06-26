package com.shareadda.api.ShareAdda.watchlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistRequestDto {
    private String userId;
    private String symbol;
}
