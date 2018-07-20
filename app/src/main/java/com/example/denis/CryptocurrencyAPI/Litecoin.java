package com.example.denis.CryptocurrencyAPI;

import com.example.denis.POJO.ChainSo.Result;

import java.util.HashMap;

import io.reactivex.Single;

public class Litecoin implements ICryptocurrency{
    public Litecoin() {

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
