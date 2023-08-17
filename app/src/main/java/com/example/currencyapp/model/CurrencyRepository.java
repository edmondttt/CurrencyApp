package com.example.currencyapp.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.currencyapp.model.room.Currency;
import com.example.currencyapp.model.room.CurrencyDao;
import com.example.currencyapp.model.room.CurrencyDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyRepository {

    //for local database
    private CurrencyDao currencyDao;
    private LiveData<List<Currency>> allCurrences;
    private ExecutorService executorService;

    public CurrencyRepository(Application application)
    {
        CurrencyDatabase currencyDatabase = CurrencyDatabase.getInstance(application);
        currencyDao = currencyDatabase.currencyDao();
        allCurrences = currencyDao.getAllCurrency();
        executorService = Executors.newSingleThreadExecutor();
        //apiRequest = RetrofitRequest.getRetrofitInstance().create(APIRequest.class);
    }

    public void insert(Currency currency)
    {
        executorService.execute(()->currencyDao.insert(currency));
    }

    public void delete(Currency currency)
    {
        executorService.execute(()->currencyDao.delete(currency));
    }

    public void update(Currency currency)
    {
        executorService.execute(()->currencyDao.update(currency));
    }

    public void deleteAllCurrency()
    {
        executorService.execute(()->currencyDao.deleteAllCurrency());
    }

    public LiveData<List<Currency>> getAllCurrency()
    {
        return allCurrences;
    }


    public List<Currency> getSpecificCurrency(String currency) {
        return currencyDao.getSpecificCurrency(currency);
    }

    // for API
//    private APIRequest apiRequest;
//
//
//    public LiveData<APIResponse> getAllCurrencyAPI(String currency)
//    {
//        final MutableLiveData<APIResponse> data = new MutableLiveData<>();
//        apiRequest.getCurrency(currency).enqueue(new Callback<APIResponse>() {
//            @Override
//            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//
//                if(response.body() != null)
//                {
//                    data.setValue(response.body());
//                    Log.i("Data", response.body().getCurrencies()+"");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<APIResponse> call, Throwable t) {
//
//                data.setValue(null);
//            }
//        });
//        return data;
//    }


}
