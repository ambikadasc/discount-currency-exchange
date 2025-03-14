package com.shop.discount.currencyexchange.service;

import com.shop.discount.currencyexchange.model.BillRequest;
import com.shop.discount.currencyexchange.model.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                List.of(new Item("Rice", 100.0, "groceries")),
                "employee", // Employee discount should not apply to groceries
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(5.0, discount); // Only bulk discount applies: $5 for every $100 = 5
    }

    @Test
    public void testMultipleDiscounts() {
        BillRequest request = new BillRequest(
                List.of(new Item("iPad", 300.0, "electronics")),
                "employee", // 30% discount
                3, // 5% discount (but only one percentage discount applies)
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(105.0, discount); // 30% of 300 = 90 + $5 for every $100: 300 / 100 = 3 * 5 = 15 → Total = 90 + 15 = 105
    }

    @Test
    public void testMixedItems_GroceriesAndElectronics() {
        BillRequest request = new BillRequest(
                List.of(
                        new Item("Potato", 50.0, "groceries"), // No percentage discount
                        new Item("Watch", 150.0, "electronics") // Percentage discount applies
                ),
                "employee", // 30% discount
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        // Total bill = 50 + 150 = 200
        // Percentage discount (30% of 150) = 45
        // Bulk discount ($5 for every $100) = 10
        // Total discount = 45 + 10 = 55
        assertEquals(55.0, discount);
    }

    @Test
    public void testMixedItems_GroceriesOnly() {
        BillRequest request = new BillRequest(
                List.of(
                        new Item("Tomato", 100.0, "groceries"),
                        new Item("Chips", 200.0, "groceries")
                ),
                "employee", // No percentage discount for groceries
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        // Total bill = 100 + 200 = 300
        // No percentage discount for groceries
        // Bulk discount ($5 for every $100) = 15
        // Total discount = 15
        assertEquals(15.0, discount);
    }

    @Test
    public void testMixedItems_ElectronicsOnly() {
        BillRequest request = new BillRequest(
                List.of(
                        new Item("Watch", 100.0, "electronics"),
                        new Item("Tablet", 200.0, "electronics")
                ),
                "affiliate", // 10% discount
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        // Total bill = 100 + 200 = 300
        // Percentage discount (10% of 300) = 30
        // Bulk discount ($5 for every $100) = 15
        // Total discount = 30 + 15 = 45
        assertEquals(45.0, discount);
    }

    @Test
    public void testEmptyItemsList() {
        BillRequest request = new BillRequest(
                List.of(), // Empty list of items
                "employee",
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        assertEquals(0.0, discount); // No items, so no discount
    }

    @Test
    public void testNullItemsList() {
        BillRequest request = new BillRequest(
                null, // Null list of items
                "employee",
                1,
                "USD",
                "EUR"
        );
        assertThrows(NullPointerException.class, () -> {
            discountService.calculateDiscount(request);
        });
    }

    @Test
    public void testInvalidUserType() {
        BillRequest request = new BillRequest(
                List.of(new Item("Watch", 100.0, "electronics")),
                "invalidUserType", // Invalid user type
                1,
                "USD",
                "EUR"
        );
        double discount = discountService.calculateDiscount(request);
        // No percentage discount for invalid user type
        // Bulk discount ($5 for every $100) = 5
        // Total discount = 5
        assertEquals(5.0, discount);
    }
}