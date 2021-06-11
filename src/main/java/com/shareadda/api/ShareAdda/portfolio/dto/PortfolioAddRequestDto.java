package com.shareadda.api.ShareAdda.portfolio.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PortfolioAddRequestDto implements Serializable {
    String stockSymbol;
    double totalUnits;
    double perUnitPrice;
    String userId;

}
