package com.cognifygroup.vgold.payGoldOtp;

import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface PayGoldOtpService {

    @POST("gold_send_otp.php?")
    @FormUrlEncoded
    Call<PayGoldOtpModel> addGold(@Field("user_id") String user_id);
}
