package com.cognifygroup.vgold.vendorForDeeposite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivraj on 9/6/18.
 */

public class VendorForDepositeModel {


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
        @SerializedName("firm_name")
        private String firm_name;
        @Expose
        @SerializedName("vendor_id")
        private String vendor_id;

        public String getFirm_name() {
            return firm_name;
        }

        public void setFirm_name(String firm_name) {
            this.firm_name = firm_name;
        }

        public String getVendor_id() {
            return vendor_id;
        }

        public void setVendor_id(String vendor_id) {
            this.vendor_id = vendor_id;
        }

        @Override
        public String toString() {
            return firm_name;
        }
    }

}
