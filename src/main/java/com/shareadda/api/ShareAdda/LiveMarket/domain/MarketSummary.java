package com.shareadda.api.ShareAdda.LiveMarket.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "market_summary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MarketSummary implements Serializable {
    @Id
    private String id;

    private String totalTurnover;
    private String date;
    private String totalTradedShare;
    private String totalTranscations;
    private String totalScriptTraded;
    private String totalMarketCapitalization;
    private String floatedMarketCapitalization;

    @Indexed
    @CreatedDate
    private Date creationDate = new Date();

    @LastModifiedDate
    private Date lastModifiedDate;
}
