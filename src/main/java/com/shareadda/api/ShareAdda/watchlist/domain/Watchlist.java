package com.shareadda.api.ShareAdda.watchlist.domain;

import com.shareadda.api.ShareAdda.LiveMarket.domain.LiveMarket;
import com.shareadda.api.ShareAdda.User.Domain.User;
import com.shareadda.api.ShareAdda.audititing.Auditing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "watchlist")
public class Watchlist extends Auditing implements Serializable {

    @Id
    private String id;

    private User user;
    private List<LiveMarket> liveMarkets = new ArrayList<>();

}
