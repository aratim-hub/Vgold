package com.cognifygroup.vgold.getTodaysGoldRate;

import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/22/18.
 */

public interface GetTodayGoldRateService {
    @GET("get_purchase_rate.php?")
    Call<GetTodayGoldRateModel> getTodayGoldRate();

    @POST("total_gold_booking_gain.php?")
    @FormUrlEncoded
    Call<GetTotalGoldGainModel> getTotalGoldGain(@Field("user_id") String userId);
}
