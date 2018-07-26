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

import org.bitcoinj.core.Transaction;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    private  Bitcoin bitcoin;
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
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
