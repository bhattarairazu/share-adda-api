package com.shareadda.api.ShareAdda.LiveMarket.repository;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopTurnOver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopTurnOverRepository extends MongoRepository<TopTurnOver,String> {
}
