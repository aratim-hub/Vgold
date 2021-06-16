package com.cognifygroup.vgold.goldbookingrequest;

import com.cognifygroup.vgold.GoldBooking.GoldBookingModel;
import com.cognifygroup.vgold.addGold.AddGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface GoldBookingRequestService {

    @POST("gold_booking.php?")
    @FormUrlEncoded
    Call<GoldBookingRequestModel> addGold(@Field("user_id") String user_id,
                                          @Field("booking_value") String booking_value,
                                          @Field("down_payment") String down_payment,
                                          @Field("monthly") String monthly,
                                          @Field("rate") String rate,
                                          @Field("gold_weight") String gold_weight,
                                          @Field("tennure") String tennure,
                                          @Field("pc") String pc,
                                          @Field("payment_option") String payment_option,
                                          @Field("bank_details") String bank_details,
                                          @Field("tr_id") String tr_id,
                                          @Field("cheque_no") String cheque_no,
                                          @Field("initial_booking_charges") String initBookCharges,
                                          @Field("booking_charges_discount") String bookChargeDisc,
                                          @Field("booking_charges") String bookingCharge,
                                          @Field("confirmed") String confirmedVal
                                          );
}
