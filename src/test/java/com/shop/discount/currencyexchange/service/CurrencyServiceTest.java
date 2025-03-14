package com.shop.discount.currencyexchange.service;

import com.shop.discount.currencyexchange.errorhandling.InvalidCurrencyException;
import com.shop.discount.currencyexchange.model.ExchangeRateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyService currencyService=new CurrencyService();



    @Test
    public void testGetExchangeRate_ValidCurrencies() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", 0.85);
        rates.put("AED", 3.67); // Assumption 1 USD = 3.67 AED
        rates.put("INR", 82.0); // Assumption 1 USD = 82 INR
        ExchangeRateResponse mockResponse = new ExchangeRateResponse(rates);

        Mockito.when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(mockResponse);

        double exchangeRate = currencyService.getExchangeRate("USD", "EUR");
        assertEquals(0.85, exchangeRate);

        double aedRate = currencyService.getExchangeRate("USD", "AED");
        assertEquals(3.67, aedRate);

        double inrRate = currencyService.getExchangeRate("USD", "INR");
        assertEquals(82.0, inrRate);
    }

    @Test
    public void testGetExchangeRate_InvalidBaseCurrency() {
        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.getExchangeRate("INVALID", "EUR");
        });
    }

    @Test
    public void testGetExchangeRate_InvalidTargetCurrency() {
        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.getExchangeRate("USD", "INVALID");
        });
    }

    @Test
    public void testGetExchangeRate_ApiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(null);

        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.getExchangeRate("USD", "EUR");
        });
    }

    @Test
    public void testGetExchangeRate_ApiReturnsInvalidRates() {
        ExchangeRateResponse mockResponse = new ExchangeRateResponse(null);

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(mockResponse);

        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.getExchangeRate("USD", "EUR");
        });
    }

    @Test
    public void testGetExchangeRate_TargetCurrencyNotInRates() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        ExchangeRateResponse mockResponse = new ExchangeRateResponse(rates);

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(mockResponse);

        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.getExchangeRate("USD", "EUR");
        });
    }
}