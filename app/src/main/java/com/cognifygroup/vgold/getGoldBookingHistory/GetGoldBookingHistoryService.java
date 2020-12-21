package com.cognifygroup.vgold.getGoldBookingHistory;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/16/18.
 */

public interface GetGoldBookingHistoryService {

    @POST("gold_booking_history.php?")
    @FormUrlEncoded
    Call<GetGoldBookingHistoryModel> getGoldBookingHistory(@Field("user_id") String user_id);
}
