package com.cognifygroup.vgold.WithdrawMoney;

import com.cognifygroup.vgold.sellGold.SellGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface WithdrawMoneyService {

    @POST("add_withdraw_request.php?")
    @FormUrlEncoded
    Call<WithdrawMoneyModel> withdrawMoney(@Field("user_id") String user_id,
                                @Field("bank_id") String bank_id,
                                @Field("amount") String amount,
                                @Field("comment") String comment);
}
