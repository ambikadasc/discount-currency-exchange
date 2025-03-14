package com.shop.discount.currencyexchange.controller;

import com.shop.discount.currencyexchange.model.BillRequest;
import com.shop.discount.currencyexchange.model.BillResponse;
import com.shop.discount.currencyexchange.model.Item;
import com.shop.discount.currencyexchange.service.CurrencyService;
import com.shop.discount.currencyexchange.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BillController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private DiscountService discountService;

    @PostMapping("/calculate")
    public ResponseEntity<BillResponse> calculateBill(@RequestBody BillRequest request) {
        double totalAmount = request.getItems().stream().mapToDouble(Item::getPrice).sum();
        double discount = discountService.calculateDiscount(request);
        double netAmount = totalAmount - discount;

        double exchangeRate = currencyService.getExchangeRate(request.getOriginalCurrency(), request.getTargetCurrency());
        double payableAmount = netAmount * exchangeRate;

        return ResponseEntity.ok(new BillResponse(totalAmount, payableAmount));
    }
}
