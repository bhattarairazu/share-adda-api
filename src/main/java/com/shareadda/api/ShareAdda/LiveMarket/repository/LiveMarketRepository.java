package com.shareadda.api.ShareAdda.LiveMarket.repository;
import com.shareadda.api.ShareAdda.LiveMarket.domain.LiveMarket;
import com.shareadda.api.ShareAdda.LiveMarket.domain.dto.LiveMarketDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LiveMarketRepository extends MongoRepository<LiveMarket,String> {

    List<LiveMarket> findBySymbolOrderByCreationDateDesc(String symbol);
}
