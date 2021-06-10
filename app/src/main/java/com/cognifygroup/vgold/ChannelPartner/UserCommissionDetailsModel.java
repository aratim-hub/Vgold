package com.cognifygroup.vgold.ChannelPartner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shivraj on 7/26/18.
 */

public class UserCommissionDetailsModel implements Serializable{

    @Expose
    @SerializedName("Data")
    private ArrayList<Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<UserCommissionDetailsModel.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<UserCommissionDetailsModel.Data> data) {
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
        @SerializedName("booking_id")
        private String booking_id;
        @Expose
        @SerializedName("commission_amount")
        private String commission_amount;
        @Expose
        @SerializedName("gold")
        private String gold;
        @Expose
        @SerializedName("created_date")
        private String created_date;


        public String getBooking_id() {
            return booking_id;
        }

        public void setBooking_id(String booking_id) {
            this.booking_id = booking_id;
        }

        public String getCommission_amount() {
            return commission_amount;
        }

        public void setCommission_amount(String commission_amount) {
            this.commission_amount = commission_amount;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
