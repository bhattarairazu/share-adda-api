package com.shareadda.api.ShareAdda.watchlist.repository;

import com.shareadda.api.ShareAdda.watchlist.domain.Watchlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends MongoRepository<Watchlist,String> {
    Watchlist findByUserId(String userId);
}
