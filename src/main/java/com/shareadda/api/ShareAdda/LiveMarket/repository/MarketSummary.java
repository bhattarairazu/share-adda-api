package com.shareadda.api.ShareAdda.LiveMarket.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketSummary extends MongoRepository<com.shareadda.api.ShareAdda.LiveMarket.domain.MarketSummary,String> {

}
