package com.example.denis.CryptocurrencyAPI;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ITestConnect {
    @GET("/")
    Single<String> testConnect();

    @GET("/address")
    Single<String> getTestAddress();
}