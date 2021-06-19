package com.cognifygroup.vgold.getTodaysGoldRate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 6/22/18.
 */

public class GetTotalGoldGainModel {

    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("Data")
    private Data Data;

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

    public GetTotalGoldGainModel.Data getData() {
        return Data;
    }

    public void setData(GetTotalGoldGainModel.Data data) {
        Data = data;
    }

    public class Data {
        @Expose
        @SerializedName("gain")
        private String gain;

        public String getGain() {
            return gain;
        }

        public void setGain(String gain) {
            this.gain = gain;
        }
    }
}
