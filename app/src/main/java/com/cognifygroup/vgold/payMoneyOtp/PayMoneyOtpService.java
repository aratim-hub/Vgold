package com.cognifygroup.vgold.payMoneyOtp;

import com.cognifygroup.vgold.transferMoney.TransferMoneyModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface PayMoneyOtpService {

    @POST("money_send_otp.php?")
    @FormUrlEncoded
    Call<PayMoneyOtpModel> addGold(@Field("user_id") String user_id);
}
