package com.cognifygroup.vgold.ChannelPartner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shivraj on 7/26/18.
 */

public class UserEMIStatusDetailsModel implements Serializable{

    @Expose
    @SerializedName("Data")
    private ArrayList<Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<UserEMIStatusDetailsModel.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<UserEMIStatusDetailsModel.Data> data) {
        Data = data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data implements Serializable {

        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("period")
        private String period;
        @Expose
        @SerializedName("installment")
        private String installment;
        @Expose
        @SerializedName("entry_status")
        private String entry_status;
        @Expose
        @SerializedName("remaining_amount")
        private String remaining_amount;
        @Expose
        @SerializedName("transaction_id")
        private String transaction_id;
        @Expose
        @SerializedName("payment_method")
        private String payment_method;
        @Expose
        @SerializedName("bank_details")
        private String bank_details;
        @Expose
        @SerializedName("cheque_no")
        private String cheque_no;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("admin_status")
        private String admin_status;
        @Expose
        @SerializedName("transaction_date")
        private String transaction_date;
        @Expose
        @SerializedName("is_installment")
        private String is_installment;
        @Expose
        @SerializedName("is_paid")
        private String is_paid;
        @Expose
        @SerializedName("next_due_date")
        private String next_due_date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getInstallment() {
            return installment;
        }

        public void setInstallment(String installment) {
            this.installment = installment;
        }

        public String getEntry_status() {
            return entry_status;
        }

        public void setEntry_status(String entry_status) {
            this.entry_status = entry_status;
        }

        public String getRemaining_amount() {
            return remaining_amount;
        }

        public void setRemaining_amount(String remaining_amount) {
            this.remaining_amount = remaining_amount;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getBank_details() {
            return bank_details;
        }

        public void setBank_details(String bank_details) {
            this.bank_details = bank_details;
        }

        public String getCheque_no() {
            return cheque_no;
        }

        public void setCheque_no(String cheque_no) {
            this.cheque_no = cheque_no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAdmin_status() {
            return admin_status;
        }

        public void setAdmin_status(String admin_status) {
            this.admin_status = admin_status;
        }

        public String getTransaction_date() {
            return transaction_date;
        }

        public void setTransaction_date(String transaction_date) {
            this.transaction_date = transaction_date;
        }

        public String getIs_installment() {
            return is_installment;
        }

        public void setIs_installment(String is_installment) {
            this.is_installment = is_installment;
        }

        public String getIs_paid() {
            return is_paid;
        }

        public void setIs_paid(String is_paid) {
            this.is_paid = is_paid;
        }

        public String getNext_due_date() {
            return next_due_date;
        }

        public void setNext_due_date(String next_due_date) {
            this.next_due_date = next_due_date;
        }
    }
}
