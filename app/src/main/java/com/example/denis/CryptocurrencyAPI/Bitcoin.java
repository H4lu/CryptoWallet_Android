package com.example.denis.CryptocurrencyAPI;

import com.example.denis.POJO.ChainSo.LastTx;
import com.example.denis.POJO.ChainSo.Result;
import com.example.denis.POJO.ChainSo.SendTransactionBody;
import com.example.denis.POJO.ChainSo.TransactionResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Test;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.UTXO;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.CoinSelection;
import org.bitcoinj.wallet.CoinSelector;
import org.web3j.crypto.Hash;

import static org.bitcoinj.core.Utils.HEX;
import static org.bitcoinj.core.Utils.finishMockSleep;


public class Bitcoin implements ICryptocurrency {
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    private Transaction transaction;
    private final int FEE = 200;
    private final int END_OF_SCRIPT_ASM = 73;
    private final String NETWORK = "BTCTEST";
    private final  String BASE_URL = "https://chain.so/api/v2/";
    private Retrofit retrofit;
    private String address = "";

    public void setAddress(String address) {
        this.address = address;
    }


    public String getAddress_Check() {
        return this.address;
    }


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
    public Single<TransactionResult> sendTx(String body) {
        return this.retrofit.create(IChainSO.class).sendTransaction(this.NETWORK, new SendTransactionBody(body));
    }
    @Override
    public double getBalance() {
        return 0;
    }

    /*
    private Observable<HashSet<UTXO>> obser = io.reactivex.Observable.create()
    private Observable<HashSet<UTXO>> parseUTXO = io.reactivex.Observable.create(){
         getUTXO()
                .map(res -> res.getData().getTxs())
                .map(res -> {
                    Set<UTXO> utxos = new HashSet<>();
                    for (int i =0;i<res.size();i++) {
                           final LastTx.Tx tx = res.get(i);
                           final Sha256Hash utxoHash = Sha256Hash.wrap(tx.getTxid());
                           final int utxoIndex = tx.getOutput_no();
                           final Coin utxoValue = Coin.parseCoin(tx.getValue());
                           // Cut OP_CHECKSIG
                           System.out.println(tx.getScript_asm().substring(0, this.END_OF_SCRIPT_ASM));
                           final Script utxoScript = new Script(tx.getScript_asm().substring(0,this.END_OF_SCRIPT_ASM).getBytes());
                           utxos.add(new UTXO(utxoHash, utxoIndex, utxoValue,  tx.getConfirmations(),false, utxoScript));
                        }
                        return utxos;
                });
    }
    */
    public Single<LastTx> getUTXO() {
        return this.retrofit.create(IChainSO.class).getUTXO(this.NETWORK, this.address);
    }
    public Single<String> getTestSignature(String hex) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        System.out.println("GOT THIS HASH " + hex);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit fit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000").client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return fit.create(ITestConnect.class).getTestSignature(new BodySign(hex));
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                .build();
        return fit.create(ITestConnect.class).getTestAddress();

    }

    @Override
    public void initAddress() {

    }
    public static class BodySign {
        private final String txHex;
        BodySign(String hex) {
            this.txHex = hex;
        }
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

    public HashSet<UTXO> parseUTXO(List<LastTx.Tx> data) {

        HashSet<UTXO> utxos = new HashSet<>();
        for (LastTx.Tx tx: data) {
            final Sha256Hash utxoHash = Sha256Hash.wrap(tx.getTxid());
            final int utxoIndex = tx.getOutput_no();
            final Coin utxoValue = Coin.parseCoin(tx.getValue());
            System.out.println(Base58.encode("0ae4da83696abd6515d3a7d62736d6aa60f1d6c8".getBytes()));
            // final Script utxoScript = new Script(tx.getScript_hex().getBytes());
            final Script utxoScript = new Script(tx.getScript_asm().substring(0, this.END_OF_SCRIPT_ASM).getBytes());
            // final Script utxoScript = new Script("76a9140ae4da83696abd6515d3a7d62736d6aa60f1d6c888ac".getBytes());
            utxos.add(new UTXO(utxoHash, utxoIndex, utxoValue, tx.getConfirmations(), false, ScriptBuilder.createOutputScript(Address.fromBase58(TestNet3Params.get(), this.address))));
        }
        return utxos;
    }

    public Transaction formTx(long amount, String paymentAddress, UTXO input) {
        System.out.println("GOT THIS INPUT" + input.getHash().toString());
        Transaction tx = new Transaction(TestNet3Params.get());
        tx.addOutput(Coin.valueOf(amount), Address.fromBase58(TestNet3Params.get(), paymentAddress));
        // change output
        tx.addOutput(Coin.valueOf(input.getValue().value - Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.value-Coin.valueOf(amount).value), Address.fromBase58(TestNet3Params.get(), this.address));
        System.out.println("BEFORE THIS " + HEX.encode(tx.bitcoinSerialize()));
        tx.addInput(input.getHash(), 0, input.getScript());
        //tx.addInput(new TransactionOutput(TestNet3Params.get(), null, input.getScript().getProgram(),input.getScript().getProgram().length));
        System.out.println("SETTING THIS SCRIPT " + HEX.encode(input.getScript().getProgram()).toString());
        System.out.println("SERIALIZE" + HEX.encode(tx.unsafeBitcoinSerialize()));
        System.out.println(HEX.encode(ScriptBuilder.createOutputScript(Address.fromBase58(TestNet3Params.get(), this.address)).getProgram()));
        setTransaction(tx);
        return tx;
    }

    /*public static String replaceAt(String input, String search, String replace, int start, int end) {
           return input.substring(0, start) +
                  input.substring(start, end).replace(search, replace) +
                  input.substring(end);
    }
    */
    public String getHashForSig(Transaction transaction) {
        String tempTx = HEX.encode(transaction.bitcoinSerialize());
        String txForSig = tempTx.substring(0, 74);
        return "";
    }
    public void createTransaction(String paymentAddress, long amount) {
        /*Transaction tx = new Transaction(NetworkParameters.testNet3());
        tx.addOutput(Coin.valueOf(amount), Address.fromBase58(NetworkParameters.testNet3(), paymentAddress));
        System.out.println("SERIALIZE" + HEX.encode(tx.bitcoinSerialize()));
        */
        getUTXO()
                .map(res -> parseUTXO(res.getData().getTxs()))
                .map(res -> res.iterator().next())
                .map(res -> formTx(amount, paymentAddress, res))
                .flatMap(res -> getTestSignature(res.hashForSignature(0, res.getInput(0).getScriptSig().getProgram(), Transaction.SigHash.ALL, false).toString()))
                .map(res -> {
                    String transaction = HEX.encode(getTransaction().bitcoinSerialize());
                   // System.out.println(HEX.encode(getTransaction().getInput(0).getScriptSig().getPubKeyHash()));

                    //dont know why bitcoinj add`s additional 2 bytes in beginning
                    String result = transaction.substring(0, 74) + "00000000" + res.toString().toLowerCase() + transaction.substring(transaction.indexOf("ffffffff"), transaction.length());
                    return result;
                })
                .flatMap(res -> sendTx(res))
                .subscribe();


    }
}
