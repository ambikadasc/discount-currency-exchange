package com.shop.discount.currencyexchange.service;

import com.shop.discount.currencyexchange.model.BillRequest;
import com.shop.discount.currencyexchange.model.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountServiceTest {

    private final DiscountService discountService = new DiscountService();

    @Test
    public void testEmployeeDiscount() {
        BillRequest request = new BillRequest(
                List.of(new Item("Fridge", 100.0, "electronics")),
                "employee",
                1,
                "USD",
                "USD"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(35.0, discount);
    }

    @Test
    public void testAffiliateDiscount() {
        BillRequest request = new BillRequest(
                List.of(new Item("Fridge", 100.0, "electronics")),
                "affiliate",
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(15.0, discount);
    }

    @Test
    public void testLoyalCustomerDiscount() {
        BillRequest request = new BillRequest(
                List.of(new Item("Watch", 100.0, "electronics")),
                "customer",
                3,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(10.0, discount);
    }

    @Test
    public void testBulkDiscount() {
        BillRequest request = new BillRequest(
                List.of(new Item("Oven", 250.0, "electronics")),
                "customer",
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(10.0, discount);
    }

    @Test
    public void testNoPercentageDiscountForGroceries() {
        BillRequest request = new BillRequest(
                List.of(new Item("item1", 100.0, "groceries")),
                "employee", // Employee discount should not apply to groceries
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(5.0, discount); // Only bulk discount applies: $5 for every $100 = 5
    }
}