package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.IndiciesSubindicies;
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
public class IndiciesSubIndiciesDto {
    private String date;
    private List<IndiciesSubindicies> results = new ArrayList<>();
}
