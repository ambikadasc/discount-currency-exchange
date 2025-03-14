package com.shop.discount.currencyexchange.model;

import lombok.Data;
import lombok.Value;
import java.util.List;

@Value
@Data
public class BillRequest {
    List<Item> items;
    String userType;
    int customerTenure;
    String originalCurrency;
    String targetCurrency;
}