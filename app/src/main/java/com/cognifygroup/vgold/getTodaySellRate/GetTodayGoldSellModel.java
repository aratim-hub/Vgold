package com.cognifygroup.vgold.getTodaySellRate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 6/22/18.
 */

public class GetTodayGoldSellModel {


    @Expose
    @SerializedName("Gold_sale_rate")
    private String Gold_sale_rate;
    @Expose
    @SerializedName("Message")
    private String Message;

    public String getGold_sale_rate() {
        return Gold_sale_rate;
    }

    public void setGold_sale_rate(String gold_sale_rate) {
        Gold_sale_rate = gold_sale_rate;
    }

    @Expose
    @SerializedName("status")
    private String status;



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
}
