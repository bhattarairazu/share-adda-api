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

@Document(collection = "listed_companies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ListedCompanies implements Serializable {
    @Id
    private String id;

    @TextIndexed
    private String symbol;

    @TextIndexed
    private String name;
    @TextIndexed
    private String companyType;
    private String companyNo;

    @Indexed
    @CreatedDate
    private Date creationDate = new Date();

    @LastModifiedDate
    private Date lastModifiedDate;
}
