package com.shareadda.api.ShareAdda.portfolio.Controller;

import com.shareadda.api.ShareAdda.portfolio.domain.PortfolioSummary;
import com.shareadda.api.ShareAdda.portfolio.dto.PortfolioAddRequestDto;
import com.shareadda.api.ShareAdda.portfolio.service.PortfolioSummaryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioSummaryI portfolioSummaryI;

    @PostMapping("/")
    public ResponseEntity<?> addPortfolio(@Valid @RequestBody PortfolioAddRequestDto portfolioAddRequestDto){
        return new ResponseEntity<>(portfolioSummaryI.add(portfolioAddRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<PortfolioSummary>> getUserPortfolio(@PathVariable("user_id") String user_id){
        return new ResponseEntity<>(portfolioSummaryI.findByUserId(user_id), HttpStatus.OK);
    }
}
