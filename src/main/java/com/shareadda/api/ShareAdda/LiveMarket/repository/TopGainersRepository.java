package com.shareadda.api.ShareAdda.LiveMarket.repository;

import com.shareadda.api.ShareAdda.LiveMarket.domain.TopGainers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopGainersRepository extends MongoRepository<TopGainers,String> {
}
