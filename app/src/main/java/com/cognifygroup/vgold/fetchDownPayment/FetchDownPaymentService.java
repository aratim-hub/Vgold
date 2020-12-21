package com.cognifygroup.vgold.fetchDownPayment;

import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface FetchDownPaymentService {

    @POST("fetch_down_payment.php?")
    @FormUrlEncoded
    Call<FetchDownPaymentModel> fetchDownPayment(@Field("gbid") String gbid);
}
