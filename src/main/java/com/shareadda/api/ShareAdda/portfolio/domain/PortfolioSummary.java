package com.shareadda.api.ShareAdda.portfolio.domain;

import com.shareadda.api.ShareAdda.audititing.Auditing;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "portfolio_summary")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PortfolioSummary extends Auditing implements Serializable {

    @Id
    private String id;

    private double totalUnits=0;

    private double currentValue=0;

    private double totalInvestment=0;

    private double totalReceivableAmount=0;

    private double overallProfit=0;

    private double todaysProfit=0;

    private String userId;

    private String name;

    private String type;

    private List<Portfolio> allPortfolio = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public void setTotalUnits(double totalUnits) {
        this.totalUnits = totalUnits;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public void setTotalReceivableAmount(double totalReceivableAmount) {
        this.totalReceivableAmount = totalReceivableAmount;
    }

    public void setOverallProfit(double overallProfit) {
        this.overallProfit = overallProfit;
    }

    public void setTodaysProfit(double todaysProfit) {
        this.todaysProfit = todaysProfit;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAllPortfolio(List<Portfolio> allPortfolio) {
        this.allPortfolio = allPortfolio;
    }

    public void addPortfolioToList(Portfolio portfolio){
        this.allPortfolio.add(portfolio);
    }
}
