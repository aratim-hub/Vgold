package com.cognifygroup.vgold.getMaturityWeight;

import com.cognifygroup.vgold.AddBank.AddBankModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface MaturityWeightService {

    @POST("calculate_gold_mature_weight.php?")
    @FormUrlEncoded
    Call<MaturityWeightModel> getMaturityWeight(@Field("gold_weight") String gold_weight,
                                         @Field("tennure") String tennure,
                                         @Field("guarantee") String guarantee);
}
