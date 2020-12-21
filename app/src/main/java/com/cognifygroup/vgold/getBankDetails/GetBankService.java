package com.cognifygroup.vgold.getBankDetails;

import com.cognifygroup.vgold.addGold.AddGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by shivraj on 8/21/18.
 */

public interface GetBankService {

    @POST("get_bank_details.php?")
    @FormUrlEncoded
    Call<GetBankModel> addGold(@Field("user_id") String user_id);
}
