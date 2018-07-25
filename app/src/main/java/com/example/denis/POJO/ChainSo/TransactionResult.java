package com.example.denis.POJO.ChainSo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionResult {
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("txid")
    @Expose
    private String txid;

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}
