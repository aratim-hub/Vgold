package com.cognifygroup.vgold.getTodaysGoldRate;

import android.content.Context;

import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 6/22/18.
 */

public class GetTodayGoldRateServiceProvider {
    private final GetTodayGoldRateService getTodayGoldRateService;

    public GetTodayGoldRateServiceProvider(Context context) {
        getTodayGoldRateService = APIServiceFactory.createService(GetTodayGoldRateService.class, context);
    }

    public void getTodayGoldRate(final APICallback apiCallback) {
        Call<GetTodayGoldRateModel> call = null;
        call = getTodayGoldRateService.getTodayGoldRate();
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetTodayGoldRateModel>() {
            @Override
            public void onResponse(Call<GetTodayGoldRateModel> call, Response<GetTodayGoldRateModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("200")) {
                    apiCallback.onSuccess(response.body());
                } else if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("400")) {
                    apiCallback.onSuccess(response.body());
                } else {
                    BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<GetTodayGoldRateModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getTotalGain(String userId, final APICallback apiCallback) {
        Call<GetTotalGoldGainModel> call = null;
        call = getTodayGoldRateService.getTotalGoldGain(userId);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetTotalGoldGainModel>() {
            @Override
            public void onResponse(Call<GetTotalGoldGainModel> call, Response<GetTotalGoldGainModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("200")) {
                    apiCallback.onSuccess(response.body());
                } else if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("400")) {
                    apiCallback.onSuccess(response.body());
                } else {
                    BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<GetTotalGoldGainModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}


