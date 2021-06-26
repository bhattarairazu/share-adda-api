package com.shareadda.api.ShareAdda.LiveMarket.repository;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopTranscations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopTranscationsRepository extends MongoRepository<TopTranscations,String> {
}
