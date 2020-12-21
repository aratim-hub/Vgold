package com.cognifygroup.vgold.getAllTransactionForGold;

import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/20/18.
 */

public interface GetAllTransactionGoldService {

    @POST("gold_wallet_transactions.php?")
    @FormUrlEncoded
    Call<GetAllTransactionGoldModel> getAllTransactionForGold(@Field("user_id") String user_id);
}

