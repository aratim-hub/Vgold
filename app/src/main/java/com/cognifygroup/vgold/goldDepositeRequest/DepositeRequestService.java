package com.cognifygroup.vgold.goldDepositeRequest;

import com.cognifygroup.vgold.addGold.AddGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface DepositeRequestService {

    @POST("gold_deposite.php?")
    @FormUrlEncoded
    Call<DepositeRequestModel> addGold(@Field("user_id") String user_id,
                               @Field("gw") String gw,
                               @Field("tennure") String tennure,
                               @Field("cmw") String cmw,
                               @Field("vendor_id") String vendor_id,
                               @Field("addpurity") String add_purity,
                               @Field("remark") String remark,
                               @Field("guarantee") String guarantee);
}
