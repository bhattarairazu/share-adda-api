package com.shareadda.api.ShareAdda.LiveMarket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarketDepth implements Serializable {
    private String buyorders;
    private String buyqty;
    private String buyprice;
    private String sellprice;
    private String sellqty;
    private String sellorders;

}
