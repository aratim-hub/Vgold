package com.cognifygroup.vgold.getBankDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivraj on 9/6/18.
 */

public class GetBankModel {


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
        @SerializedName("bank_name")
        private String bank_name;
        @Expose
        @SerializedName("bank_id")
        private String bank_id;
        @Expose
        @SerializedName("acc_no")
        private String acc_no;

        public String getAcc_no() {
            return acc_no;
        }

        public void setAcc_no(String acc_no) {
            this.acc_no = acc_no;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_id() {
            return bank_id;
        }

        public void setBank_id(String bank_id) {
            this.bank_id = bank_id;
        }


        @Override
        public String toString() {
            return bank_name+"( "+acc_no+" )";
        }
    }

}
