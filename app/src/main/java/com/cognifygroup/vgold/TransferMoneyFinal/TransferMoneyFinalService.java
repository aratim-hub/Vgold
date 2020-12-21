package com.cognifygroup.vgold.TransferMoneyFinal;

import com.cognifygroup.vgold.transferMoney.TransferMoneyModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface TransferMoneyFinalService {

    @POST("money_transfer.php?")
    @FormUrlEncoded
    Call<TransferMoneyFinalModel> transferMoney(@Field("user_id") String user_id,
                                                @Field("no") String no,
                                                @Field("amount") String amount);
}
