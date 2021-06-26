package com.shareadda.api.ShareAdda.watchlist.service.impl;

import com.shareadda.api.ShareAdda.LiveMarket.domain.LiveMarket;
import com.shareadda.api.ShareAdda.LiveMarket.domain.dto.LiveMarketDto;
import com.shareadda.api.ShareAdda.LiveMarket.service.ScrappingService;
import com.shareadda.api.ShareAdda.User.Domain.User;
import com.shareadda.api.ShareAdda.User.Repository.UserRepository;
import com.shareadda.api.ShareAdda.exception.BackendException;
import com.shareadda.api.ShareAdda.watchlist.domain.Watchlist;
import com.shareadda.api.ShareAdda.watchlist.dto.WatchListResponseDto;
import com.shareadda.api.ShareAdda.watchlist.dto.WatchlistRequestDto;
import com.shareadda.api.ShareAdda.watchlist.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
public class WatchListServiceImpl implements WatchlistService {

    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScrappingService scrappingService;

    @Override
    public WatchListResponseDto save(WatchlistRequestDto watchlist) throws IOException {
        Optional<User> user = userRepository.findById(watchlist.getUserId());
        User newuser = null;
        if (user.isPresent()) {
            newuser = user.get();
        }else{
            throw new BackendException("User with userid "+watchlist.getUserId()+" Not found");
        }
        Watchlist watchlistDomain = new Watchlist();
        watchlistDomain.setUser(newuser);
        Watchlist wl = watchListRepository.findByUserId(watchlist.getUserId());
        List<LiveMarket> liveMarkets = new ArrayList<>();
        if(wl.getLiveMarkets().size()==0) {
            LiveMarket liveMarket = new LiveMarket();
            liveMarket.setSymbol(watchlist.getSymbol());
            liveMarkets.add(liveMarket);
        }else{
            LiveMarket liveMarket = new LiveMarket();
            liveMarket.setSymbol(watchlist.getSymbol());
            liveMarkets = wl.getLiveMarkets();
            liveMarkets.add(liveMarket);

        }
        watchlistDomain.setLiveMarkets(liveMarkets);
        WatchListResponseDto watchListResponseDto = new WatchListResponseDto();
        Watchlist wc = watchListRepository.save(watchlistDomain);
        watchListResponseDto.setUser(wc.getUser());
        watchListResponseDto.setResults(wc.getLiveMarkets());

        return watchListResponseDto;
    }

    @Override
    public WatchListResponseDto findByUserId(String userId) throws IOException {
        Watchlist wc = watchListRepository.findByUserId(userId);
        LiveMarketDto liveMarketDto = scrappingService.scrapeLiveMarket();
        for(int i = 0;i<wc.getLiveMarkets().size();i++){
            for(int j = 0;j<liveMarketDto.getResults().size();j++){
                if(wc.getLiveMarkets().get(j).getSymbol().equals(liveMarketDto.getResults().get(j).getSymbol())){
                    wc.getLiveMarkets().set(j,liveMarketDto.getResults().get(i));
                    break;
                }
            }

        }
        Watchlist watchList = watchListRepository.save(wc);
        WatchListResponseDto watchListResponseDto = new WatchListResponseDto();
        watchListResponseDto.setResults(watchList.getLiveMarkets());
        watchListResponseDto.setUser(watchList.getUser());
        return watchListResponseDto;
    }
}
