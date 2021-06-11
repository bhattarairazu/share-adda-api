package com.shareadda.api.ShareAdda.LiveMarket.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopLoosers extends MongoRepository<TopLoosers,String> {
}
