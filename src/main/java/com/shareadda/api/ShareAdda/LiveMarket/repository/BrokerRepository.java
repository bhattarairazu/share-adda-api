package com.shareadda.api.ShareAdda.LiveMarket.repository;
import com.shareadda.api.ShareAdda.LiveMarket.domain.Brokers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerRepository extends MongoRepository<Brokers,String> {
}
