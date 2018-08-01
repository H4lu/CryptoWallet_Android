package com.example.denis.CryptocurrencyAPI;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;


import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
public interface ITestConnect {
    @GET("/")
    Single<String> testConnect();

    @GET("/address0")
    Single<String> getTestAddress();

    @GET("/address1")
    rx.Observable<String> getETHAddress();

    @POST("/sign")
    io.reactivex.Observable<String> getTestSignature(@Body Bitcoin.BodySign txHash);

    @POST("/sign1")
    rx.Observable<String> getETHSignature(@Body Bitcoin.BodySign txHash);
}