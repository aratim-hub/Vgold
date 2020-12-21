package com.cognifygroup.vgold.GetGoldDepositeHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivraj on 6/16/18.
 */

public class GetGoldDepositeHistoryModel {


    @Expose
    @SerializedName("Data")
    private ArrayList<Data> Data;
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
        @SerializedName("account_status")
        private String account_status;
        @Expose
        @SerializedName("transaction_id")
        private String transaction_id;
        @Expose
        @SerializedName("admin_status")
        private String admin_status;
        @Expose
        @SerializedName("gold_quality")
        private String gold_quality;
        @Expose
        @SerializedName("amount")
        private String amount;
        @Expose
        @SerializedName("vendor_status")
        private String vendor_status;
        @Expose
        @SerializedName("added_date")
        private String added_date;
        @Expose
        @SerializedName("bank_guarantee")
        private String bank_guarantee;
        @Expose
        @SerializedName("maturity_weight")
        private String maturity_weight;
        @Expose
        @SerializedName("rate")
        private String rate;
        @Expose
        @SerializedName("tennure")
        private String tennure;
        @Expose
        @SerializedName("gold")
        private String gold;
        @Expose
        @SerializedName("vendor_id")
        private String vendor_id;
        @Expose
        @SerializedName("gold_deposite_id")
        private String gold_deposite_id;

        public String getAccount_status() {
            return account_status;
        }

        public void setAccount_status(String account_status) {
            this.account_status = account_status;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getAdmin_status() {
            return admin_status;
        }

        public void setAdmin_status(String admin_status) {
            this.admin_status = admin_status;
        }

        public String getGold_quality() {
            return gold_quality;
        }

        public void setGold_quality(String gold_quality) {
            this.gold_quality = gold_quality;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getVendor_status() {
            return vendor_status;
        }

        public void setVendor_status(String vendor_status) {
            this.vendor_status = vendor_status;
        }

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
        }

        public String getBank_guarantee() {
            return bank_guarantee;
        }

        public void setBank_guarantee(String bank_guarantee) {
            this.bank_guarantee = bank_guarantee;
        }

        public String getMaturity_weight() {
            return maturity_weight;
        }

        public void setMaturity_weight(String maturity_weight) {
            this.maturity_weight = maturity_weight;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getTennure() {
            return tennure;
        }

        public void setTennure(String tennure) {
            this.tennure = tennure;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getVendor_id() {
            return vendor_id;
        }

        public void setVendor_id(String vendor_id) {
            this.vendor_id = vendor_id;
        }

        public String getGold_deposite_id() {
            return gold_deposite_id;
        }

        public void setGold_deposite_id(String gold_deposite_id) {
            this.gold_deposite_id = gold_deposite_id;
        }
    }
}
