package com.example.denis.POJO.ChainSo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LastTx {
    public class Data {

        @SerializedName("network")
        @Expose
        private String network;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("txs")
        @Expose
        private List<Tx> txs = null;

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

        public List<Tx> getTxs() {
            return txs;
        }

        public void setTxs(List<Tx> txs) {
            this.txs = txs;
        }
    }
    public class Tx {

            @SerializedName("txid")
            @Expose
            private String txid;
            @SerializedName("output_no")
            @Expose
            private Integer output_no;
            @SerializedName("script_asm")
            @Expose
            private String script_asm;
            @SerializedName("script_hex")
            @Expose
            private String script_hex;
            @SerializedName("value")
            @Expose
            private String value;
            @SerializedName("confirmations")
            @Expose
            private Integer confirmations;
            @SerializedName("time")
            @Expose
            private Integer time;

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }

            public Integer getOutput_no() {
                return output_no;
            }

            public void setOutput_no(Integer output_no) {
                this.output_no = output_no;
            }

            public String getScript_asm() {
                return script_asm;
            }

            public void setScript_asm(String script_asm) {
                this.script_asm = script_asm;
            }

            public String getScript_hex() {
                return script_hex;
            }

            public void setScript_hex(String script_hex) {
                this.script_hex = script_hex;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Integer getConfirmations() {
                return confirmations;
            }

            public void setConfirmations(Integer confirmations) {
                this.confirmations = confirmations;
            }

            public Integer getTime() {
                return time;
            }

            public void setTime(Integer time) {
                this.time = time;
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

