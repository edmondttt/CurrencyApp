package com.example.currencyapp.model.retrofit;

import java.util.Currency;
import java.util.List;

public class APIResponse {

    private String status;
    private List<Currency> currencies;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}
