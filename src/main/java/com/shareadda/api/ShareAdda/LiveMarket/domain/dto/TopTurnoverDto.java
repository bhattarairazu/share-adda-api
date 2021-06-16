package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopLoosers;
import com.shareadda.api.ShareAdda.LiveMarket.domain.TopTurnOver;
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
public class TopTurnoverDto {
    private String date;
    private List<TopTurnOver> topTurnOverList = new ArrayList<>();

}
