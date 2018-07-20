package com.example.denis.CryptocurrencyAPI;

import com.example.denis.POJO.ChainSo.Result;

import java.util.HashMap;

import io.reactivex.Single;

public interface ICryptocurrency {
    double getBalance() throws Exception;
    Single<Result> getBalanceAsync() throws Exception;
    void sendTransaction();
    String getAddress();
    void initAddress();
    HashMap<String, String> getLastTransactions();
}
