package com.shareadda.api.ShareAdda.portfolio.Controller;

import com.shareadda.api.ShareAdda.portfolio.domain.PortfolioSummary;
import com.shareadda.api.ShareAdda.portfolio.dto.PortfolioAddRequestDto;
import com.shareadda.api.ShareAdda.portfolio.dto.PortfolioSummaryRequestDto;
import com.shareadda.api.ShareAdda.portfolio.service.PortfolioSummaryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePortfolioSummaryById(@PathVariable String id) {
        portfolioSummaryI.deleteById(id);
        Map<String, String> newMap = new HashMap<>();
        newMap.put("status","success");
        return new ResponseEntity<>(newMap,HttpStatus.OK);
    }

    @DeleteMapping("/{id}/{symbol}")
    public ResponseEntity<?> deletePortfolioByUserAndSymbol(@PathVariable String id,@PathVariable String symbol) {
        PortfolioSummary summarys = portfolioSummaryI.deleteByIdAndSymbol(id, symbol);
        if (summarys == null) {
            Map<String, String> newMap = new HashMap<>();
            newMap.put("status","success");

            return new ResponseEntity<>(newMap, HttpStatus.OK);
        }
        return new ResponseEntity<>(summarys,HttpStatus.OK);
    }

    @PostMapping("/portfoliosummary")
    public ResponseEntity<?> addPortfolioSummary(@Valid @RequestBody PortfolioSummaryRequestDto portfolioSummaryRequestDto) {
        PortfolioSummary portfolioSummary = new PortfolioSummary();
        portfolioSummary.setName(portfolioSummaryRequestDto.getName());
        portfolioSummary.setType(portfolioSummaryRequestDto.getType());
        portfolioSummary.setUserId(portfolioSummaryRequestDto.getUserId());
        portfolioSummary.setAllPortfolio(new ArrayList<>());
        return new ResponseEntity<>(portfolioSummaryI.save(portfolioSummary),HttpStatus.CREATED);
    }


}
