package com.shareadda.api.ShareAdda.LiveMarket.repository;
import com.shareadda.api.ShareAdda.LiveMarket.domain.LiveMarket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveMarketRepository extends MongoRepository<LiveMarket,String> {

}
