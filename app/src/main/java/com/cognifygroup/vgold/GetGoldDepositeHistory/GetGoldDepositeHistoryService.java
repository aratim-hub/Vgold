package com.cognifygroup.vgold.GetGoldDepositeHistory;


import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/16/18.
 */

public interface GetGoldDepositeHistoryService {

    @POST("gold_deposite_history.php?")
    @FormUrlEncoded
    Call<GetGoldDepositeHistoryModel> getGoldDepositegHistory(@Field("user_id") String user_id);
}
