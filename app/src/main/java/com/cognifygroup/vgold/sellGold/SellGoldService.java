package com.cognifygroup.vgold.sellGold;

import com.cognifygroup.vgold.addGold.AddGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface SellGoldService {

    @POST("sale_gold.php?")
    @FormUrlEncoded
    Call<SellGoldModel> addGold(@Field("user_id") String user_id,
                                @Field("gold") String gold,
                                @Field("amount") String amount);

    @POST("sale_gold_verifyotp.php?")
    @FormUrlEncoded
    Call<SellGoldModel> getOTP(@Field("user_id") String user_id,
                               @Field("action") String gold);

    @POST("sale_gold_verifyotp.php?")
    @FormUrlEncoded
    Call<SellGoldModel> verifyOTP(@Field("user_id") String user_id,
                                  @Field("action") String action,
                                  @Field("otp") String otp);
}
