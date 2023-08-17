package com.example.currencyapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.currencyapp.model.CurrencyRepository;
import com.example.currencyapp.model.room.Currency;

import java.util.List;

public class CurrencyViewModel extends AndroidViewModel {
    //for local database
    private CurrencyRepository currencyRepository;
    private LiveData<List<Currency>> allCurrency;
    //private LiveData<List<Currency>> allCurrencyAPI;
    //private CurrencyRepository currencyRepositoryAPI;

    public CurrencyViewModel(@NonNull Application application) {
        super(application);
        currencyRepository = new CurrencyRepository(application);
        allCurrency = currencyRepository.getAllCurrency();
        //allCurrencyAPI = currencyRepositoryAPI.getAllCurrencyAPI("USD");
    }

    public void insert(Currency currency)
    {
        currencyRepository.insert(currency);
    }

    public void delete(Currency currency)
    {
        currencyRepository.delete(currency);
    }

    public void update(Currency currency)
    {
        currencyRepository.update(currency);
    }

    public void deleteAllCurrency()
    {
        currencyRepository.deleteAllCurrency();
    }

    public LiveData<List<Currency>> getAllCurrency()
    {
        return allCurrency;
    }

    public List<Currency> getSpecificCurrency(String currency) {
        return currencyRepository.getSpecificCurrency(currency);
    }
    //for API

}
