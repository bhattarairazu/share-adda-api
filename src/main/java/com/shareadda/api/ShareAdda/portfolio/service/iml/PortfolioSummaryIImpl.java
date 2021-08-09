package com.shareadda.api.ShareAdda.portfolio.service.iml;

import com.shareadda.api.ShareAdda.LiveMarket.domain.CompanyDetails;
import com.shareadda.api.ShareAdda.LiveMarket.repository.CompanyAndSymbol;
import com.shareadda.api.ShareAdda.LiveMarket.service.ScrappingService;
import com.shareadda.api.ShareAdda.exception.BackendException;
import com.shareadda.api.ShareAdda.exception.ResourceNotFoundException;
import com.shareadda.api.ShareAdda.portfolio.domain.Portfolio;
import com.shareadda.api.ShareAdda.portfolio.domain.PortfolioSummary;
import com.shareadda.api.ShareAdda.portfolio.dto.PortfolioAddRequestDto;
import com.shareadda.api.ShareAdda.portfolio.repository.PortfoliosummaryRepository;
import com.shareadda.api.ShareAdda.portfolio.service.PortfolioSummaryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioSummaryIImpl implements PortfolioSummaryI {

    @Autowired
    private PortfoliosummaryRepository portfoliosummaryRepository;

    @Autowired
    private CompanyAndSymbol companyAndSymbolRepository;

    @Autowired
    private ScrappingService scrappingService;

    //private static double ltp = 1999;

    @Override
    public PortfolioSummary save(PortfolioSummary portfolioSummary) {
        return portfoliosummaryRepository.save(portfolioSummary);
    }

    private String getLtp(String symbol) throws IOException {
        CompanyDetails companyDetails = scrappingService.getCompanyDetails(symbol);
        String marketprice = companyDetails.getMarketPrice().replace(",","");
        return marketprice;

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
    public void deleteById(String id) {
        portfoliosummaryRepository.deleteById(id);
    }

    @Override
    public PortfolioSummary deleteByIdAndSymbol(String id,String symbol){
        PortfolioSummary portfolioSummary = portfoliosummaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio with id " + id + " Not found"));

        for(int i = 0;i<portfolioSummary.getAllPortfolio().size();i++){
            if (portfolioSummary.getAllPortfolio().get(i).getStockSymbol().equalsIgnoreCase(symbol.toUpperCase())) {
                portfolioSummary.getAllPortfolio().remove(i);
            }
        }
        if(portfolioSummary.getAllPortfolio().size()<=0){
            deleteById(portfolioSummary.getId());
            return null;
        }
        PortfolioSummary deletedPortfolioSummary = addToPortfolioSummary(portfolioSummary.getAllPortfolio(),true,portfolioSummary);
        return this.save(deletedPortfolioSummary);

    }
    @Override
    public PortfolioSummary add(PortfolioAddRequestDto portfolioAddRequestDto) {
        PortfolioSummary portfolioSummaryUser = portfoliosummaryRepository.findById(portfolioAddRequestDto.getPortfolioSummaryId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio Summary with id " + portfolioAddRequestDto.getPortfolioSummaryId() + " Not found.Create Portfolio At First"));
        //List<String> listOfPortfolioSymbol = portfolioSummaryUser.getAllPortfolio().stream().map(Portfolio::getStockSymbol).collect(Collectors.toList());
        Double ltp = 0.00;
        try{
             ltp = Double.parseDouble(getLtp(portfolioAddRequestDto.getStockSymbol()));
        }catch (IOException ex){

        }
        Portfolio portfolio = null;
        int currentIndex = -1;
        if(portfolioSummaryUser.getAllPortfolio().size()>0) {
            for (int i = 0; i < portfolioSummaryUser.getAllPortfolio().size(); i++) {
                if (portfolioAddRequestDto.getStockSymbol().equalsIgnoreCase(portfolioSummaryUser.getAllPortfolio().get(i).getStockSymbol())) {
                    System.out.println("inside match index");
                    portfolio = portfolioSummaryUser.getAllPortfolio().get(i);
                    currentIndex = i;
                    System.out.println("current index "+currentIndex);
                    break;
                }
            }
        }

        if(portfolio == null) portfolio = new Portfolio();
        String companyNo = companyAndSymbolRepository.findBySymbol(portfolioAddRequestDto.getStockSymbol().toUpperCase()).getNumber();

        double units = portfolioAddRequestDto.getTotalUnits();
        double perunitprice = portfolioAddRequestDto.getWacc();
        //sracpe latest ltp accourding to company symbol
        double latestTotalPrice = ltp * units;
        //setting portfolio adding portfolio if it is buy
        portfolio.setStockSymbol(portfolioAddRequestDto.getStockSymbol());
        portfolio.setStockCode(companyNo);
        portfolio.setLtp(ltp);
        portfolio.setWacc((portfolio.getWacc()+perunitprice)/2);
        portfolio.setDividend(0);

        if(portfolioAddRequestDto.getTranscationType().equalsIgnoreCase("SECONDARY_SELL")){
            if(portfolio.getTotalUnits()<portfolioAddRequestDto.getTotalUnits())
                throw new BackendException("You dont' have enough units to sell.Sell Units exceeds the current total units");
            portfolio.setTotalUnits(portfolio.getTotalUnits()-units);
            portfolio.setCurrentValue(portfolio.getCurrentValue()-latestTotalPrice);
            portfolio.setInvestment(portfolio.getInvestment()-(perunitprice*units));
            portfolio.setSoldAmount(latestTotalPrice);
            portfolio.setSoldUnit(units);

        }else if(portfolioAddRequestDto.getTranscationType().equalsIgnoreCase("SECONDARY_BUY")){
            portfolio.setTotalUnits(portfolio.getTotalUnits()+units);
            portfolio.setCurrentValue(portfolio.getCurrentValue()+latestTotalPrice);
            portfolio.setInvestment(portfolio.getInvestment()+(perunitprice*units));
            portfolio.setSoldUnit(portfolio.getSoldUnit());
            portfolio.setSoldAmount(portfolio.getSoldAmount());

        }

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

        portfolio.setTotalReceivableAmount(portfolio.getCurrentValue());
        portfolio.setUserId(portfolioAddRequestDto.getUserId());
        portfolio.setTranscationDate(portfolioAddRequestDto.getTranscationDate());
        portfolio.setTranscationType(portfolioAddRequestDto.getTranscationType());
        if (currentIndex>=0){
            System.out.println("currentn index greater than 0");
            portfolioSummaryUser.getAllPortfolio().set(currentIndex, portfolio);
        }else {

            System.out.println("currentn index less than 0");
            portfolioSummaryUser.getAllPortfolio().add(portfolio);

        }
        return this.save(addToPortfolioSummary(portfolioSummaryUser.getAllPortfolio(),false,portfolioSummaryUser));
    }


    private PortfolioSummary addToPortfolioSummary(List<Portfolio> allPortfolio,Boolean isDelete,PortfolioSummary portfolioSummary){
//        PortfolioSummary portfolioSummary = null;
//        if(isDelete){
//            portfolioSummary  = portfolioS;
//        }else {
//            portfolioSummary = new PortfolioSummary();
//        }
        double totalUnits = 0, currentValue = 0, totalInvestment = 0, totalReceivableAmount = 0, overallProfit = 0, todaysProfit = 0;
        for (int i = 0; i < allPortfolio.size(); i++) {
            totalUnits = totalUnits + allPortfolio.get(i).getTotalUnits();
            currentValue = currentValue + allPortfolio.get(i).getCurrentValue();
            totalInvestment = totalInvestment + allPortfolio.get(i).getInvestment();
            totalReceivableAmount = totalReceivableAmount + allPortfolio.get(i).getTotalReceivableAmount();
            overallProfit = overallProfit + allPortfolio.get(i).getOverallProfit() + allPortfolio.get(i).getOverallLoss();
            todaysProfit = todaysProfit + allPortfolio.get(i).getTodaysProfit() + allPortfolio.get(i).getTodaysLoss();

        }
        if(allPortfolio.size()==0) {
            portfolioSummary.setUserId(null);
        }else{
            portfolioSummary.setUserId(allPortfolio.get(0).getUserId());
        }
        portfolioSummary.setTotalUnits(totalUnits);
        portfolioSummary.setCurrentValue(currentValue);
        portfolioSummary.setTotalReceivableAmount(totalReceivableAmount);
        portfolioSummary.setOverallProfit(overallProfit);
        portfolioSummary.setTodaysProfit(todaysProfit);
        portfolioSummary.setTotalInvestment(totalInvestment);
        if(!isDelete) portfolioSummary.setAllPortfolio(allPortfolio);

        return portfolioSummary;
        }


}
