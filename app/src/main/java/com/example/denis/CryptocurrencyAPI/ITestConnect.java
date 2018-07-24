package com.example.denis.CryptocurrencyAPI;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ITestConnect {
    @GET("/")
    Single<String> testConnect();

    @GET("/address")
    Single<String> getTestAddress();

    @POST("/sign")
    Single<String> getTestSignature(@Body Bitcoin.BodySign txHash);
}