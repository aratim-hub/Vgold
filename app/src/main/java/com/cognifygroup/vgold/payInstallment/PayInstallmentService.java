package com.cognifygroup.vgold.payInstallment;

import com.cognifygroup.vgold.addGold.AddGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface PayInstallmentService {

    @POST("installment.php?")
    @FormUrlEncoded
    Call<PayInstallmentModel> addGold(@Field("user_id") String user_id,
                               @Field("gbid") String gbid,
                               @Field("amountr") String amountr,
                               @Field("payment_option") String payment_option,
                               @Field("bank_details") String bank_details,
                               @Field("tr_id") String tr_id,
                               @Field("cheque_no") String cheque_no);
}
