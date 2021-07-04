package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.MarketDepth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarketDepthDto {
    private String date;
    private Boolean isMarketOpen;
    private String ltp;
    private String pointschange;
    private String percentchange;
    private String buytotalqnt;
    private String selltotalqnt;
    private String previousclose;
    private String open;
    private String high;
    private String low;
    private String close;
    private List<MarketDepth> results = new ArrayList<>();
}
