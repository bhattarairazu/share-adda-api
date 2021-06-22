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

@Document(collection = "live_market")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class LiveMarket implements Serializable {

    @Id
    private String id;

    @TextIndexed
    private String symbol;

    private String ltp;
    private String ltv;
    private String pointchange;
    private String percentchange;
    private String open;
    private String high;
    private String low;
    private String volume;
    private String previousclosing;

    @Indexed
    @CreatedDate
    private Date creationDate = new Date();

    @LastModifiedDate
    private Date lastModifiedDate;
}
