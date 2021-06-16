package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopLoosers;
import com.shareadda.api.ShareAdda.LiveMarket.domain.TopShareTraded;
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
public class TopShareTradedDto {
    private String date;
    private List<TopShareTraded> topShareTradedList = new ArrayList<>();

}
