package com.shop.discount.currencyexchange.service;

import com.shop.discount.currencyexchange.errorhandling.InvalidCurrencyException;
import com.shop.discount.currencyexchange.model.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

   @Autowired
   RestTemplate restTemplate;

    @Cacheable("exchangeRates")
    public double getExchangeRate(String baseCurrency, String targetCurrency) {
        if (!isValidCurrencyCode(baseCurrency)) {
            throw new InvalidCurrencyException("Invalid base currency code: " + baseCurrency);
        }
        if (!isValidCurrencyCode(targetCurrency)) {
            throw new InvalidCurrencyException("Invalid target currency code: " + targetCurrency);
        }

        // Fetch exchange rate
        String url = String.format("https://open.er-api.com/v6/latest/%s?apikey=your-api-key", baseCurrency);
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

        if (response == null || response.getRates() == null || !response.getRates().containsKey(targetCurrency)) {
            throw new InvalidCurrencyException("Failed to fetch exchange rate for target currency: " + targetCurrency);
        }

        return response.getRates().get(targetCurrency);
    }

    private boolean isValidCurrencyCode(String currencyCode) {
        // Example: Validate currency code format (3 uppercase letters)
        return currencyCode != null && currencyCode.matches("[A-Z]{3}");
    }
}
