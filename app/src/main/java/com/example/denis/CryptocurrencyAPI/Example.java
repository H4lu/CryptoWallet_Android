package com.example.denis.CryptocurrencyAPI;

import com.example.denis.POJO.ChainSo.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Result data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
        this.data = data;
    }

}