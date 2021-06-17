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

@Document(collection = "company_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CompanyDetails implements Serializable {
    @Id
    private String id;

    @TextIndexed
    private String symbol;
    @TextIndexed
    private String companyName;
    private String sector;
    private String listedShares;
    private String paidupValue;
    private String totalPaidupvalue;
    private String shareOutStanding;
    private String marketPrice;
    private String percentChange;
    private String lastTradedOn;
    private String fiftyTwoWeeksHighLow;
    private String hunderdTwentyDayAverage;
    private String oneYearYield;
    private String eps;
    private String peRatio;
    private String bookValue;
    private String pbv;
    private String hunderEightyDayAverage;
    private String companyNo;


    @Indexed
    @CreatedDate
    private Date creationDate = new Date();

    @LastModifiedDate
    private Date lastModifiedDate;
}
