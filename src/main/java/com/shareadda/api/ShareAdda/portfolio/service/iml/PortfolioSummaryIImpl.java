package com.shareadda.api.ShareAdda.portfolio.service.iml;

import com.shareadda.api.ShareAdda.LiveMarket.repository.CompanyAndSymbol;
import com.shareadda.api.ShareAdda.exception.ResourceNotFoundException;
import com.shareadda.api.ShareAdda.portfolio.domain.Portfolio;
import com.shareadda.api.ShareAdda.portfolio.domain.PortfolioSummary;
import com.shareadda.api.ShareAdda.portfolio.dto.PortfolioAddRequestDto;
import com.shareadda.api.ShareAdda.portfolio.repository.PortfoliosummaryRepository;
import com.shareadda.api.ShareAdda.portfolio.service.PortfolioSummaryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioSummaryIImpl implements PortfolioSummaryI {

    @Autowired
    private PortfoliosummaryRepository portfoliosummaryRepository;

    @Autowired
    private CompanyAndSymbol companyAndSymbolRepository;

    private static double ltp = 1999;

    @Override
    public PortfolioSummary save(PortfolioSummary portfolioSummary) {
        return portfoliosummaryRepository.save(portfolioSummary);
    }


    @Override
    public List<PortfolioSummary> findByUserId(String userid) {
        List<PortfolioSummary> portfolioSummary = portfoliosummaryRepository.findByUserId(userid);
        return portfolioSummary;
    }

    @Override
    public PortfolioSummary findById(String id) {
        PortfolioSummary portfolioSummary = portfoliosummaryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Portfolio Summary with id "+id+" Not found"));
        return portfolioSummary;
    }

    @Override
    public PortfolioSummary add(PortfolioAddRequestDto portfolioAddRequestDto) {

        Portfolio portfolio = new Portfolio();
        String companyNo = companyAndSymbolRepository.findBySymbol(portfolioAddRequestDto.getStockSymbol().toUpperCase()).getNumber();
        double units = portfolioAddRequestDto.getTotalUnits();
        double perunitprice = portfolioAddRequestDto.getWacc();
        //sracpe latest ltp accourding to company symbol
        double latestTotalPrice = ltp * units;

        //setting portfolio adding portfolio if it is buy
        portfolio.setStockSymbol(portfolioAddRequestDto.getStockSymbol());
        portfolio.setStockCode(companyNo);
        portfolio.setTotalUnits(units);
        portfolio.setLtp(ltp);
        portfolio.setCurrentValue(latestTotalPrice);
        portfolio.setInvestment(perunitprice*units);
        portfolio.setSoldAmount(0);
        portfolio.setSoldUnit(0);
        portfolio.setWacc(perunitprice);
        portfolio.setDividend(0);
        if(ltp>perunitprice){
            double profit = latestTotalPrice-(perunitprice*units);
            portfolio.setTodaysProfit(profit);
            portfolio.setOverallProfit(portfolio.getOverallProfit()+portfolio.getTodaysProfit());
        }else if(ltp<perunitprice){
            double loss = (perunitprice*units)-latestTotalPrice;
            portfolio.setTodaysLoss(loss);
            portfolio.setOverallLoss(portfolio.getOverallLoss()+portfolio.getTodaysLoss());

        }else{
            portfolio.setTodaysLoss(0);
            portfolio.setTodaysProfit(0);
            portfolio.setOverallLoss(portfolio.getOverallLoss());
            portfolio.setOverallProfit(portfolio.getOverallProfit());
        }

        portfolio.setTotalReceivableAmount(latestTotalPrice);
        portfolio.setUserId(portfolioAddRequestDto.getUserId());
        portfolio.setTranscationDate(portfolioAddRequestDto.getTranscationDate());
        portfolio.setTranscationType(portfolioAddRequestDto.getTranscationType());

        List<Portfolio> portfoliosList = new ArrayList<>();
        portfoliosList.add(portfolio);
        PortfolioSummary portfolioSummary = addToPortfolioSummary(portfoliosList);

        return this.save(portfolioSummary);
    }
    private PortfolioSummary addToPortfolioSummary(List<Portfolio> allPortfolio){
        PortfolioSummary portfolioSummary = new PortfolioSummary();
       double totalUnits = 0,currentValue = 0,totalInvestment = 0,totalReceivableAmount = 0,overallProfit = 0,todaysProfit = 0;
        for(int i = 0;i<allPortfolio.size();i++){
            totalUnits = totalUnits + allPortfolio.get(i).getTotalUnits();
            currentValue = currentValue + allPortfolio.get(i).getCurrentValue();
            totalInvestment = totalInvestment + allPortfolio.get(i).getInvestment();
            totalReceivableAmount = totalReceivableAmount + allPortfolio.get(i).getTotalReceivableAmount();
            overallProfit = overallProfit + allPortfolio.get(i).getOverallProfit() + allPortfolio.get(i).getOverallLoss();
            todaysProfit = todaysProfit + allPortfolio.get(i).getTodaysProfit()+allPortfolio.get(i).getTodaysLoss();

        }
        portfolioSummary.setUserId(allPortfolio.get(0).getUserId());
        portfolioSummary.setTotalUnits(totalUnits);
        portfolioSummary.setCurrentValue(currentValue);
        portfolioSummary.setTotalReceivableAmount(totalReceivableAmount);
        portfolioSummary.setOverallProfit(overallProfit);
        portfolioSummary.setTodaysProfit(todaysProfit);
        portfolioSummary.setAllPortfolio(allPortfolio);
        return portfolioSummary;


    }
}
