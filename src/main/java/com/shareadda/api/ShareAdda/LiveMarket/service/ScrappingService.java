package com.shareadda.api.ShareAdda.LiveMarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shareadda.api.ShareAdda.LiveMarket.domain.*;
import com.shareadda.api.ShareAdda.LiveMarket.repository.CompanyAndSymbol;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScrappingService {
    @Autowired
    CompanyAndSymbol companyAndSymbolRepository;

    @Autowired
    private RestTemplate restTemplate;

    public JSONArray newsList(String source) throws ParseException {
        if(source.equalsIgnoreCase("merolagani")){
            return getMeroLagani();
        }
        return null;
    }

    private JSONArray getMeroLagani() {
        String url = "https://merolagani.com/handlers/webrequesthandler.ashx?type=get_news&newsID=0&newsCategoryID=0&symbol=&page=1&pageSize=100&popular=false&includeFeatured=true&news=%23ctl00_ContentPlaceHolder1_txtNews&languageType=NP";

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


    public List<FloorSheet> getFloorSheet(String stocksymbol) throws IOException{
        String url = null;
        if(stocksymbol==null) {
            url = "http://www.nepalstock.com/main/floorsheet?_limit=50";
        }else{
            url = "http://www.nepalstock.com/main/floorsheet?_limit=5000&stock-symbol=" + stocksymbol;
        }
        List<FloorSheet> floorSheetList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("table my-table");
        String []date = doc.getElementsByClass("col-xs-2 col-md-2 col-sm-0").get(0).text().split(" ");
        //String newdate = date.substring(5,15);
        String newDate = date[2];
        System.out.println(newDate);

        Elements trs = ele.select("tr");
        for(Element tr:trs.subList(2,51)){
            Elements tds = tr.select("td");
            FloorSheet floorSheet = new FloorSheet();
            floorSheet.setDate(newDate);
            floorSheet.setContractNo(tds.get(1).text());
            floorSheet.setStockSymbol(tds.get(2).text());
            floorSheet.setBuyerBroker(tds.get(3).text());
            floorSheet.setSellarBroker(tds.get(4).text());
            floorSheet.setQuantity(tds.get(5).text());
            floorSheet.setRate(tds.get(6).text());
            floorSheet.setAmount(tds.get(7).text());
            floorSheetList.add(floorSheet);
        }
        return floorSheetList;

    }
    public List<LiveMarket> scrapeLiveMarket() throws IOException {
        List<LiveMarket> liveMarketList = new ArrayList<>();
        String url = "http://www.nepalstock.com/todaysprice?_limit=500";
        Document doc = Jsoup.connect(url).get();
        Element table = doc.getElementsByClass("table table-condensed table-hover").get(0);
        Elements rows = table.select("tr");
        Map<String, Element> innerMap = null;
        for(Element rr : rows.subList(2,220)){
            LiveMarket liveMarket = new LiveMarket();

            Elements tds = rr.select("td");
            //outerMap.put(tds.get(0))
            liveMarket.setCompanyName(tds.get(1).text());
            liveMarket.setNoOfTransactions(tds.get(2).text());
            liveMarket.setMaxPrice(tds.get(3).text());
            liveMarket.setMinPrice(tds.get(4).text());
            liveMarket.setClosePrice(tds.get(5).text());
            liveMarket.setTradedShare(tds.get(6).text());
            liveMarket.setAmount(tds.get(7).text());
            liveMarket.setPreviousClosing(tds.get(8).text());
            liveMarket.setDifference(tds.get(9).text());
            liveMarketList.add(liveMarket);

        }

        return liveMarketList;
    }

    public List<IndiciesSubindicies> indiciesSubindicies() throws IOException {
        List<IndiciesSubindicies> indiciesSubindiciesList = new ArrayList<>();
        String url = "http://www.nepalstock.com";
        Document doc = Jsoup.connect(url).get();
        List<Element> tables = doc.getElementsByClass("table table-hover table-condensed").stream().skip(4).collect(Collectors.toList());
        for(int i = 0;i<tables.size();i++){
            Elements trow = tables.get(i).select("tr");
            if(i==0) {
                for (Element tdata : trow.subList(1, 4)) {
                    indiciesSubindiciesList.add(extractindiciessubindicies(tdata));

                }
            }else{
                for (Element tdata : trow.subList(1, 14)) {
                    indiciesSubindiciesList.add(extractindiciessubindicies(tdata));

                }
            }

        }
        return indiciesSubindiciesList;


    }

    private IndiciesSubindicies extractindiciessubindicies(Element tdata) {
        Elements tdatas = tdata.select("td");
        IndiciesSubindicies indiciesSubindicies = new IndiciesSubindicies();
        indiciesSubindicies.setIndicesName(tdatas.get(0).text());
        indiciesSubindicies.setCurrentPrice(tdatas.get(1).text());
        indiciesSubindicies.setPointsChange(tdatas.get(2).text());
        indiciesSubindicies.setPercentChange(tdatas.get(3).text());
        return indiciesSubindicies;
    }

    public MarketSummary getMarketSummary() throws IOException {
        MarketSummary marketSummary = new MarketSummary();
        Map<String,String> newMap = new HashMap<>();
        String url = "http://www.nepalstock.com/";
        String []headingarr = {"totalTurnover","totalTradedShare","totalTranscations","totalScriptTraded","totalMarketCapitalization","floatedMarketCapitalization"};
        Document table = Jsoup.connect(url).get();
        Element element = table.getElementsByClass("table table-hover table-condensed").get(3);
        List<Element> trs = element.select("tr").subList(1,7);
        for(int i = 0;i<trs.size();i++){
            Elements td=trs.get(i).select("td");
            newMap.put(headingarr[i],td.get(1).text());
        }
        ObjectMapper mapper = new ObjectMapper();
        marketSummary = mapper.convertValue(newMap,MarketSummary.class);

        return marketSummary;
    }
    public List<TopLoosers> getTopLoosers() throws IOException {
        List<TopLoosers> topLoosersList = new ArrayList<>();
        String url = "http://www.nepalstock.com/losers";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByClass("dataTable table");
        List<Element> tr = table.select("tr").stream().skip(2).collect(Collectors.toList());
        //System.out.println(tr);
        for(int i =0;i<tr.size()-1;i++){
            TopLoosers topLoosers = new TopLoosers();
            Elements tds = tr.get(i).select("td");
            topLoosers.setSymbol(tds.get(0).text());
            topLoosers.setLtp(tds.get(1).text());
            topLoosers.setPercnetchange(tds.get(2).text());
            topLoosersList.add(topLoosers);
            //System.out.println(topLoosersList);

        }
        return topLoosersList;

    }
    public List<TopGainers> getTopGainer() throws IOException {
        List<TopGainers> topgainerslist = new ArrayList<>();
        String url = "http://www.nepalstock.com/gainers";
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByClass("dataTable table");
        List<Element> tr = table.select("tr").stream().skip(2).collect(Collectors.toList());
        System.out.println(tr);
        for(int i =0;i<tr.size()-1;i++){
            TopGainers topgainers = new TopGainers();

            Elements tds = tr.get(i).select("td");
            topgainers.setSymbol(tds.get(0).text());
            topgainers.setLtp(tds.get(1).text());
            topgainers.setPercnetchange(tds.get(2).text());
            topgainerslist.add(topgainers);
            //System.out.println(tds.get(0).text());
            //System.out.println(topLoosersList);


        }
        return topgainerslist;

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
            listedCompanies.setCompanyCode(tds.get(3).text());
            listedCompanies.setName(tds.get(2).text());
            listedCompanies.setCompanyType(tds.get(4).text());
            listedCompaniesList.add(listedCompanies);
        }
        return listedCompaniesList;


    }
    public CompanyDetails getCompanyDetails(String symbol) throws IOException {
        CompanyDetails companyDetails = new CompanyDetails();
        String url = "https://merolagani.com/CompanyDetail.aspx?symbol="+symbol;
        Map<String,String> newMap = new HashMap<>();
        String []companydetailsheading = {"sector","shareOutStanding","marketPrice","percentChange","lastTradedOn","fiftyTwoWeeksHighLow","hunderdTwentyDayAverage","oneYearYield","eps","peRatio","bookValue","pbv","thirtyDayAvgVolume","marketCapitalization","companySymbol","companyName","sector","listedShares","paidupValue","totalPaidupvalue"};

        Document doc = Jsoup.connect(url).get();
        Elements tables = doc.getElementsByClass("table table-striped table-hover table-zeromargin");
        Elements tr = tables.select("tbody").select("tr");
        List<Element> trs = tables.select("tbody").select("tr");
        System.out.println(trs);
        int j = 0;
        for(int i = 0;i<trs.size();i++){
            if(i==6 || (i>=13 && i<=34)) continue;
            Elements td=trs.get(i).select("td");
            newMap.put(trs.get(i).select("th").get(0).text(),td.get(0).text());
            System.out.println(newMap);
            // System.out.println(j);
            j+=1;

        }
        ObjectMapper mapper = new ObjectMapper();
        companyDetails = mapper.convertValue(newMap,CompanyDetails.class);
        // System.out.println(tables);
        return companyDetails;
    }
    public String getChartData(int no,char type) throws IOException {
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

        String ss = restTemplate.getForObject(url, String.class);
        System.out.println(ss);
        return ss;

    }
    public String getChartofCompanyData(int no,char type) throws IOException {
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
        System.out.println(ss);
        return ss;

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
    public List<TopTurnOver> getTopTurnOver() throws IOException {
        String url = "http://www.nepalstock.com/turnovers";
        List<TopTurnOver> topTurnOvers = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("dataTable table");
        Elements tbody = ele.select("tbody").get(1).select("tr");
        for(Element td:tbody.subList(2,11)){
            Elements tds = td.select("td");
            System.out.println(tds);
            TopTurnOver topTurnOver = new TopTurnOver();
            topTurnOver.setCompanySymbol(tds.get(0).text());
            topTurnOver.setTurnOver(tds.get(1).text());
            topTurnOver.setClosingPrice(tds.get(2).text());
            topTurnOvers.add(topTurnOver);
        }
        return topTurnOvers;

    }
    public List<TopShareTraded> getTopShareTraded() throws IOException {
        String url = "http://www.nepalstock.com/turnovers";
        List<TopShareTraded> topShareTradedList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("dataTable table");
        Elements tbody = ele.select("tbody").get(3).select("tr");
        for(Element td:tbody.subList(2,11)){
            Elements tds = td.select("td");
            System.out.println(tds);
            TopShareTraded topShareTraded = new TopShareTraded();
            topShareTraded.setCompanySymbol(tds.get(0).text());
            topShareTraded.setShareTraded(tds.get(1).text());
            topShareTraded.setClosingPrice(tds.get(2).text());
            topShareTradedList.add(topShareTraded);
        }
        return topShareTradedList;

    }

    public List<TopTranscations> getTopTranscations() throws IOException {
        String url = "http://www.nepalstock.com/turnovers";
        List<TopTranscations> topTranscationsList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getElementsByClass("dataTable table");
        Elements tbody = ele.select("tbody").get(5).select("tr");
        for(Element td:tbody.subList(2,11)){
            Elements tds = td.select("td");
            System.out.println(tds);
            TopTranscations topTranscations = new TopTranscations();
            topTranscations.setCompanySymbol(tds.get(0).text());
            topTranscations.setNoOfTranscations(tds.get(1).text());
            topTranscations.setClosingPrice(tds.get(2).text());
            topTranscationsList.add(topTranscations);
        }
        return topTranscationsList;

    }
}
