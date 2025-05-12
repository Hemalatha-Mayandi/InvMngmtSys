package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProductsInStock {
    String productId;
    Integer totalBuys;
    Integer totalSells;
    Integer currentStock;
    Integer totalTransactions;
}
