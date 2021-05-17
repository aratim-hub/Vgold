package com.cognifygroup.vgold.GoldBooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivraj on 6/15/18.
 */

public class GoldBookingModel {


    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("Booking_value")
    private String Booking_value;
    @Expose
    @SerializedName("Gold_rate")
    private String Gold_rate;
    @Expose
    @SerializedName("Down_payment")
    private String Down_payment;
    @Expose
    @SerializedName("Monthly")
    private String Monthly;
    @Expose
    @SerializedName("Booking_charges")
    private String BookingCharge;
    @Expose
    @SerializedName("Initial_Booking_charges")
    private String Initial_Booking_charges;
    @Expose
    @SerializedName("Booking_charges_discount")
    private String Booking_charges_discount;

    public String getBooking_value() {
        return Booking_value;
    }

    public void setBooking_value(String booking_value) {
        Booking_value = booking_value;
    }

    public String getGold_rate() {
        return Gold_rate;
    }

    public void setGold_rate(String gold_rate) {
        Gold_rate = gold_rate;
    }

    public String getDown_payment() {
        return Down_payment;
    }

    public void setDown_payment(String down_payment) {
        Down_payment = down_payment;
    }

    public String getMonthly() {
        return Monthly;
    }

    public void setMonthly(String monthly) {
        Monthly = monthly;
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

    public String getBookingCharge() {
        return BookingCharge;
    }

    public void setBookingCharge(String bookingCharge) {
        BookingCharge = bookingCharge;
    }

    public String getInitial_Booking_charges() {
        return Initial_Booking_charges;
    }

    public void setInitial_Booking_charges(String initial_Booking_charges) {
        Initial_Booking_charges = initial_Booking_charges;
    }

    public String getBooking_charges_discount() {
        return Booking_charges_discount;
    }

    public void setBooking_charges_discount(String booking_charges_discount) {
        Booking_charges_discount = booking_charges_discount;
    }
}
