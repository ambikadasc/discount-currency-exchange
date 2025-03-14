package com.shop.discount.currencyexchange.service;

import com.shop.discount.currencyexchange.model.BillRequest;
import com.shop.discount.currencyexchange.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shop.discount.currencyexchange.constants.DiscountConstants.*;


@Service
public class DiscountService {

    public double calculateDiscount(BillRequest request) {
        double nonGroceryAmount = request.getItems().stream()
                .filter(item -> !GROCERIES.equals(item.getCategory()))
                .mapToDouble(Item::getPrice)
                .sum();

        double percentageDiscount = getMaxPercentageDiscount(request) * nonGroceryAmount;
        double bulkDiscount = BULK_DISCOUNT.apply(request);

        return percentageDiscount + bulkDiscount;
    }

    private double getMaxPercentageDiscount(BillRequest request) {
        return List.of(EMPLOYEE_DISCOUNT, AFFILIATE_DISCOUNT, LOYAL_CUSTOMER_DISCOUNT)
                .stream()
                .mapToDouble(func -> func.apply(request))
                .max()
                .orElse(0.0);
    }
}
