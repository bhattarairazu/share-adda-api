package com.shareadda.api.ShareAdda.watchlist.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.LiveMarket;
import com.shareadda.api.ShareAdda.User.Domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WatchListResponseDto {
    private List<LiveMarket> results = new ArrayList<>();
    private User user;

}
