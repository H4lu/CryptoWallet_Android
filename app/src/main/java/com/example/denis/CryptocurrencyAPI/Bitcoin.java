package com.example.denis.CryptocurrencyAPI;

import com.example.denis.POJO.ChainSo.LastTx;
import com.example.denis.POJO.ChainSo.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.UTXO;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.CoinSelection;
import org.bitcoinj.wallet.CoinSelector;
import static org.bitcoinj.core.Utils.HEX;


public class Bitcoin implements ICryptocurrency {

    private String address = "";

    public void setAddress(String address) {
        this.address = address;
    }


    public String getAddress_Check() {
        return this.address;
    }
    private final String NETWORK = "BTCTEST";
    private final  String BASE_URL = "https://chain.so/api/v2/";
    private Retrofit retrofit;
    public Bitcoin() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
         retrofit = new Retrofit.Builder().baseUrl(this.BASE_URL).client(okHttpClient)
                 .addConverterFactory(GsonConverterFactory.create(gson))
                 .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                .build();
    }

    public ITestConnect testConnect() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit fit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ITestConnect test = fit.create(ITestConnect.class);
        Single<String> res = test.testConnect();
        return test;
        /*iTestConnect.testConnect().enqueue(new Callback<Observable<String>>() {
            @Override
            public void onResponse(Call<Observable<String>> call, Response<Observable<String>> response) {
                System.out.println("RESPONSE " + response.body());
            }

            @Override
            public void onFailure(Call<Observable<String>> call, Throwable t) {

            }
        });
*/
    }
    @Override
    public Single<Result> getBalanceAsync() throws IOException {
        return this.retrofit.create(IChainSO.class).getBalance(this.NETWORK, this.address);
    }

    @Override
    public double getBalance() {
        return 0;
    }
    private HashSet<UTXO> parseUTXO() {
        getUTXO()
                .map(res -> res.getData().getTxs())
                .map(res -> {
                    Set<UTXO> utxos = new HashSet<>();
                    for (int i =0;i<res.size();i++) {
                           final LastTx.Tx tx = res.get(i);
                           final Sha256Hash utxoHash = Sha256Hash.wrap(tx.getTxid());
                           final int utxoIndex = tx.getOutput_no();
                           final Coin utxoValue = Coin.parseCoin(tx.getValue());
                           final byte[] test = tx.getScript_asm().getBytes();
                           System.out.println(test);
                           final Script utxoScript = new Script("OP_DUP OP_HASH160 0ae4da83696abd6515d3a7d62736d6aa60f1d6c8 OP_EQUALVERIFY".getBytes());
                           utxos.add(new UTXO(utxoHash, utxoIndex, utxoValue,  tx.getConfirmations(),false, utxoScript));
                        }
                        return utxos;
                })
                .subscribe(utxo -> {
                    System.out.println("GOT THIS UTXOS");
                    for (UTXO tx: utxo) {
                        System.out.println(tx);
                    }
                });
        return null;
    }
    public Single<LastTx> getUTXO() {
        return this.retrofit.create(IChainSO.class).getUTXO(this.NETWORK, this.address);
    }
    /*OkHttpClient client = new OkHttpClient();
        client.interceptors().add(logging);
        Request request = new Request.Builder().url("https://chain.so/api/v2/get_address_balance/BTC/" + address + "/0").build();
        try (Response response = client.newCall(request).execute()) {
            String resp = response.body().toString();
            Log.i("Response",resp);
            System.out.println("TEST");
            System.out.println(resp);
        }*/
    /*public String testReq() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://chain.so/api/v2/get_address_balance/BTC/" + address + "/0").build();
        String resp;
        try (Response response = client.newCall(request).execute()) {
            resp = response.body().toString();
            Log.i("Response", resp);

        }
        return resp;
    }
    */

    public Single<String> initAddresss() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit fit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000").client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return fit.create(ITestConnect.class).getTestAddress();

    }

    @Override
    public void initAddress() {

    }

    @Override
    public HashMap<String,String> getLastTransactions() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public void sendTransaction() {

    }
    public void createTransaction(String paymentAddress, long amount) {
        Transaction tx = new Transaction(NetworkParameters.testNet3());
        tx.addOutput(Coin.valueOf(amount), Address.fromBase58(NetworkParameters.testNet3(), paymentAddress));
        System.out.println("SERIALIZE" + HEX.encode(tx.bitcoinSerialize()));
        getUTXO()
                .map(res -> {
                    parseUTXO();
                    return res.getData();})
                .subscribe(res -> {

                    //System.out.println("GOT THIS UTXO" + res);
                });

    }
}
