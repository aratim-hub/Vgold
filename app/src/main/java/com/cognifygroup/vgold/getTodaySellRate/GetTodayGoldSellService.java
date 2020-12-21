package com.cognifygroup.vgold.getTodaySellRate;

import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shivraj on 6/22/18.
 */

public interface GetTodayGoldSellService {
    @GET("get_sale_rate.php?")
    Call<GetTodayGoldSellModel> getTodayGoldRate();
}
