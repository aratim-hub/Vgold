package com.cognifygroup.vgold.GetGoldTransactionHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivraj on 6/18/18.
 */

public class GetGoldTransactionHistoryModel {


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
        @SerializedName("transaction_date")
        private String transaction_date;
        @Expose
        @SerializedName("admin_status")
        private String admin_status;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("cheque_no")
        private String cheque_no;
        @Expose
        @SerializedName("bank_details")
        private String bank_details;
        @Expose
        @SerializedName("payment_method")
        private String payment_method;
        @Expose
        @SerializedName("transaction_id")
        private String transaction_id;
        @Expose
        @SerializedName("remaining_amount")
        private String remaining_amount;
        @Expose
        @SerializedName("entry_status")
        private String entry_status;
        @Expose
        @SerializedName("installment")
        private String installment;
        @Expose
        @SerializedName("period")
        private String period;
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("next_due_date")
        private String next_due_date;

        public String getNext_due_date() {
            return next_due_date;
        }

        public void setNext_due_date(String next_due_date) {
            this.next_due_date = next_due_date;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getRemaining_amount() {
            return remaining_amount;
        }

        public void setRemaining_amount(String remaining_amount) {
            this.remaining_amount = remaining_amount;
        }

        public String getEntry_status() {
            return entry_status;
        }

        public void setEntry_status(String entry_status) {
            this.entry_status = entry_status;
        }

        public String getInstallment() {
            return installment;
        }

        public void setInstallment(String installment) {
            this.installment = installment;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
