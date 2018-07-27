package com.example.denis.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.denis.CryptocurrencyAPI.Bitcoin;
import com.example.denis.CryptocurrencyAPI.Ethereum;

import org.bitcoinj.core.Transaction;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import rx.schedulers.Schedulers;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe;

public class MainActivity extends AppCompatActivity {
    private  Bitcoin bitcoin;
    private Ethereum ethereum;

    public void addressETHClick (android.view.View view) {
        ethereum.initAddresss()
                .map(address -> ethereum.setAddress(address))
                //.doOnNext(address -> ethereum.setAddress(address))
                //.doOnNext(res -> ethereum.balance())
                .flatMap(res -> ethereum.balance())
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(System.out::println)
                .subscribe(ethGetBalance -> System.out.println(ethGetBalance.getBalance()));
    }

    public void signETHClick (android.view.View view) {
        ethereum.createTransaction("0x619B30BE614ce453035058736cd2B83c34373Ddd", "0.00002")
                .map(rawTransaction -> Hash.sha3(TransactionEncoder.encode(rawTransaction).toString()))
                .flatMap(hash -> ethereum.signTx(hash))
                .flatMap(signature -> {
                    System.out.println("SIGNATURE TO STRIGN " + signature.toString());
                    byte [] signatureBytes = Numeric.hexStringToByteArray(signature.toString());
                    byte [] num = Numeric.hexStringToByteArray(signature.toString());
                    Sign.SignatureData signatureData = new Sign.SignatureData(signatureBytes[64],
                            Arrays.copyOfRange(signatureBytes,0,32), Arrays.copyOfRange(signatureBytes,32,64));
                    List<RlpType> values = ethereum.asRlpValues(ethereum.getRawTransaction(), TransactionEncoder.createEip155SignatureData(signatureData, (byte) 3));
                    RlpList rlpList = new RlpList(values);
                    System.out.println("ENCODED TRANSACTION " + Hex.toHexString(RlpEncoder.encode(rlpList)));
                    return ethereum.sendETHTransaction(Hex.toHexString(RlpEncoder.encode(rlpList)));
                 })
                //.flatMap(txHex -> ethereum.sendETHTransaction(txHex.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
    }
    public void signClickHandler (android.view.View view) {
       /* bitcoin.getUTXO()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(res -> bitcoin.parseUTXO(res.getData().getTxs()))
                .map(res -> bitcoin.formTx(12000, "mvLpZMU3cavwLbUMKocpSWcjP9LF62BQMd", res.iterator().next()))
                .flatMap(res -> bitcoin.getTestSignature(res.hashForSignature(0, res.getInput(0).getScriptBytes(), Transaction.SigHash.ALL, false).toString()));
                */
        bitcoin.createTransaction("mvLpZMU3cavwLbUMKocpSWcjP9LF62BQMd", 10);
    }
    public void addressClickHandler(android.view.View view) {
        TextView text = ((TextView)findViewById(R.id.nested_contain_main).findViewById(R.id.textId));

        try {
            //text.setText("Balance called");
            bitcoin.initAddresss()
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnSuccess((res) -> bitcoin.setAddress(res))
                    .flatMap((res) -> bitcoin.getBalanceAsync())
                    .map(result -> result.getData().getConfirmed_balance())
                    //.flatMap(res -> bitcoin.getBalanceAsync())
                    .subscribe(result -> {
                        System.out.println("GOT CONFIRMED BALANCE " + result);
                        System.out.println("ADDRESS CHECK " + bitcoin.getAddress_Check());
                    });
        } catch (Exception e) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.bitcoin = new Bitcoin();
        this.ethereum = new Ethereum();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // needs to be implemented with subscribe destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
