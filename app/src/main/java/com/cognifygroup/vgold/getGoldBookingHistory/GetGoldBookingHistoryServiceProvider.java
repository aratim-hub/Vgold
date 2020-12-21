package com.cognifygroup.vgold.getGoldBookingHistory;

import android.content.Context;


import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 6/16/18.
 */

public class GetGoldBookingHistoryServiceProvider {
    private final GetGoldBookingHistoryService getGoldBookingHistoryService;

    public GetGoldBookingHistoryServiceProvider(Context context) {
        getGoldBookingHistoryService = APIServiceFactory.createService(GetGoldBookingHistoryService.class, context);
    }

    public void getGoldBookingHistory(String user_id,final APICallback apiCallback) {
        Call<GetGoldBookingHistoryModel> call = null;
        call = getGoldBookingHistoryService.getGoldBookingHistory(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetGoldBookingHistoryModel>() {
            @Override
            public void onResponse(Call<GetGoldBookingHistoryModel> call, Response<GetGoldBookingHistoryModel> response) {
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
            public void onFailure(Call<GetGoldBookingHistoryModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}

