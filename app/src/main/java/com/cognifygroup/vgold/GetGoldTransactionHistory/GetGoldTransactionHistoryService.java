package com.cognifygroup.vgold.GetGoldTransactionHistory;

import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/18/18.
 */

public interface GetGoldTransactionHistoryService {

    @POST("gold_booking_transactions.php?")
    @FormUrlEncoded
    Call<GetGoldTransactionHistoryModel> getGoldTransactionHistory(@Field("gold_booking_id") String gold_booking_id);
}
