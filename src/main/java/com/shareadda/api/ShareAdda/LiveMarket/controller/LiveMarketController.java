package com.shareadda.api.ShareAdda.LiveMarket.controller;


import com.shareadda.api.ShareAdda.LiveMarket.domain.*;
import com.shareadda.api.ShareAdda.LiveMarket.domain.MarketSummary;
import com.shareadda.api.ShareAdda.LiveMarket.domain.dto.*;
import com.shareadda.api.ShareAdda.LiveMarket.repository.*;
import com.shareadda.api.ShareAdda.LiveMarket.service.ScrappingService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/scrape-data")
public class LiveMarketController {
    @Autowired
    private CompanyAndSymbol companyAndSymbol;

    @Autowired
    private ScrappingService scrappingService;

    @Autowired
    private ListedCompanyRepository listedCompanyRepository;

    @Autowired
    private FloorSheetRepository floorSheetRepository;

    @Autowired
    private IndiciesSubindiciesRepository indicieSubindiciesRepository;

    @Autowired
    private com.shareadda.api.ShareAdda.LiveMarket.repository.MarketSummary marketSummaryRepository;

    @Autowired
    private TopLoosersRepository topLoosersRepository;

    @Autowired
    private TopGainersRepository topGainersRepository;

    @Autowired
    private TopShareTradedRepository topShareTradedRepository;

    @Autowired
    private TopTurnOverRepository topTurnOverRepository;

    @Autowired
    private TopTranscationsRepository topTranscationsRepository;

    @Autowired
    private LiveMarketRepository liveMarketRepository;

    @Autowired
    private BrokerRepository brokerRepository;


    private boolean getMarketStatus(){
//        if(LocalTime.now().getHour()>=15 || LocalTime.now().getHour()<11) {
//            return true;
//        }
        JSONObject jsonobj = scrappingService.getMarketStatus();
        if(jsonobj.get("isOpen").equals("OPEN")) return true;
        return false;
    }

    @GetMapping("/todaysprice")
    public ResponseEntity<?> getTodayStockPrice() throws IOException {
        System.out.println(LocalTime.now().getHour());

        if(!getMarketStatus()) {
            LiveMarketDto liveMarketDto = new LiveMarketDto();
            liveMarketDto.setIsMarketOpen(false);
            liveMarketDto.setResults(liveMarketRepository.findAll());
            liveMarketDto.setDate(LocalDate.now()+" 15:00:00");
            return new ResponseEntity<>(liveMarketDto,HttpStatus.OK);
        }
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
        if(!getMarketStatus() && stockSymbol.equalsIgnoreCase("all")) {
            FloorSheetDto floorSheetDto = new FloorSheetDto();
            floorSheetDto.setDate(LocalDate.now()+" 15:00:00");
            floorSheetDto.setResults(floorSheetRepository.findAll());
            return new ResponseEntity<>(floorSheetDto, HttpStatus.OK);
        }
        if(!getMarketStatus() && !stockSymbol.equalsIgnoreCase("all")){
            FloorSheetDto floorSheetDto = new FloorSheetDto();
            floorSheetDto.setDate(LocalDate.now()+" 15:00:00");
            String finalStockSymbol = stockSymbol;
            List<FloorSheet> floorSheets = floorSheetRepository.findAll().stream().filter(floor->floor.getSymbol().equalsIgnoreCase(finalStockSymbol)).collect(Collectors.toList());
            floorSheetDto.setResults(floorSheets);
            return new ResponseEntity<>(floorSheetDto, HttpStatus.OK);
        }
        if (stockSymbol.equalsIgnoreCase("all"))
            stockSymbol = null;
        return new ResponseEntity<>(scrappingService.getFloorSheet(stockSymbol), HttpStatus.OK);
    }

    @GetMapping("/indicies-subindicies")
    public ResponseEntity<IndiciesSubIndiciesDto> getIndiciesSubindicies() throws IOException {
        if(!getMarketStatus()){
            IndiciesSubIndiciesDto indiciesdto = new IndiciesSubIndiciesDto();
            indiciesdto.setDate(LocalDate.now()+" 15:00:00");
            indiciesdto.setIsMarketOpen(false);
            indiciesdto.setResults(indicieSubindiciesRepository.findAll());
            return new ResponseEntity<>(indiciesdto, HttpStatus.OK);
        }
        return new ResponseEntity<>(scrappingService.indiciesSubindicies(), HttpStatus.OK);
    }

    @GetMapping("/market-summary")
    public ResponseEntity<MarketSummary> getMarketSumary() throws IOException {
        if (!getMarketStatus()) {
            return new ResponseEntity<>(marketSummaryRepository.findAll().get(0), HttpStatus.OK);
        }
        return new ResponseEntity<>(scrappingService.getMarketSummary(), HttpStatus.OK);
    }

