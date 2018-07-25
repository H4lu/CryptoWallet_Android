package com.example.denis.CryptocurrencyAPI;

import com.example.denis.POJO.ChainSo.LastTx;
import com.example.denis.POJO.ChainSo.Result;
import com.example.denis.POJO.ChainSo.SendTransactionBody;
import com.example.denis.POJO.ChainSo.TransactionResult;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IChainSO {
    @GET("/api/v2/get_address_balance/{network}/{address}")
    Single<Result> getBalance(@Path ("network") String network, @Path("address") String address);

    @GET("/api/v2/get_tx_unspent/{network}/{address}")
    Single<LastTx> getUTXO(@Path("network") String network, @Path("address") String address);
    @POST("/api/v2/send_tx/{network}")
    Single<TransactionResult> sendTransaction(@Path("network") String network, @Body SendTransactionBody body);
}
