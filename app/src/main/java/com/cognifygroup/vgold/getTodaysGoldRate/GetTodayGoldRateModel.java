package com.cognifygroup.vgold.getTodaysGoldRate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 6/22/18.
 */

public class GetTodayGoldRateModel {


    @Expose
    @SerializedName("Gold_purchase_rate")
    private String Gold_purchase_rate;
    @Expose
    @SerializedName("Gold_purchase_rate_with_gst")
    private String Gold_purchase_rate_with_gst;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    public String getGold_purchase_rate() {
        return Gold_purchase_rate;
    }

    public void setGold_purchase_rate(String Gold_purchase_rate) {
        this.Gold_purchase_rate = Gold_purchase_rate;
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

    public String getGold_purchase_rate_with_gst() {
        return Gold_purchase_rate_with_gst;
    }

    public void setGold_purchase_rate_with_gst(String gold_purchase_rate_with_gst) {
        Gold_purchase_rate_with_gst = gold_purchase_rate_with_gst;
    }
}
