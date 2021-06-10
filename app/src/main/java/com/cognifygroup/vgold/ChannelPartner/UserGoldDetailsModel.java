package com.cognifygroup.vgold.ChannelPartner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shivraj on 7/26/18.
 */

public class UserGoldDetailsModel implements Serializable{

    @Expose
    @SerializedName("Data")
    private ArrayList<Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<UserGoldDetailsModel.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<UserGoldDetailsModel.Data> data) {
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
        @SerializedName("gold_booking_id")
        private String gold_booking_id;
        @Expose
        @SerializedName("rate")
        private String rate;
        @Expose
        @SerializedName("gold")
        private String gold;
        @Expose
        @SerializedName("tennure")
        private String tennure;
        @Expose
        @SerializedName("booking_charge")
        private String booking_charge;
        @Expose
        @SerializedName("promo_code")
        private String promo_code;
        @Expose
        @SerializedName("booking_amount")
        private String booking_amount;
        @Expose
        @SerializedName("down_payment")
        private String down_payment;
        @Expose
        @SerializedName("monthly_installment")
        private String monthly_installment;
        @Expose
        @SerializedName("admin_status")
        private String admin_status;
        @Expose
        @SerializedName("added_date")
        private String added_date;
        @Expose
        @SerializedName("account_status")
        private String account_status;
        @Expose
        @SerializedName("commission")
        private String commission;
        @Expose
        @SerializedName("totalPaidInstallment")
        private String totalPaidInstallment;

        public String getGold_booking_id() {
            return gold_booking_id;
        }

        public void setGold_booking_id(String gold_booking_id) {
            this.gold_booking_id = gold_booking_id;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getTennure() {
            return tennure;
        }

        public void setTennure(String tennure) {
            this.tennure = tennure;
        }

        public String getBooking_charge() {
            return booking_charge;
        }

        public void setBooking_charge(String booking_charge) {
            this.booking_charge = booking_charge;
        }

        public String getPromo_code() {
            return promo_code;
        }

        public void setPromo_code(String promo_code) {
            this.promo_code = promo_code;
        }

        public String getBooking_amount() {
            return booking_amount;
        }

        public void setBooking_amount(String booking_amount) {
            this.booking_amount = booking_amount;
        }

        public String getDown_payment() {
            return down_payment;
        }

        public void setDown_payment(String down_payment) {
            this.down_payment = down_payment;
        }

        public String getMonthly_installment() {
            return monthly_installment;
        }

        public void setMonthly_installment(String monthly_installment) {
            this.monthly_installment = monthly_installment;
        }

        public String getAdmin_status() {
            return admin_status;
        }

        public void setAdmin_status(String admin_status) {
            this.admin_status = admin_status;
        }

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
        }

        public String getAccount_status() {
            return account_status;
        }

        public void setAccount_status(String account_status) {
            this.account_status = account_status;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getTotalPaidInstallment() {
            return totalPaidInstallment;
        }

        public void setTotalPaidInstallment(String totalPaidInstallment) {
            this.totalPaidInstallment = totalPaidInstallment;
        }
    }
}
