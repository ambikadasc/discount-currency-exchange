package com.shop.discount.currencyexchange.service;

import com.shop.discount.currencyexchange.model.BillRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shop.discount.currencyexchange.constants.DiscountConstants.*;
import com.shop.discount.currencyexchange.model.Item;


@Service
public class DiscountService {

    public double calculateDiscount(BillRequest request) {
        boolean isGroceries = request.getItems().stream().anyMatch(item -> GROCERIES.equals(item.getCategory()));
        double totalAmount = request.getItems().stream().mapToDouble(Item::getPrice).sum();
        double percentageDiscount = isGroceries ? 0.0 : getMaxPercentageDiscount(request);
        double bulkDiscount = BULK_DISCOUNT.apply(request);
        return (totalAmount * percentageDiscount) + bulkDiscount;
    }

    private double getMaxPercentageDiscount(BillRequest request) {
        return List.of(EMPLOYEE_DISCOUNT, AFFILIATE_DISCOUNT, LOYAL_CUSTOMER_DISCOUNT)
                .stream()
                .mapToDouble(func -> func.apply(request))
                .max()
                .orElse(0.0);
    }
}