    @GetMapping("/get-top-loosers")
    public ResponseEntity<TopLoosersDto> getTopLoosers() throws IOException{
        if (!getMarketStatus()) {
            TopLoosersDto topLoosersDto = new TopLoosersDto();
            topLoosersDto.setDate(LocalDate.now()+" 15:00:00");
            topLoosersDto.setResults(topLoosersRepository.findAll());
            return new ResponseEntity<>(topLoosersDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(scrappingService.getTopLoosers(), HttpStatus.OK);
    }

    @GetMapping("/get-top-gainers")
    public ResponseEntity<TopGainersDto> getTopGainers() throws IOException{
        if (!getMarketStatus()) {
            TopGainersDto topGainersDto = new TopGainersDto();
            topGainersDto.setDate(LocalDate.now()+" 15:00:00");
            topGainersDto.setResults(topGainersRepository.findAll());
            return new ResponseEntity<>(topGainersDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(scrappingService.getTopGainer(), HttpStatus.OK);

    }

    @GetMapping("/get-all-brokers")
    public ResponseEntity<List<Brokers>> getAllBrokers() throws IOException{

        return new ResponseEntity<>(brokerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/get-all-companies")
    public ResponseEntity<List<ListedCompaniesDto>> getAllListedCompanies() throws IOException{
        List<ListedCompaniesDto> listedCompaniesDtos = listedCompanyRepository.findAll().stream().map(l->{
            ListedCompaniesDto listedCompaniesDto = new ListedCompaniesDto();
            listedCompaniesDto.setCompanyNo(l.getCompanyNo());
            listedCompaniesDto.setCompanyType(l.getCompanyType());
            listedCompaniesDto.setName(l.getName());
            listedCompaniesDto.setSymbol(l.getCompanyCode());
            return listedCompaniesDto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(listedCompaniesDtos, HttpStatus.OK);
    }

    @GetMapping("/chart/market/{no}/{timeduration}/")
    public ResponseEntity<Map<String,List<?>>> getChartOfMarket(@PathVariable int no, @PathVariable char timeduration) throws IOException, java.text.ParseException {
        return new ResponseEntity<>(scrappingService.getChartData(no,timeduration), HttpStatus.OK);
    }

    @GetMapping("/chart/market/company/{no}/{timeduration}/")
    public ResponseEntity<Map<String,List<?>>> getChartofCompanyDataNewWeb(@PathVariable int no, @PathVariable char timeduration) throws IOException, java.text.ParseException {
        return new ResponseEntity<>(scrappingService.getChartofCompanyData(no,timeduration), HttpStatus.OK);
    }

    @GetMapping("/get-top-turnover")
    public ResponseEntity<TopTurnoverDto> getTopTurnOver() throws IOException{
        if (!getMarketStatus()) {
            TopTurnoverDto topTurnoverDto = new TopTurnoverDto();
            topTurnoverDto.setDate(LocalDate.now()+" 15:00:00");
            topTurnoverDto.setResults(topTurnOverRepository.findAll());
            return new ResponseEntity<>(topTurnoverDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(scrappingService.getTopTurnOver(), HttpStatus.OK);
    }

    @GetMapping("/get-top-sharetraded")
    public ResponseEntity<TopShareTradedDto> getTopShareTraded() throws IOException{
        if (!getMarketStatus()) {
            TopShareTradedDto topShareTradedDto = new TopShareTradedDto();
            topShareTradedDto.setDate(LocalDate.now()+" 15:00:00");
            topShareTradedDto.setResults(topShareTradedRepository.findAll());
            return new ResponseEntity<>(topShareTradedDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(scrappingService.getTopShareTraded(), HttpStatus.OK);
    }

    @GetMapping("/get-top-transcations")
    public ResponseEntity<TopTranscationsDto> getTopTranscations() throws IOException{
        if (!getMarketStatus()) {
            TopTranscationsDto topTranscationsDto = new TopTranscationsDto();
            topTranscationsDto.setDate(LocalDate.now()+" 15:00:00");
            topTranscationsDto.setResults(topTranscationsRepository.findAll());
            return new ResponseEntity<>(topTranscationsDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(scrappingService.getTopTranscations(), HttpStatus.OK);
    }

    @GetMapping("/company-details/{no}")
    public ResponseEntity<CompanyDetails> getCompanyDetails(@PathVariable String no) throws IOException {
        return new ResponseEntity<>(scrappingService.getCompanyDetails(no),HttpStatus.OK);
    }

    @GetMapping("/market-depth/{symbol}")
    public ResponseEntity<?> getMarketDepth(@PathVariable String symbol) throws IOException {
        if (!getMarketStatus()) {
            Map<String, Boolean> maps = new HashMap<>();
            maps.put("isMarketOpen",false);
            return new ResponseEntity<>(maps, HttpStatus.OK);
        }
        return new ResponseEntity<>(scrappingService.getMarketDepth(symbol),HttpStatus.OK);
    }
    /*
    //scrapping left to do
    TODO:Scrapping of news websie of nepalipaisa,sharesansar,bizmandu
    TODO: Scrapping of company Details
    TODO:


    */
}
