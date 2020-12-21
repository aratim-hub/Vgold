package com.cognifygroup.vgold.transferMoney;

import com.cognifygroup.vgold.sellGold.SellGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface TransferMoneyService {

    @POST("fetch_name.php?")
    @FormUrlEncoded
    Call<TransferMoneyModel> addGold(@Field("no") String no);
}
