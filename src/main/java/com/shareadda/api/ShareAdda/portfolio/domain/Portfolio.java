package com.shareadda.api.ShareAdda.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio implements Serializable {

    private String stockSymbol;
    private String stockCode;
    private double totalUnits=0;
    private double ltp=0;
    private double currentValue=0;
    private double investment=0;
    private double soldAmount=0;
    private double soldUnit=0;
    private double wacc=0;
    private double dividend=0;
    private double todaysLoss=0;
    private double todaysProfit=0;
    private double overallLoss=0;
    private double overallProfit=0;
    private double totalReceivableAmount=0;
    private String userId;

}
