package com.shareadda.api.ShareAdda.LiveMarket.domain.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ListedCompaniesDto {

    private String symbol;


    private String name;

    private String companyType;
    private String companyNo;


}
