package com.cognifygroup.vgold.plan;

import com.cognifygroup.vgold.getReferCode.ReferModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface PlanService {

    @POST("get_gold_plans.php?")
    @FormUrlEncoded
    Call<PlanModel> getPlan(@Field("quantity") String qty);
}
