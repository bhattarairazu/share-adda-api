package com.shareadda.api.ShareAdda.LiveMarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.shareadda.api.ShareAdda.LiveMarket.domain.*;
import com.shareadda.api.ShareAdda.LiveMarket.domain.dto.*;
import com.shareadda.api.ShareAdda.LiveMarket.repository.CompanyAndSymbol;
import com.shareadda.api.ShareAdda.LiveMarket.repository.ListedCompanyRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScrappingService {
    @Autowired
    private CompanyAndSymbol companyAndSymbolRepository;

    @Autowired
    private ListedCompanyRepository listedCompanyRepository;

    @Autowired
    private RestTemplate restTemplate;

    public JSONArray newsList(String source) throws ParseException {
        if(source.equalsIgnoreCase("merolagani")){
            return getMeroLagani();
        }
        return null;
    }

    private JSONArray getMeroLagani() {
        String url = "https://merolagani.com/handlers/webrequesthandler.ashx?type=get_news&newsID=0&newsCategoryID=0&symbol=&page=1&pageSize=200&popular=false&includeFeatured=true&news=%23ctl00_ContentPlaceHolder1_txtNews&languageType=NP";

        JSONParser parser = new JSONParser();
        JSONArray arr = null;

        try {
            URL oracle = new URL(url); // URL to Parse
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                arr= (JSONArray) parser.parse(inputLine);
                for(Object o :arr){
                    JSONObject jsonObject = (JSONObject)o;
                    String image = jsonObject.get("imagePath").toString();
                    jsonObject.put("imagePath","https://images.merolagani.com/"+image);
                    jsonObject.put("newsUrl","https://merolagani.com/NewsDetail.aspx?newsID="+jsonObject.get("newsID").toString());
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public List<FinancialReport> getFinancialReport(int no) throws IOException{
        List<FinancialReport> financialReportList = new ArrayList<>();
        String url = "http://www.nepalstock.com/company/display/"+no;
        Document doc = Jsoup.connect(url).get();
        Element table = doc.getElementsByClass("my-table table").get(1);
        List<Element> trs = table.select("tr").stream().skip(2).collect(Collectors.toList());
        for(int i = 0;i<trs.size();i++){
            Elements tds = trs.get(i).select("td");
            FinancialReport financialReport = new FinancialReport();
            financialReport.setTitle(tds.get(0).select("h5").text());
            financialReport.setReportType(tds.get(1).text());
            financialReport.setDate(tds.get(2).text());
            financialReport.setFileurl(tds.get(3).select("a").attr("href"));
            financialReport.setCode(String.valueOf(no));
            financialReportList.add(financialReport);
        }
        return financialReportList;

    }


    public FloorSheetDto getFloorSheet(String stocksymbol) throws IOException{
        String url = null;
        if(stocksymbol==null) {
            url = "http://www.nepalstock.com/main/floorsheet?_limit=5000";
        }else{
            url = "http://www.nepalstock.com/main/floorsheet?_limit=5000&stock-symbol=" + stocksymbol;
        }
        List<FloorSheet> floorSheetList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("table my-table");
        String []date = doc.getElementsByClass("col-xs-2 col-md-2 col-sm-0").get(0).text().split(" ");
        //String newdate = date.substring(5,15);
        String newDate = date[2]+" "+date[3];
        System.out.println(newDate);

        Elements trs = ele.select("tr");
        for(Element tr:trs.subList(2,5001)){
            Elements tds = tr.select("td");
            FloorSheet floorSheet = new FloorSheet();
            //floorSheet.setDate(newDate);
            floorSheet.setContractNo(tds.get(1).text());
            floorSheet.setSymbol(tds.get(2).text());
            floorSheet.setBuyerBroker(tds.get(3).text());
            floorSheet.setSellarBroker(tds.get(4).text());
            floorSheet.setQuantity(tds.get(5).text());
            floorSheet.setRate(tds.get(6).text());
            floorSheet.setAmount(tds.get(7).text());
            floorSheetList.add(floorSheet);
        }
        FloorSheetDto floorSheetDto = new FloorSheetDto();
        floorSheetDto.setDate(newDate);
        floorSheetDto.setResults(floorSheetList);
        return floorSheetDto;

    }
    public LiveMarketDto scrapeLiveMarket() throws IOException {
        List<LiveMarket> liveMarketList = new ArrayList<>();
        String url = "http://www.nepalstock.com/stocklive";
        Document doc = Jsoup.connect(url).get();
        Element table = doc.getElementsByClass("table table-condensed").get(0);
        Element datecol = doc.getElementsByClass("col-xs-12 col-md-6 col-sm-6 panel panel-default").get(0);
        String[] getindiv = datecol.getElementsByClass("panel-heading").text().split(" ");
        String newDate = getindiv[2]+" "+getindiv[3];
        Elements rows = table.select("tr");
        for(int i = 1;i<rows.size();i++){
            Elements tds = rows.get(i).select("td");
            LiveMarket liveMarket = new LiveMarket();
            liveMarket.setSymbol(tds.get(1).text());
            liveMarket.setLtp(tds.get(2).text());
            liveMarket.setLtv(tds.get(3).text());
            liveMarket.setPointchange(tds.get(4).text());
            liveMarket.setPercentchange(tds.get(5).text());
            liveMarket.setOpen(tds.get(6).text());
            liveMarket.setHigh(tds.get(7).text());
            liveMarket.setLow(tds.get(8).text());
            liveMarket.setVolume(tds.get(9).text());
            liveMarket.setPreviousclosing(tds.get(10).text());
            liveMarketList.add(liveMarket);
        }
        LiveMarketDto liveMarketDto = new LiveMarketDto();
        liveMarketDto.setDate(newDate);
        liveMarketDto.setResults(liveMarketList);

        //String url = "http://www.nepalstock.com/todaysprice?_limit=500";
//        Document doc = Jsoup.connect(url).get();
//        Element table = doc.getElementsByClass("table table-condensed table-hover").get(0);
//        Elements rows = table.select("tr");
//        Map<String, Element> innerMap = null;
//        for(Element rr : rows.subList(2,220)){
//            LiveMarket liveMarket = new LiveMarket();
//
//            Elements tds = rr.select("td");
//            //outerMap.put(tds.get(0))
//            liveMarket.setCompanyName(tds.get(1).text());
//            liveMarket.setNoOfTransactions(tds.get(2).text());
//            liveMarket.setMaxPrice(tds.get(3).text());
//            liveMarket.setMinPrice(tds.get(4).text());
//            liveMarket.setClosePrice(tds.get(5).text());
//            liveMarket.setTradedShare(tds.get(6).text());
//            liveMarket.setAmount(tds.get(7).text());
//            liveMarket.setPreviousClosing(tds.get(8).text());
//            liveMarket.setDifference(tds.get(9).text());
//            liveMarketList.add(liveMarket);
//
//        }


        return liveMarketDto;
    }

    public IndiciesSubIndiciesDto indiciesSubindicies() throws IOException {
        List<IndiciesSubindicies> indiciesSubindiciesList = new ArrayList<>();
        String url = "http://www.nepalstock.com";
        Document doc = Jsoup.connect(url).get();
        String []date = doc.getElementsByClass("col-xs-12 col-md-12 col-sm-12 panel panel-default").get(1).getElementsByClass("panel-heading").text().split(" ");
        //String newdate = date.substring(5,15);
        String newDate = date[2];
        String time = date[3];
        String allDate = newDate+" " + time;
        System.out.println(allDate);
        //System.out.println(newDate+" "+time);

        List<Element> tables = doc.getElementsByClass("table table-hover table-condensed").stream().skip(4).collect(Collectors.toList());
        for(int i = 0;i<tables.size();i++){
            Elements trow = tables.get(i).select("tr");
            if(i==0) {
                for (Element tdata : trow.subList(1, 4)) {
                    indiciesSubindiciesList.add(extractindiciessubindicies(tdata,allDate));

                }
            }else{
                for (Element tdata : trow.subList(1, 14)) {
                    indiciesSubindiciesList.add(extractindiciessubindicies(tdata,allDate));

                }
            }

        }
        IndiciesSubIndiciesDto indiciesSubIndiciesDto = new IndiciesSubIndiciesDto();
        indiciesSubIndiciesDto.setDate(allDate);
        indiciesSubIndiciesDto.setResults(indiciesSubindiciesList);

        return indiciesSubIndiciesDto;


    }

    private IndiciesSubindicies extractindiciessubindicies(Element tdata,String allDate) {
        Elements tdatas = tdata.select("td");
        IndiciesSubindicies indiciesSubindicies = new IndiciesSubindicies();
        indiciesSubindicies.setIndicesName(tdatas.get(0).text());
        indiciesSubindicies.setCurrentPrice(tdatas.get(1).text());
        //getting plus minus sign
        String[] plusminus = tdatas.get(4).select("img").attr("src").split("/");
        String pm = plusminus[plusminus.length-1];
        String sign = null;
        if(pm.equalsIgnoreCase("decrease.gif")){
            indiciesSubindicies.setPointsChange("-"+tdatas.get(2).text());
            indiciesSubindicies.setPercentChange("-"+tdatas.get(3).text());
        }else{
            indiciesSubindicies.setPointsChange(tdatas.get(2).text());
            indiciesSubindicies.setPercentChange(tdatas.get(3).text());
        }
        //indiciesSubindicies.setDate(allDate);

        return indiciesSubindicies;
    }

    public MarketSummary getMarketSummary() throws IOException {
        MarketSummary marketSummary = new MarketSummary();
        Map<String,String> newMap = new HashMap<>();
        String url = "http://www.nepalstock.com/";
        String []headingarr = {"date","totalTurnover","totalTradedShare","totalTranscations","totalScriptTraded","totalMarketCapitalization","floatedMarketCapitalization"};
        Document table = Jsoup.connect(url).get();
        String []date = table.getElementsByClass("col-xs-12 col-md-12 col-sm-12 panel panel-default").get(1).getElementsByClass("panel-heading").text().split(" ");
        //String newdate = date.substring(5,15);
        String newDate = date[2];
        String time = date[3];
        String allDate = newDate+" " + time;
        newMap.put(headingarr[0],allDate);
        Element element = table.getElementsByClass("table table-hover table-condensed").get(3);
        List<Element> trs = element.select("tr").subList(1,7);
        for(int i = 0;i<trs.size();i++){
            Elements td=trs.get(i).select("td");
            newMap.put(headingarr[i+1],td.get(1).text());
        }
        ObjectMapper mapper = new ObjectMapper();
        marketSummary = mapper.convertValue(newMap,MarketSummary.class);

        return marketSummary;
    }
    public TopLoosersDto getTopLoosers() throws IOException {
        List<TopLoosers> topLoosersList = new ArrayList<>();
        String url = "http://www.nepalstock.com/losers";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByClass("dataTable table");
        String[] date = table.select("tr").get(1).text().split(" ");
        String newDate = date[2]+" "+date[3];
        System.out.println("main date "+(date[2]+" "+date[3]));
        List<Element> tr = table.select("tr").stream().skip(2).collect(Collectors.toList());
        //System.out.println(tr);
        for(int i =0;i<tr.size()-1;i++){
            TopLoosers topLoosers = new TopLoosers();
            Elements tds = tr.get(i).select("td");
            topLoosers.setSymbol(tds.get(0).text());
            topLoosers.setLtp(tds.get(1).text());
            topLoosers.setPercnetchange(tds.get(2).text());
            //topLoosers.setDate(newDate);
            topLoosersList.add(topLoosers);

            //System.out.println(topLoosersList);

        }
        TopLoosersDto topLoosersDto = new TopLoosersDto();
        topLoosersDto.setDate(newDate);
        topLoosersDto.setResults(topLoosersList);
        return topLoosersDto;

    }
    public TopGainersDto getTopGainer() throws IOException {
        List<TopGainers> topgainerslist = new ArrayList<>();
        String url = "http://www.nepalstock.com/gainers";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByClass("dataTable table");
        String[] date = table.select("tr").get(1).text().split(" ");
        String newDate = date[2]+" "+date[3];
        System.out.println("main date "+(date[2]+" "+date[3]));
        List<Element> tr = table.select("tr").stream().skip(2).collect(Collectors.toList());
        System.out.println(tr);
        for(int i =0;i<tr.size()-1;i++){
            TopGainers topgainers = new TopGainers();

            Elements tds = tr.get(i).select("td");
            topgainers.setSymbol(tds.get(0).text());
            topgainers.setLtp(tds.get(1).text());
            topgainers.setPercnetchange(tds.get(2).text());
            //topgainers.setDate(newDate);
            topgainerslist.add(topgainers);
            //System.out.println(tds.get(0).text());
            //System.out.println(topLoosersList);


        }
        TopGainersDto topGainersDto = new TopGainersDto();
        topGainersDto.setDate(newDate);
        topGainersDto.setResults(topgainerslist);
        return topGainersDto;

    }
    public List<Brokers> getAllBrokers() throws IOException {
        List<Brokers> brokersList = new ArrayList<>();
        String url = "https://merolagani.com/BrokerList.aspx";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByClass("table table-striped table-hover");
        List<Element> trs = table.select("tr").stream().skip(1).collect(Collectors.toList());
        for(int i = 0;i<trs.size();i++){
            Elements tds = trs.get(i).select("td");
            System.out.println(tds);
            Brokers brokers = new Brokers();
            brokers.setCode(tds.get(0).select("a").text());
            brokers.setName(tds.get(1).text());
            brokers.setLandline(tds.get(2).text());
            brokers.setAddress(tds.get(3).text());
            brokersList.add(brokers);

        }
        System.out.println(brokersList);
        return brokersList;
    }
    public List<ListedCompanies> getAllListedCompanies() throws IOException {
        List<ListedCompanies> listedCompaniesList = new ArrayList<>();
        String url = "http://www.nepalstock.com/company?_limit=500";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByClass("my-table table");
        List<Element> trs = table.select("tr").stream().skip(2).collect(Collectors.toList());
        for(int i = 0;i<trs.size()-1;i++){
            Elements tds = trs.get(i).select("td");
            ListedCompanies listedCompanies = new ListedCompanies();
            listedCompanies.setSymbol(tds.get(3).text());
            listedCompanies.setName(tds.get(2).text());
            listedCompanies.setCompanyType(tds.get(4).text());
            listedCompaniesList.add(listedCompanies);
        }
        List<CompanyWithSymbolNumber> listcompanysymbolno = getSymbolNumber();
        for(int j = 0;j<listcompanysymbolno.size();j++){
            for (int i = 0;i<listedCompaniesList.size();i++){
                if(listcompanysymbolno.get(j).getSymbol().equalsIgnoreCase(listedCompaniesList.get(i).getSymbol())){
                    listedCompaniesList.get(i).setCompanyNo(listcompanysymbolno.get(j).getNumber());
                    break;
                }
            }
        }

        return listedCompanyRepository.saveAll(listedCompaniesList);


    }
    public CompanyDetails getCompanyDetails(String symbol) throws IOException {
        CompanyDetails companyDetails = new CompanyDetails();
        String url = "https://merolagani.com/CompanyDetail.aspx?symbol="+symbol;
        Map<String,String> newMap = new HashMap<>();
        String []companydetailsheading = {"sector","shareOutStanding","marketPrice","percentChange","lastTradedOn","fiftyTwoWeeksHighLow","hunderEightyDayAverage","hunderdTwentyDayAverage","oneYearYield","eps","peRatio","bookValue","pbv"};

        Document doc = Jsoup.connect(url).get();
        Elements tables = doc.getElementsByClass("table table-striped table-hover table-zeromargin");
        Elements tr = tables.select("tbody").select("tr");
        List<Element> trs = tables.select("tbody").select("tr");
        System.out.println(trs.size());
        int j = 0;
        for(int i = 0;i<13;i++){
                Elements td = trs.get(i).select("td");
                newMap.put(companydetailsheading[i], td.get(0).text());

        }
        String []addedName = {"symbol","companyName","listedShares","paidupValue","totalPaidupvalue"};
        int k = 0;
        for(int m = trs.size()-6;m<=trs.size()-1;m++) {
            if (m == trs.size()-4) continue;
            Elements td = trs.get(m).select("td");
            newMap.put(addedName[k], td.get(0).text());
            k++;

        }

        String companyNo = companyAndSymbolRepository.findBySymbol(newMap.get("symbol")).getNumber();
        newMap.put("companyNo",companyNo);
        ObjectMapper mapper = new ObjectMapper();
        companyDetails = mapper.convertValue(newMap,CompanyDetails.class);
        // System.out.println(tables);
        return companyDetails;
    }
    public Map<String,List<?>> getChartData(int no,char type) throws IOException {
        /*
         *Here M- Represet type which is Month
         * D - Day
         * Q - Quarter
         * W- week
         * Y - Year
         *
         * 58- Nepse
         * 57 -Sensitive
         * 62 - Float
         * 63 - Sensitive Float
         * 51 - Banking
         * 52 - Hotel
         * 54-Hydropower
         * 55- Development Bank
         * 56 - Manufacture and processing
         * 64 - Microfinance
         * 65 - Life Insurance
         * 59 - Non life Insurance
         * 60- finance
         * 61- Trading
         * 53- Others
         * 66 - Mutual Fund
         */
        String url= "http://www.nepalstock.com/graphdata/"+no+"/"+type;

        //JSONArray ss = restTemplate.getForObject(url, JSONArray.class);
        String ss = restTemplate.getForObject(url, String.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonArray jsonArray = (JsonArray) jp.parse(ss);
//        {
//            time:["2019-11-11 10:00","2019-11-11 10:00"],
//            data:[3000,4000]
//        }
        Map<String,List<?>> chartMap = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Double> datas = new ArrayList<>();
        System.out.println(jsonArray.size());
        for(int i = 0;i<jsonArray.size();i++){
            JsonArray innerarray = (JsonArray) jp.parse(String.valueOf(jsonArray.get(i)));
            dates.add(getDate(Long.parseLong(String.valueOf(innerarray.get(0)))));
            datas.add(Double.parseDouble(String.valueOf(innerarray.get(1))));

        }

        chartMap.put("time", dates);
        chartMap.put("data", datas);
        //System.out.println(dates);
        //System.out.println(datas);
        return chartMap;

    }
    private String getDate(long time){
        Timestamp timestamp = new Timestamp(time);
        Date date = timestamp;
        return String.valueOf(date);
    }
    public Map<String,List<?>> getChartofCompanyData(int no,char type) throws IOException {
        /*
         *Here M- Represet type which is Month
         * D - Day
         * Q - Quarter
         * W- week
         * Y - Year
         *
         * 58- Nepse
         * 57 -Sensitive
         * 62 - Float
         * 63 - Sensitive Float
         * 51 - Banking
         * 52 - Hotel
         * 54-Hydropower
         * 55- Development Bank
         * 56 - Manufacture and processing
         * 64 - Microfinance
         * 65 - Life Insurance
         * 59 - Non life Insurance
         * 60- finance
         * 61- Trading
         * 53- Others
         * 66 - Mutual Fund
         */
        String url= "http://www.nepalstock.com/company/graphdata/"+no+"/"+type;

        String ss = restTemplate.getForObject(url, String.class);
        //JSONArray ss = restTemplate.getForObject(url, JSONArray.class);
        JsonParser jp = new JsonParser();
        JsonArray jsonArray = (JsonArray) jp.parse(ss);
//        {
//            time:["2019-11-11 10:00","2019-11-11 10:00"],
//            data:[3000,4000]
//        }
        Map<String,List<?>> chartMap = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Double> datas = new ArrayList<>();
        System.out.println(jsonArray.size());
        for(int i = 0;i<jsonArray.size();i++){
            JsonArray innerarray = (JsonArray) jp.parse(String.valueOf(jsonArray.get(i)));
            dates.add(getDate(Long.parseLong(String.valueOf(innerarray.get(0)))));
            datas.add(Double.parseDouble(String.valueOf(innerarray.get(1))));

        }

        chartMap.put("time", dates);
        chartMap.put("data", datas);
        //System.out.println(dates);
        //System.out.println(datas);
        return chartMap;

    }
    public List<CompanyWithSymbolNumber> getSymbolNumber() throws IOException {
        List<CompanyWithSymbolNumber> companyWithSymbolNumberList = new ArrayList<>();
        String url = "http://www.nepalstock.com.np/company?_limit=500";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByClass("my-table table");
        List<Element> trs = table.select("tr").stream().skip(2).collect(Collectors.toList());
        for(int i = 0;i<trs.size()-1;i++){
            Elements tds = trs.get(i).select("td");
            CompanyWithSymbolNumber companyWithSymbolNumber = new CompanyWithSymbolNumber();
            companyWithSymbolNumber.setSymbol(tds.get(3).text());
            Element ees= tds.get(5).getElementsByTag("a").first();
            String urls = ees.attr("href");
            String[] getNumber = urls.split("/");
            String companyNumber = getNumber[getNumber.length-1];
            companyWithSymbolNumber.setNumber(companyNumber);
            companyWithSymbolNumberList.add(companyWithSymbolNumber);

            //already saved to database
            //no need to scrape further

        }
        return companyAndSymbolRepository.saveAll(companyWithSymbolNumberList);

    }
    public TopTurnoverDto getTopTurnOver() throws IOException {
        String url = "http://www.nepalstock.com/turnovers";
        List<TopTurnOver> topTurnOvers = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("dataTable table");

        Elements tbody = ele.select("tbody").get(1).select("tr");
        String[] date = tbody.get(1).text().split(" ");
        String newDate = date[2]+" "+date[3];
        System.out.println("main date "+(date[2]+" "+date[3]));
        for(Element td:tbody.subList(2,11)){
            Elements tds = td.select("td");
            System.out.println(tds);
            TopTurnOver topTurnOver = new TopTurnOver();
            topTurnOver.setSymbol(tds.get(0).text());
            topTurnOver.setTurnOver(tds.get(1).text());
            topTurnOver.setClosingPrice(tds.get(2).text());
            //topTurnOver.setDate(newDate);
            topTurnOvers.add(topTurnOver);
        }
        TopTurnoverDto topTurnoverDto = new TopTurnoverDto();
        topTurnoverDto.setDate(newDate);
        topTurnoverDto.setResults(topTurnOvers);
        return topTurnoverDto;

    }
    public TopShareTradedDto getTopShareTraded() throws IOException {
        String url = "http://www.nepalstock.com/turnovers";
        List<TopShareTraded> topShareTradedList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("dataTable table");
        Elements tbody = ele.select("tbody").get(3).select("tr");
        String[] date = tbody.get(1).text().split(" ");
        String newDate = date[2]+" "+date[3];
        System.out.println("main date "+(date[2]+" "+date[3]));
        for(Element td:tbody.subList(2,11)){
            Elements tds = td.select("td");
            System.out.println(tds);
            TopShareTraded topShareTraded = new TopShareTraded();
            topShareTraded.setSymbol(tds.get(0).text());
            topShareTraded.setShareTraded(tds.get(1).text());
            topShareTraded.setClosingPrice(tds.get(2).text());
            //topShareTraded.setDate(newDate);
            topShareTradedList.add(topShareTraded);
        }
        TopShareTradedDto topShareTradedDto = new TopShareTradedDto();
        topShareTradedDto.setDate(newDate);
        topShareTradedDto.setResults(topShareTradedList);
        return topShareTradedDto;

    }

    public TopTranscationsDto getTopTranscations() throws IOException {
        String url = "http://www.nepalstock.com/turnovers";
        List<TopTranscations> topTranscationsList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("dataTable table");
        Elements tbody = ele.select("tbody").get(5).select("tr");
        String[] date = tbody.get(1).text().split(" ");
        String newDate = date[2]+" "+date[3];
        System.out.println("main date "+(date[2]+" "+date[3]));
        for(Element td:tbody.subList(2,11)){
            Elements tds = td.select("td");
            System.out.println(tds);
            TopTranscations topTranscations = new TopTranscations();
            topTranscations.setSymbol(tds.get(0).text());
            topTranscations.setNoOfTranscations(tds.get(1).text());
            topTranscations.setClosingPrice(tds.get(2).text());
            //topTranscations.setDate(newDate);
            topTranscationsList.add(topTranscations);
        }
        TopTranscationsDto topTranscationsDto = new TopTranscationsDto();
        topTranscationsDto.setDate(newDate);
        topTranscationsDto.setResults(topTranscationsList);

        return topTranscationsDto;

    }
    public MarketDepthDto getMarketDepth(String symbol) throws IOException {
        String no = companyAndSymbolRepository.findBySymbol(symbol.toUpperCase()).getNumber();
        List<MarketDepth> marketDepths = new ArrayList<>();
        String url = "http://www.nepalstock.com/marketdepthofcompany/"+no;
        Document doc = Jsoup.connect(url).get();
        Elements tableorders = doc.getElementsByClass("table table-striped table-bordered orderTable");
        Elements buytrs = tableorders.get(0).select("tr");
        Elements selltrs = tableorders.get(1).select("tr");
        //System.out.println(trs);
        for(int i = 1;i<buytrs.size();i++){
            MarketDepth marketDepth = new MarketDepth();
            Elements buytd = buytrs.get(i).select("td");
            marketDepth.setBuyorders(buytd.get(0).text());
            marketDepth.setBuyqty(buytd.get(1).text());
            marketDepth.setBuyprice(buytd.get(2).text());

            Elements selltd = selltrs.get(i).select("td");
            marketDepth.setSellprice(selltd.get(0).text());
            marketDepth.setSellqty(selltd.get(1).text());
            marketDepth.setSellorders(selltd.get(2).text());
            marketDepths.add(marketDepth);


        }
        Element depthindex = doc.getElementsByClass("depthIndex").get(0);
        Element trs = depthindex.select("tr").get(0);
        Elements tds = trs.select("td");
        //for(int i = 0;i<tds.size();i++){
            MarketDepthDto marketDepthDto = new MarketDepthDto();
            marketDepthDto.setLtp(tds.get(0).select("label").text());
            Elements spans = tds.get(0).select("span");
            String[] priceandpercent = spans.get(1).text().split(" ");
            marketDepthDto.setPercentchange(priceandpercent[1]);
            //for price change need to remove &nbsp;&nbsp;
            String[] pricearry = priceandpercent[0].split("&nbsp;&nbsp;");
            //getting indicitator name if it is positive or negative
            if(priceandpercent[1].contains("-")){
                marketDepthDto.setPointschange("-"+pricearry[0]);
            }else{
                marketDepthDto.setPointschange(pricearry[0]);
            }
            marketDepthDto.setPreviousclose(tds.get(1).text().split(" ")[2]);
            marketDepthDto.setOpen(tds.get(2).text().split(" ")[1]);
            marketDepthDto.setHigh(tds.get(3).text().split(" ")[1]);
            marketDepthDto.setLow(tds.get(4).text().split(" ")[1]);
            marketDepthDto.setClose(tds.get(5).text().trim().split(" ")[1]);

           //for total quantity
            Element table = tableorders.get(2).select("tr").get(0);
            Elements tds_qnt = table.select("td");
            marketDepthDto.setBuytotalqnt(tds_qnt.get(1).text());
            marketDepthDto.setSelltotalqnt(tds_qnt.get(3).text());
            //getting date
            String[] dates = doc.getElementsByClass("col-xs-2 col-md-2 col-sm-0").get(0).text().split(" ");
            //System.out.println("date "+dates[2]+" "+dates[3].trim());
            marketDepthDto.setDate(dates[2]+" "+dates[3].trim());
            marketDepthDto.setResults(marketDepths);

            //System.out.println(spans.get(0).className());
        //}
        return marketDepthDto;
    }

}
