package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopGainers;
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
public class TopGainersDto {
    private String date;
    private List<TopGainers> results = new ArrayList<>();
}
