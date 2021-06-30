package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.LiveMarket;
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
public class LiveMarketDto {
    private String date;
    private Boolean isMarketOpen;
    private List<LiveMarket> results = new ArrayList<>();
}
