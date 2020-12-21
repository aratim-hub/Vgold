package com.cognifygroup.vgold.getOtp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 7/26/18.
 */

public interface OtpService {

    @POST("forgot_password_otp.php?")
    @FormUrlEncoded
    Call<OtpModel> getOtp(@Field("id") String id);
}
