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
import io.reactivex.Observable;
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
    private NetworkParameters networkParameters = TestNet3Params.get();
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
                 .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
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

    public Observable<String> getTestSignature(String hash) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //System.out.println("GOT THIS HASH " + hex);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit fit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000").client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return fit.create(ITestConnect.class).getTestSignature(new BodySign(hash));
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
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


    public Transaction formTx(long amount, String paymentAddress, HashSet<UTXO> input) {
        Transaction tx = new Transaction(this.networkParameters);
        long transactionValue = 0;
        for (UTXO transaction: input) {
            System.out.println("ADD THIS TRANSACTION " + transaction.getHash() + " WITH THIS INDEX " + transaction.getIndex());
            tx.addInput(transaction.getHash(), transaction.getIndex(), transaction.getScript());
            transactionValue+=transaction.getValue().value;
        }

       tx.addOutput(Coin.valueOf(amount), Address.fromBase58(this.networkParameters, paymentAddress));
      //  tx.addOutput(Coin.valueOf(amount), Address.fromBase58(LitecoinNetwork.get(), "LZCofQ8vpu2VR42FW3LueB48ZnZZaQU8ao"));
        if (transactionValue - amount - Transaction.DEFAULT_TX_FEE.value > Transaction.MIN_NONDUST_OUTPUT.value) {
            tx.addOutput(Coin.valueOf(transactionValue - amount - Transaction.DEFAULT_TX_FEE.value),
                        Address.fromBase58(this.networkParameters, this.address));
        }

        ;
        // change output
        /*tx.addOutput(Coin.valueOf(input.getValue().value - Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.value-Coin.valueOf(amount).value),
                Address.fromBase58(TestNet3Params.get(), this.address));
        System.out.println("BEFORE THIS " + HEX.encode(tx.bitcoinSerialize()));
        */
        //tx.addInput(input.getHash(), 0, input.getScript());
        //tx.addInput(new TransactionOutput(TestNet3Params.get(), null, input.getScript().getProgram(),input.getScript().getProgram().length));
        //System.out.println("SETTING THIS SCRIPT " + HEX.encode(input.getScript().getProgram()).toString());
        //System.out.println("SERIALIZE" + HEX.encode(tx.unsafeBitcoinSerialize()));
        //System.out.println(HEX.encode(ScriptBuilder.createOutputScript(Address.fromBase58(TestNet3Params.get(), this.address)).getProgram()));
        setTransaction(tx);
        tx.getInputs().size();
        return tx;
    }

    /*public static String replaceAt(String input, String search, String replace, int start, int end) {
           return input.substring(0, start) +
                  input.substring(start, end).replace(search, replace) +
                  input.substring(end);
    }
    */

    public HashSet<UTXO> chooseUTXONaive(HashSet<UTXO> transactions, long amount) {
        long temp = 0;
        HashSet<UTXO> newSet = new HashSet<UTXO>();
        for (UTXO transaction : transactions) {
            long transactionValue = transaction.getValue().value;
            temp += transactionValue;
	        System.out.println("ADD THIS INPUT " + transaction.getHash().toString());
            newSet.add(transaction);
            if (temp >= amount + Transaction.DEFAULT_TX_FEE.value) break;
        }
        return newSet;
    }

    public String getHashForSig(Transaction transaction) {
        String tempTx = HEX.encode(transaction.bitcoinSerialize());
        String txForSig = tempTx.substring(0, 74);
        return "";
    }

    public Single<List<String>> sign(int size) {
       return Observable.range(0, size).concatMap(index -> getTestSignature(getTransaction()
        .hashForSignature(index, getTransaction().getInput(index.longValue()).getScriptSig().getProgram(), Transaction.SigHash.ALL, false)
                .toString())
        ).toList();
    }
   /* public void test(List<String> list) {
        Observable.fromIterable(list)
                .concatMap()
    }
*/  private Single<String> print(List<String> list) {
        System.out.println(list);
        return Single.just("");
    }

    private Single<String> formSignedTransaction(List<String> signatures) {
        String tx = HEX.encode(getTransaction().bitcoinSerialize());
        System.out.println("UNBUILDED " + tx);
        for (int i = 0;i<signatures.size(); i++) {
            String signature = signatures.get(i);
            final int firstIndex = tx.indexOf("0000001976");
            final int lastIndex = tx.substring(firstIndex).indexOf("ffffffff");
            tx = tx.substring(0, firstIndex) + "000000" + signature.toLowerCase() + tx.substring(firstIndex).substring(lastIndex, tx.length() - firstIndex);
            System.out.println("TEMP RESULT " + tx);
        }
       // getTransaction().getInput(s)
        /*for (String signature: signatures) {
            tx = tx.substring(0,76) + "000000" + signature.toLowerCase() + tx.substring(tx.indexOf("ffffffff"), tx.length());
            System.out.println("TEMP RESULT " + tx);
        }*/
        System.out.println("FINALLY " + tx);
        // System.out.println("SERIALIZED " + HEX.encode(tx.bitcoinSerialize()));
        return Single.just(tx);
    }

    public void createTransaction(String paymentAddress, long amount) {
        /*Transaction tx = new Transaction(NetworkParameters.testNet3());
        tx.addOutput(Coin.valueOf(amount), Address.fromBase58(NetworkParameters.testNet3(), paymentAddress));
        System.out.println("SERIALIZE" + HEX.encode(tx.bitcoinSerialize()));
        */
        getUTXO()
                .map(res -> parseUTXO(res.getData().getTxs()))
                .map(res -> chooseUTXONaive(res, amount))
                .map(res -> formTx(amount, paymentAddress, res))
                .flatMap(tx -> sign(tx.getInputs().size()))
                .flatMap(res -> formSignedTransaction(res))
                .doOnError(System.out::println)
                .flatMap(this::sendTx)
                /*.flatMap(tx -> Observable.range(0,tx.getInputs().size()).map(index  -> getTestSignature(
                        getTransaction().hashForSignature(
                                index,
                                getTransaction().getInput(index.longValue()).getScriptSig().getProgram(),
                                Transaction.SigHash.ALL,
                                false)
                                .toString()
                )))
                */
                /*.map(index -> getTestSignature(
                        getTransaction().hashForSignature(
                                index.blockingFirst().intValue(),
                                getTransaction().getInput(index.blockingFirst().longValue()).getScriptSig().getProgram(),
                                Transaction.SigHash.ALL,
                                false)
                                .toString()
                        )
                ).toObservable().toList().doOnSuccess(res -> System.out.println(res))
               /* .map(tx -> Observable.range(0, tx.getInputs().size())
                                      .flatMap(index -> getTestSignature(getTransaction().hashForSignature(index,
                                                getTransaction().getInput(index).getScriptSig().getProgram(),
                                                Transaction.SigHash.ALL,
                                                false).toString()))
                                      .toList())

               // .flatMap(index -> getTestSignature(getTransaction().hashForSignature(index,
                                                   // getTransaction().getInput()))

                       /* .flatMapIterable(index ->
                            getTestSignature(getTransaction().hashForSignature(index,
                                                                getTransaction().getInput(index).getScriptSig().getProgram(),
                                                                Transaction.SigHash.ALL,
                                                                    false)
                                             .toString()
                        )
                        ))
                        */
                //.flatMap(res -> getTestSignature(res.hashForSignature(0, res.getInput(0).getScriptSig().getProgram(), Transaction.SigHash.ALL, false).toString()))
                /*
                .map(res -> {
                    String transaction = HEX.encode(getTransaction().bitcoinSerialize());
                   // System.out.println(HEX.encode(getTransaction().getInput(0).getScriptSig().getPubKeyHash()));

                    //dont know why bitcoinj add`s additional 2 bytes in beginning
                    String result = transaction.substring(0, 74) + "00000000" + res.toString().toLowerCase() + transaction.substring(transaction.indexOf("ffffffff"), transaction.length());
                    return result;
                })
                .flatMap(res -> sendTx(res))
                */
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
