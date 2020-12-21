package com.cognifygroup.vgold.addGold;

import com.cognifygroup.vgold.AddBank.AddBankModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface AddGoldService {

    @POST("add_gold.php?")
    @FormUrlEncoded
    Call<AddGoldModel> addGold(@Field("user_id") String user_id,
                                         @Field("gold") String gold,
                                         @Field("amount") String amount,
                                         @Field("payment_option") String payment_option,
                                         @Field("bank_details") String bank_details,
                                         @Field("tr_id") String tr_id,
                                         @Field("cheque_no") String cheque_no);
}
