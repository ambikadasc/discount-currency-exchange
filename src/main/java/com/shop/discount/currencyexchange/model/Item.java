package com.shop.discount.currencyexchange.model;

import lombok.Value;

@Value
public class Item {
    String name;
    double price;
    String category;
}