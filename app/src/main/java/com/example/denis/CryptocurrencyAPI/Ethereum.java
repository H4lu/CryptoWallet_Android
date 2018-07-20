package com.example.denis.CryptocurrencyAPI;


import com.example.denis.POJO.ChainSo.Result;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.util.HashMap;

import io.reactivex.Single;

public class Ethereum implements ICryptocurrency {
    private String infuraToken = "hgAaKEDG9sIpNHqt8UYM";
    private String address;
    private Web3j web3j;
    public Ethereum() {
        this.web3j = Web3jFactory.build(new HttpService("https://infura.io/" + this.infuraToken));
        this.initAddress();
    }

    @Override
    public Single<Result> getBalanceAsync() {
        return null;
    }

    @Override
    public double getBalance() {
        return 0;
    }

    @Override
    public void initAddress() {

    }

    @Override
    public HashMap<String, String> getLastTransactions() {
        return null;
    }

    @Override
    public void sendTransaction() {

    }

    @Override
    public String getAddress() {
        return null;
    }
}
