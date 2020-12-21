package com.cognifygroup.vgold.fetchDownPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 6/15/18.
 */

public class FetchDownPaymentModel {


    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("monthly_installment")
    private String monthly_installment;

    public String getMonthly_installment() {
        return monthly_installment;
    }

    public void setMonthly_installment(String monthly_installment) {
        this.monthly_installment = monthly_installment;
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
}
