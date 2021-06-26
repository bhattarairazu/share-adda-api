package com.shareadda.api.ShareAdda.LiveMarket.repository;

import com.shareadda.api.ShareAdda.LiveMarket.domain.FloorSheet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorSheetRepository extends MongoRepository<FloorSheet,String> {
}
