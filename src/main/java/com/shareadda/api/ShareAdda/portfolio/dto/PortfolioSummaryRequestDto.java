package com.shareadda.api.ShareAdda.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioSummaryRequestDto {
    private String name;
    private String type;
    private String userId;
}
