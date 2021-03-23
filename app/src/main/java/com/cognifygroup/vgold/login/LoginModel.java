package com.cognifygroup.vgold.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER1 on 1/15/2018.
 */

public class LoginModel {


    @Expose
    @SerializedName("data")
    private ArrayList<Data> data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
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
        @SerializedName("Qrcode")
        private String Qrcode;
        @Expose
        @SerializedName("Gender")
        private String Gender;
        @Expose
        @SerializedName("Date_of_birth")
        private String Date_of_birth;
        @Expose
        @SerializedName("User_role")
        private String User_role;
        @Expose
        @SerializedName("Mobile_no")
        private String Mobile_no;
        @Expose
        @SerializedName("Email")
        private String Email;
        @Expose
        @SerializedName("Last_Name")
        private String Last_Name;
        @Expose
        @SerializedName("Middle_Name")
        private String Middle_Name;
        @Expose
        @SerializedName("First_Name")
        private String First_Name;
        @Expose
        @SerializedName("User_ID")
        private String User_ID;
        @Expose
        @SerializedName("profile_photo")
        private String profile_photo;
        @Expose
        @SerializedName("pan_no")
        private String pan_no;
        @Expose
        @SerializedName("Address")
        private String address;
        @Expose
        @SerializedName("City")
        private String City;
        @Expose
        @SerializedName("State")
        private String State;
        @Expose
        @SerializedName("validity_date")
        private String validity_date;
        @Expose
        @SerializedName("Version_code")
        private String Version_code;

        public String getVersion_code() {
            return Version_code;
        }

        public void setVersion_code(String version_code) {
            Version_code = version_code;
        }

        public String getValidity_date() {
            return validity_date;
        }

        public void setValidity_date(String validity_date) {
            this.validity_date = validity_date;
        }

        public String getProfile_photo() {
            return profile_photo;
        }

        public void setProfile_photo(String profile_photo) {
            this.profile_photo = profile_photo;
        }

        public String getPan_no() {
            return pan_no;
        }

        public void setPan_no(String pan_no) {
            this.pan_no = pan_no;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getQrcode() {
            return Qrcode;
        }

        public void setQrcode(String Qrcode) {
            this.Qrcode = Qrcode;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getDate_of_birth() {
            return Date_of_birth;
        }

        public void setDate_of_birth(String Date_of_birth) {
            this.Date_of_birth = Date_of_birth;
        }

        public String getUser_role() {
            return User_role;
        }

        public void setUser_role(String User_role) {
            this.User_role = User_role;
        }

        public String getMobile_no() {
            return Mobile_no;
        }

        public void setMobile_no(String Mobile_no) {
            this.Mobile_no = Mobile_no;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getLast_Name() {
            return Last_Name;
        }

        public void setLast_Name(String Last_Name) {
            this.Last_Name = Last_Name;
        }

        public String getMiddle_Name() {
            return Middle_Name;
        }

        public void setMiddle_Name(String Middle_Name) {
            this.Middle_Name = Middle_Name;
        }

        public String getFirst_Name() {
            return First_Name;
        }

        public void setFirst_Name(String First_Name) {
            this.First_Name = First_Name;
        }

        public String getUser_ID() {
            return User_ID;
        }

        public void setUser_ID(String User_ID) {
            this.User_ID = User_ID;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }
    }
}
