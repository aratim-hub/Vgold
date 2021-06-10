package com.cognifygroup.vgold.payInstallment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 6/15/18.
 */

public class PayInstallmentModel {


    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("data")
    private Data data;

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

    public class Data {

        @Expose
        @SerializedName("today_sale_rate")
        private String today_sale_rate;
        @Expose
        @SerializedName("amount_to_pay")
        private String amount_to_pay;
        @Expose
        @SerializedName("amount_to_pay_gst")
        private String amount_to_pay_gst;
        @Expose
        @SerializedName("gst_per")
        private String gst_per;
        @Expose
        @SerializedName("gold_in_wallet")
        private String gold_in_wallet;
        @Expose
        @SerializedName("gold_reduce")
        private String gold_reduce;
        @Expose
        @SerializedName("reduced_gold_in_wallet")
        private String reduced_gold_in_wallet;

        public String getToday_sale_rate() {
            return today_sale_rate;
        }

        public void setToday_sale_rate(String today_sale_rate) {
            this.today_sale_rate = today_sale_rate;
        }

        public String getAmount_to_pay() {
            return amount_to_pay;
        }

        public void setAmount_to_pay(String amount_to_pay) {
            this.amount_to_pay = amount_to_pay;
        }

        public String getAmount_to_pay_gst() {
            return amount_to_pay_gst;
        }

        public void setAmount_to_pay_gst(String amount_to_pay_gst) {
            this.amount_to_pay_gst = amount_to_pay_gst;
        }

        public String getGst_per() {
            return gst_per;
        }

        public void setGst_per(String gst_per) {
            this.gst_per = gst_per;
        }

        public String getGold_in_wallet() {
            return gold_in_wallet;
        }

        public void setGold_in_wallet(String gold_in_wallet) {
            this.gold_in_wallet = gold_in_wallet;
        }

        public String getGold_reduce() {
            return gold_reduce;
        }

        public void setGold_reduce(String gold_reduce) {
            this.gold_reduce = gold_reduce;
        }

        public String getReduced_gold_in_wallet() {
            return reduced_gold_in_wallet;
        }

        public void setReduced_gold_in_wallet(String reduced_gold_in_wallet) {
            this.reduced_gold_in_wallet = reduced_gold_in_wallet;
        }
    }
}
