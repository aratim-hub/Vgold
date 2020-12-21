package com.cognifygroup.vgold.getBookingId;

import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface GetGoldBookingIdService {

    @POST("installment_booking_id.php?")
    @FormUrlEncoded
    Call<GetBookingIdModel> getGoldBookingId(@Field("user_id") String user_id);
}
