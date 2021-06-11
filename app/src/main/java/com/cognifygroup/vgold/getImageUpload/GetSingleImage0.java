package com.cognifygroup.vgold.getImageUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 8/22/18.
 */

public class GetSingleImage0 {

    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("path")
    private String path;
    @Expose
    @SerializedName("data")
    private Data data;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @Expose
        @SerializedName("identity_photo")
        private String identity_photo;
        @Expose
        @SerializedName("address_photo")
        private String address_photo;
        @Expose
        @SerializedName("address_photo_back")
        private String address_photo_back;

        public String getIdentity_photo() {
            return identity_photo;
        }

        public void setIdentity_photo(String identity_photo) {
            this.identity_photo = identity_photo;
        }

        public String getAddress_photo() {
            return address_photo;
        }

        public void setAddress_photo(String address_photo) {
            this.address_photo = address_photo;
        }

        public String getAddress_photo_back() {
            return address_photo_back;
        }

        public void setAddress_photo_back(String address_photo_back) {
            this.address_photo_back = address_photo_back;
        }
    }

}
