package com.shop.discount.currencyexchange.model;

import lombok.Value;
import java.util.Map;

@Value
public class ExchangeRateResponse {
    Map<String, Double> rates;
}