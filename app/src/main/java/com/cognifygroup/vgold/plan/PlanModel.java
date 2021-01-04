package com.cognifygroup.vgold.plan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivraj on 6/15/18.
 */

public class PlanModel {


    @Expose
    @SerializedName("Data")
    private Data Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public PlanModel.Data getData() {
        return Data;
    }

    public void setData(PlanModel.Data data) {
        Data = data;
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
        @SerializedName("quantity")
        private String quantity;
        @Expose
        @SerializedName("total_amount")
        private String tot_amt;
        @Expose
        @SerializedName("booking_amount")
        private String booking_amt;
        @Expose
        @SerializedName("booking_charge")
        private String booking_chrg;
        @Expose
        @SerializedName("have_to_pay")
        private String payable_amt;
        @Expose
        @SerializedName("remaing_amount")
        private String remain_amt;


        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getTot_amt() {
            return tot_amt;
        }

        public void setTot_amt(String tot_amt) {
            this.tot_amt = tot_amt;
        }

        public String getBooking_amt() {
            return booking_amt;
        }

        public void setBooking_amt(String booking_amt) {
            this.booking_amt = booking_amt;
        }

        public String getBooking_chrg() {
            return booking_chrg;
        }

        public void setBooking_chrg(String booking_chrg) {
            this.booking_chrg = booking_chrg;
        }

        public String getPayable_amt() {
            return payable_amt;
        }

        public void setPayable_amt(String payable_amt) {
            this.payable_amt = payable_amt;
        }

        public String getRemain_amt() {
            return remain_amt;
        }

        public void setRemain_amt(String remain_amt) {
            this.remain_amt = remain_amt;
        }
    }
}
