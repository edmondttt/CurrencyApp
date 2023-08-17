package com.example.currencyapp.model.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIRequest {
    @GET("v6/1f30a7e88410cd7139aafb8e/latest/{currency}")
    Call<JsonObject> getCurrency(@Path("currency") String currency);
}
