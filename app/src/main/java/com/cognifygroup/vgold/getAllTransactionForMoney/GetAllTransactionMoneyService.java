package com.cognifygroup.vgold.getAllTransactionForMoney;

import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/20/18.
 */

public interface GetAllTransactionMoneyService {

    @POST("money_wallet_transactions.php?")
    @FormUrlEncoded
    Call<GetAllTransactionMoneyModel> getAllTransactionForMoney(@Field("user_id") String user_id);
}
