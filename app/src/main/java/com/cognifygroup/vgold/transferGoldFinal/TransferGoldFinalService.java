package com.cognifygroup.vgold.transferGoldFinal;

import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface TransferGoldFinalService {

    @POST("gold_transfer.php?")
    @FormUrlEncoded
    Call<TransferGoldFinalModel> transferGold(@Field("user_id") String user_id,
                                                @Field("no") String no,
                                                @Field("gold") String gold);
}
