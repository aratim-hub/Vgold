package com.cognifygroup.vgold.Loan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 6/15/18.
 */

public class LoanModel {


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
        @SerializedName("amount_in_account")
        private String amount_in_account;
        @Expose
        @SerializedName("loan_amount")
        private String loan_amount;
        @Expose
        @SerializedName("is_eligible")
        private String is_eligible;

        public String getAmount_in_account() {
            return amount_in_account;
        }

        public void setAmount_in_account(String amount_in_account) {
            this.amount_in_account = amount_in_account;
        }

        public String getLoan_amount() {
            return loan_amount;
        }

        public void setLoan_amount(String loan_amount) {
            this.loan_amount = loan_amount;
        }

        public String getIs_eligible() {
            return is_eligible;
        }

        public void setIs_eligible(String is_eligible) {
            this.is_eligible = is_eligible;
        }
    }
}
