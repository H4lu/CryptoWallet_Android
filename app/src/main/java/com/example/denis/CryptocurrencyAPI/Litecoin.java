package com.example.denis.CryptocurrencyAPI;

import com.example.denis.POJO.ChainSo.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.TestNet3Params;

import java.util.HashMap;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Litecoin implements ICryptocurrency{
    private NetworkParameters networkParameters = TestNet3Params.get();
    private Transaction transaction;
    private final int END_OF_SCRIPT_ASM = 73;
    private final String NETWORK = "LTCTEST";
    private final String BASE_URL = "https://chain.so/api/v2/";
    private Retrofit retrofit;
    private String address = "";

    public Litecoin() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        retrofit = new Retrofit.Builder().baseUrl(this.BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @Override
    public void initAddress() {

    }

    @Override
    public Single<Result> getBalanceAsync() {
        return null;
    }

    @Override
    public HashMap<String, String> getLastTransactions() {
        return null;
    }

    @Override
    public double getBalance() {
        return 0;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public void sendTransaction() {

    }
}
