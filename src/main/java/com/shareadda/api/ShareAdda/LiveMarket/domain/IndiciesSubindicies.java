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

@Document(collection = "indices_subindices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class IndiciesSubindicies implements Serializable {
    @Id
    private String id;

    @TextIndexed
    private String indicesName;

    private String currentPrice;
    private String pointsChange;
    private String percentChange;
    private String date;


    @Indexed
    @CreatedDate
    private Date creationDate = new Date();

    @LastModifiedDate
    private Date lastModifiedDate;
}
