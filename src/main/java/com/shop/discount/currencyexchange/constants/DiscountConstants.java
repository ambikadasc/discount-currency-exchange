package com.shop.discount.currencyexchange.constants;

import com.shop.discount.currencyexchange.model.BillRequest;
import com.shop.discount.currencyexchange.model.Item;

import java.util.function.Function;

public class DiscountConstants {

    public static final String EMPLOYEE="employee";
    public static final String AFFILIATE="affiliate";
    public static final String GROCERIES="groceries";



    public static final Function<BillRequest, Double> EMPLOYEE_DISCOUNT = request ->
            EMPLOYEE.equals(request.getUserType()) ? 0.30 : 0.0;

    public static final Function<BillRequest, Double> AFFILIATE_DISCOUNT = request ->
            AFFILIATE.equals(request.getUserType()) ? 0.10 : 0.0;

    public static final Function<BillRequest, Double> LOYAL_CUSTOMER_DISCOUNT = request ->
            request.getCustomerTenure() > 2 ? 0.05 : 0.0;

    public static final Function<BillRequest, Double> BULK_DISCOUNT = request ->
            (double) ((int) (request.getItems().stream().mapToDouble(Item::getPrice).sum() / 100) * 5);
}
