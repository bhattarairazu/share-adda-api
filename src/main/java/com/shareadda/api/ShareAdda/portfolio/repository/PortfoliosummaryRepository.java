package com.shareadda.api.ShareAdda.portfolio.repository;

import com.shareadda.api.ShareAdda.portfolio.domain.PortfolioSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfoliosummaryRepository extends MongoRepository<PortfolioSummary,String> {
    List<PortfolioSummary> findByUserId(String userId);

}
