package com.shareadda.api.ShareAdda.portfolio.service;

import com.shareadda.api.ShareAdda.portfolio.domain.PortfolioSummary;
import com.shareadda.api.ShareAdda.portfolio.dto.PortfolioAddRequestDto;

import java.util.List;

public interface PortfolioSummaryI {
    PortfolioSummary save(PortfolioSummary PortfolioSummary);

    List<PortfolioSummary> findByUserId(String userid);

    PortfolioSummary findById(String id);

    PortfolioSummary add(PortfolioAddRequestDto portfolioAddRequestDto);

    void deleteById(String id);

    PortfolioSummary deleteByIdAndSymbol(String id, String symbol);
}
