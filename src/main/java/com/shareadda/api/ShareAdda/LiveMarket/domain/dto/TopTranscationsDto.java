package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopLoosers;
import com.shareadda.api.ShareAdda.LiveMarket.domain.TopTranscations;
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
public class TopTranscationsDto {
    private String date;
    private List<TopTranscations> results = new ArrayList<>();

}
