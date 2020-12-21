package com.cognifygroup.vgold.getVendorOffer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivraj on 6/16/18.
 */

public class VendorOfferModel {


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
        @SerializedName("vendor_id")
        private String vendor_id;
        @Expose
        @SerializedName("logo_path")
        private String logo_path;
        @Expose
        @SerializedName("letter_path")
        private String letter_path;
        @Expose
        @SerializedName("advertisement_path")
        private String advertisement_path;

        public String getAdvertisement_path() {
            return advertisement_path;
        }

        public void setAdvertisement_path(String advertisement_path) {
            this.advertisement_path = advertisement_path;
        }

        public String getVendor_id() {
            return vendor_id;
        }

        public void setVendor_id(String vendor_id) {
            this.vendor_id = vendor_id;
        }

        public String getLogo_path() {
            return logo_path;
        }

        public void setLogo_path(String logo_path) {
            this.logo_path = logo_path;
        }

        public String getLetter_path() {
            return letter_path;
        }

        public void setLetter_path(String letter_path) {
            this.letter_path = letter_path;
        }
    }
}
