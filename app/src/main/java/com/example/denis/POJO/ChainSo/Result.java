package com.example.denis.POJO.ChainSo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result {
    public class Data {

        @SerializedName("network")
        @Expose
        private String network;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("confirmed_balance")
        @Expose
        private String confirmed_balance;
        @SerializedName("unconfirmed_balance")
        @Expose
        private String unconfirmed_balance;

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getConfirmed_balance() {
            return confirmed_balance;
        }

        public void setConfirmed_balance(String confirmed_balance) {
            this.confirmed_balance = confirmed_balance;
        }

        public String getUnconfirmed_balance() {
            return unconfirmed_balance;
        }

        public void setUnconfirmed_balance(String unconfirmed_balance) {
            this.unconfirmed_balance = unconfirmed_balance;
        }

    }
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}