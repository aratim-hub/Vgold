package com.cognifygroup.vgold.getAllTransactionForGold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivraj on 6/15/18.
 */

public class GetAllTransactionGoldModel {


    @Expose
    @SerializedName("Data")
    private ArrayList<Data> Data;
    @Expose
    @SerializedName("gold_Balance")
    private String gold_Balance;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<Data> getData() {
        return Data;
    }

    public void setData(ArrayList<Data> Data) {
        this.Data = Data;
    }

    public String getGold_Balance() {
        return gold_Balance;
    }

    public void setGold_Balance(String gold_Balance) {
        this.gold_Balance = gold_Balance;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("transaction_date")
        private String transaction_date;
        @Expose
        @SerializedName("admin_status")
        private String admin_status;
        @Expose
        @SerializedName("cheque_no")
        private String cheque_no;
        @Expose
        @SerializedName("bank_details")
        private String bank_details;
        @Expose
        @SerializedName("online_transaction_id")
        private String online_transaction_id;
        @Expose
        @SerializedName("payment_method")
        private String payment_method;
        @Expose
        @SerializedName("transafer_to")
        private String transafer_to;
        @Expose
        @SerializedName("received_from")
        private String received_from;
        @Expose
        @SerializedName("gold")
        private String gold;
        @Expose
        @SerializedName("transaction_id")
        private String transaction_id;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTransaction_date() {
            return transaction_date;
        }

        public void setTransaction_date(String transaction_date) {
            this.transaction_date = transaction_date;
        }

        public String getAdmin_status() {
            return admin_status;
        }

        public void setAdmin_status(String admin_status) {
            this.admin_status = admin_status;
        }

        public String getCheque_no() {
            return cheque_no;
        }

        public void setCheque_no(String cheque_no) {
            this.cheque_no = cheque_no;
        }

        public String getBank_details() {
            return bank_details;
        }

        public void setBank_details(String bank_details) {
            this.bank_details = bank_details;
        }

        public String getOnline_transaction_id() {
            return online_transaction_id;
        }

        public void setOnline_transaction_id(String online_transaction_id) {
            this.online_transaction_id = online_transaction_id;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getTransafer_to() {
            return transafer_to;
        }

        public void setTransafer_to(String transafer_to) {
            this.transafer_to = transafer_to;
        }

        public String getReceived_from() {
            return received_from;
        }

        public void setReceived_from(String received_from) {
            this.received_from = received_from;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }
    }
}
