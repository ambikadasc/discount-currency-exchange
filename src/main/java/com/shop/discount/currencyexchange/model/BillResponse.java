package com.shop.discount.currencyexchange.model;

import lombok.Value;

@Value
public class BillResponse {
    double totalAmount;
    double payableAmount;
}