package com.shareadda.api.ShareAdda.LiveMarket.controller;


import com.shareadda.api.ShareAdda.LiveMarket.domain.*;
import com.shareadda.api.ShareAdda.LiveMarket.domain.dto.*;
import com.shareadda.api.ShareAdda.LiveMarket.repository.CompanyAndSymbol;
import com.shareadda.api.ShareAdda.LiveMarket.repository.ListedCompanyRepository;
import com.shareadda.api.ShareAdda.LiveMarket.service.ScrappingService;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/scrape-data")
public class LiveMarketController {
    @Autowired
    private CompanyAndSymbol companyAndSymbol;

    @Autowired
    private ScrappingService scrappingService;

    @Autowired
    private ListedCompanyRepository listedCompanyRepository;

    @GetMapping("/todaysprice")
    public ResponseEntity<List<LiveMarket>> getTodayStockPrice() throws IOException {
        return new ResponseEntity<>(scrappingService.scrapeLiveMarket(), HttpStatus.OK);

    }
    @GetMapping("/news/{newsSource}")
    public JSONArray getNews(@PathVariable String newsSource) throws ParseException {
        return scrappingService.newsList(newsSource);
    }

    @GetMapping("/financialReport/{symbol}")
    public ResponseEntity<List<FinancialReport>> getFinancialReport(@PathVariable String symbol) throws IOException {
        Integer company_no = Integer.parseInt(companyAndSymbol.findBySymbol(symbol).getNumber());
        return new ResponseEntity<>(scrappingService.getFinancialReport(company_no), HttpStatus.OK);
    }

    @GetMapping("/floorsheet/{stockSymbol}")
    public ResponseEntity<FloorSheetDto> getFloorSheet(@PathVariable String stockSymbol) throws IOException {
        if (stockSymbol.equalsIgnoreCase("all"))
                stockSymbol = null;
        return new ResponseEntity<>(scrappingService.getFloorSheet(stockSymbol), HttpStatus.OK);
    }

    @GetMapping("/indicies-subindicies")
    public ResponseEntity<IndiciesSubIndiciesDto> getIndiciesSubindicies() throws IOException {
        return new ResponseEntity<>(scrappingService.indiciesSubindicies(), HttpStatus.OK);
    }

    @GetMapping("/market-summary")
    public ResponseEntity<MarketSummary> getMarketSumary() throws IOException {
        return new ResponseEntity<>(scrappingService.getMarketSummary(), HttpStatus.OK);
    }

    @GetMapping("/get-top-loosers")
    public ResponseEntity<TopLoosersDto> getTopLoosers() throws IOException{
        return new ResponseEntity<>(scrappingService.getTopLoosers(), HttpStatus.OK);
    }

    @GetMapping("/get-top-gainers")
    public ResponseEntity<TopGainersDto> getTopGainers() throws IOException{
        return new ResponseEntity<>(scrappingService.getTopGainer(), HttpStatus.OK);

    }

    @GetMapping("/get-all-brokers")
    public ResponseEntity<List<Brokers>> getAllBrokers() throws IOException{
        return new ResponseEntity<>(scrappingService.getAllBrokers(), HttpStatus.OK);
    }

    @GetMapping("/get-all-companies")
    public ResponseEntity<List<ListedCompanies>> getAllListedCompanies() throws IOException{
        return new ResponseEntity<>(listedCompanyRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/chart/market/{no}/{timeduration}/")
    public ResponseEntity<Map<String,List<?>>> getChartOfMarket(@PathVariable int no, @PathVariable char timeduration) throws IOException{
        return new ResponseEntity<>(scrappingService.getChartData(no,timeduration), HttpStatus.OK);
    }

    @GetMapping("/chart/market/company/{no}/{timeduration}/")
    public ResponseEntity<Map<String,List<?>>> getChartOfCompany(@PathVariable int no, @PathVariable char timeduration) throws IOException{
        return new ResponseEntity<>(scrappingService.getChartofCompanyData(no,timeduration), HttpStatus.OK);
    }

    @GetMapping("/get-top-turnover")
    public ResponseEntity<TopTurnoverDto> getTopTurnOver() throws IOException{
        return new ResponseEntity<>(scrappingService.getTopTurnOver(), HttpStatus.OK);
    }

    @GetMapping("/get-top-sharetraded")
    public ResponseEntity<TopShareTradedDto> getTopShareTraded() throws IOException{
        return new ResponseEntity<>(scrappingService.getTopShareTraded(), HttpStatus.OK);
    }

    @GetMapping("/get-top-transcations")
    public ResponseEntity<TopTranscationsDto> getTopTranscations() throws IOException{
        return new ResponseEntity<>(scrappingService.getTopTranscations(), HttpStatus.OK);
    }

    @GetMapping("/company-details/{no}")
    public ResponseEntity<CompanyDetails> getCompanyDetails(@PathVariable String no) throws IOException {
        return new ResponseEntity<>(scrappingService.getCompanyDetails(no),HttpStatus.OK);
    }
    /*
    //scrapping left to do
    TODO:Scrapping of news websie of nepalipaisa,sharesansar,bizmandu
    TODO: Scrapping of company Details
    TODO:


    */
}
