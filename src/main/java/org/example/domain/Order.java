package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class Order {

    String success;
    String orderId;
    String productId;
    String productName;
    String  orderType;
    Integer quantity;
    String username;
    String  previousStock;
    String newStock;
}
