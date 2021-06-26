package com.shareadda.api.ShareAdda.LiveMarket.repository;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopShareTraded;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopShareTradedRepository extends MongoRepository<TopShareTraded,String> {
}
