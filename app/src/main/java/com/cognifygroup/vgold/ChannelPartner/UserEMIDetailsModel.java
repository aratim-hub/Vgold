package com.cognifygroup.vgold.ChannelPartner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shivraj on 7/26/18.
 */

public class UserEMIDetailsModel implements Serializable{

    @Expose
    @SerializedName("Data")
    private ArrayList<Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<UserEMIDetailsModel.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<UserEMIDetailsModel.Data> data) {
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
        @SerializedName("gold")
        private String gold;
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
        @SerializedName("added_date")
        private String added_date;
        @Expose
        @SerializedName("last_paid_date")
        private String last_paid_date;
        @Expose
        @SerializedName("tennure")
        private String tennure;
        @Expose
        @SerializedName("booking_charge")
        private String booking_charge;
        @Expose
        @SerializedName("upcoming_installment_no")
        private String upcoming_installment_no;

        public String getGold_booking_id() {
            return gold_booking_id;
        }

        public void setGold_booking_id(String gold_booking_id) {
            this.gold_booking_id = gold_booking_id;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
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

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
        }

        public String getLast_paid_date() {
            return last_paid_date;
        }

        public void setLast_paid_date(String last_paid_date) {
            this.last_paid_date = last_paid_date;
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

        public String getUpcoming_installment_no() {
            return upcoming_installment_no;
        }

        public void setUpcoming_installment_no(String upcoming_installment_no) {
            this.upcoming_installment_no = upcoming_installment_no;
        }
    }
}
