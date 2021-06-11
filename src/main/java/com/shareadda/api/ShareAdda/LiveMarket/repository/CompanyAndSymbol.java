package com.shareadda.api.ShareAdda.LiveMarket.repository;

import com.shareadda.api.ShareAdda.LiveMarket.domain.CompanyWithSymbolNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAndSymbol extends MongoRepository<CompanyWithSymbolNumber,String> {

    CompanyWithSymbolNumber findBySymbol(String symbol);

}
