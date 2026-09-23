package com.bookstore.dao;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GenericDao<T, ID extends Serializable> {

    String fetchTitleAndPrice(String symbol, Instant instant);
    List<String> fetchTitleAndPriceGt25(String symbol, Instant instant);
    
    String fetchTitleAndPriceCb(String symbol, Instant instant);
    List<String> fetchTitleAndPriceGt25Cb(String symbol, Instant instant);
}
