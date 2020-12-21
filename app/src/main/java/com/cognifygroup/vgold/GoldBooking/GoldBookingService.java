package com.cognifygroup.vgold.GoldBooking;

import com.cognifygroup.vgold.AddBank.AddBankModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface GoldBookingService {

    @POST("booking_details.php?")
    @FormUrlEncoded
    Call<GoldBookingModel> getGoldBooking(@Field("quantity") String quantity,
                                         @Field("tennure") String tennure,
                                         @Field("pc") String pc);
}
