package com.cognifygroup.vgold.ChannelPartner;

import com.cognifygroup.vgold.GetGoldDepositeHistory.GetGoldDepositeHistoryModel;
import com.cognifygroup.vgold.plan.PlanModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shivraj on 7/26/18.
 */

public class UserDetailsModel implements Serializable{

    @Expose
    @SerializedName("data")
    private ArrayList<Data> Data;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public ArrayList<UserDetailsModel.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<UserDetailsModel.Data> data) {
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
        @SerializedName("uid")
        private String uid;
        @Expose
        @SerializedName("uname")
        private String uname;
        @Expose
        @SerializedName("umobile")
        private String umobile;
        @Expose
        @SerializedName("uemail")
        private String uemail;
        @Expose
        @SerializedName("urole")
        private String urole;
        @Expose
        @SerializedName("total_gold_in_account")
        private String total_gold_in_account;
        @Expose
        @SerializedName("upic")
        private String upic;
        @Expose
        @SerializedName("total_commission_created")
        private String total_commission_created;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getUmobile() {
            return umobile;
        }

        public void setUmobile(String umobile) {
            this.umobile = umobile;
        }

        public String getUemail() {
            return uemail;
        }

        public void setUemail(String uemail) {
            this.uemail = uemail;
        }

        public String getUrole() {
            return urole;
        }

        public void setUrole(String urole) {
            this.urole = urole;
        }

        public String getTotal_gold_in_account() {
            return total_gold_in_account;
        }

        public void setTotal_gold_in_account(String total_gold_in_account) {
            this.total_gold_in_account = total_gold_in_account;
        }

        public String getUpic() {
            return upic;
        }

        public void setUpic(String upic) {
            this.upic = upic;
        }

        public String getTotal_commission_created() {
            return total_commission_created;
        }

        public void setTotal_commission_created(String total_commission_created) {
            this.total_commission_created = total_commission_created;
        }
    }
}
