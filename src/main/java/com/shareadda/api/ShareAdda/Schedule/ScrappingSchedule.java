package com.shareadda.api.ShareAdda.Schedule;

import com.shareadda.api.ShareAdda.LiveMarket.domain.dto.*;
import com.shareadda.api.ShareAdda.LiveMarket.repository.*;
import com.shareadda.api.ShareAdda.LiveMarket.service.ScrappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Component
public class ScrappingSchedule {

    @Autowired
    private ScrappingService scrappingService;

    @Autowired
    private FloorSheetRepository floorSheetRepository;

    @Autowired
    private LiveMarketRepository liveMarketRepository;

    @Autowired
    private IndiciesSubindiciesRepository indiciesSubindiciesRepository;

    @Autowired
    private MarketSummary marketSummaryRepostiroy;

    @Autowired
    private TopShareTradedRepository topShareTradedRepository;

    @Autowired
    private TopTranscationsRepository topTranscationsRepository;

    @Autowired
    private TopTurnOverRepository topTurnOverRepository;

    @Autowired
    private TopLoosersRepository topLoosersrepository;

    @Autowired
    private TopGainersRepository topGainersRepository;

    private static final Logger log = LoggerFactory.getLogger(ScrappingSchedule.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron="00 58 14 * * SUN-THU")
    public void liveScrapCronJob() throws IOException,NullPointerException {
        log.info("Scrapping server hit: for live data");
        saveLiveMarket();
    }

    @Scheduled(cron ="00 01 15 * * SUN-THU")
    public void reportCurrentTime() {
        log.info("Scrapping data from nepal stock  mon-thur 3:01 PM {}", dateFormat.format(new Date()));
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
            savedFloorSheet();
            saveIndiciesSubIndicies();
            marketSummary();
            saveTopGainers();
            saveTopLoosers();
            saveTopShareTraded();
            saveTopTranscations();
            saveTopTurnOver();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    private void savedFloorSheet() throws IOException {
        floorSheetRepository.deleteAll();
        FloorSheetDto floorSheetDto = scrappingService.getFloorSheet(null);
        floorSheetRepository.saveAll(floorSheetDto.getResults());
        log.info("Saved Floorsheet all datas");
    }

    private void saveLiveMarket() throws IOException{
        liveMarketRepository.deleteAll();
        LiveMarketDto liveMarketDto = scrappingService.scrapeLiveMarket();
        liveMarketRepository.saveAll(liveMarketDto.getResults());
        log.info("Saved Live Market Data of "+ LocalDate.now());
    }

    private void saveIndiciesSubIndicies() throws IOException{
        indiciesSubindiciesRepository.deleteAll();
        IndiciesSubIndiciesDto inciciesSubIndiciesdto = scrappingService.indiciesSubindicies();
        indiciesSubindiciesRepository.saveAll(inciciesSubIndiciesdto.getResults());
        log.info("Saved Indicies SubIndicies Data of "+ LocalDate.now());
    }
    private void marketSummary() throws IOException{
        marketSummaryRepostiroy.deleteAll();
        marketSummaryRepostiroy.save(scrappingService.getMarketSummary());
        log.info("Saved Market Summary Data of "+ LocalDate.now());

    }

    private void saveTopGainers() throws  IOException{
        topGainersRepository.deleteAll();
        TopGainersDto topGainersDto = scrappingService.getTopGainer();
        topGainersRepository.saveAll(topGainersDto.getResults());
        log.info("Saved Top gainers " + LocalDate.now());
    }

    private void saveTopLoosers() throws IOException{
        topLoosersrepository.deleteAll();
        TopLoosersDto topLoosersDto =scrappingService.getTopLoosers();
        topLoosersrepository.saveAll(topLoosersDto.getResults());
        log.info("Saved Top Loosers " + LocalDate.now());
    }

    private void saveTopShareTraded() throws IOException{
        topShareTradedRepository.deleteAll();
        TopShareTradedDto topShareTradedDto = scrappingService.getTopShareTraded();
        topShareTradedRepository.saveAll(topShareTradedDto.getResults());
        log.info("Saved Top Share Traded " + LocalDate.now());
    }

    private void saveTopTranscations() throws IOException{
        topTranscationsRepository.deleteAll();
        TopTranscationsDto topTranscationsDto = scrappingService.getTopTranscations();
        topTranscationsRepository.saveAll(topTranscationsDto.getResults());
        log.info("Saved Top Transcations " + LocalDate.now());
    }

    private void saveTopTurnOver() throws  IOException {
        topTurnOverRepository.deleteAll();
        TopTurnoverDto topTurnoverDto = scrappingService.getTopTurnOver();
        topTurnOverRepository.saveAll(topTurnoverDto.getResults());
        log.info("Saved Top TurnOver " + LocalDate.now());

    }






}
