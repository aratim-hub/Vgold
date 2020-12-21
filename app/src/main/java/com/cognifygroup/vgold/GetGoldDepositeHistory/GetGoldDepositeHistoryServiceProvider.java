package com.cognifygroup.vgold.GetGoldDepositeHistory;

import android.content.Context;

import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryService;
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

public class GetGoldDepositeHistoryServiceProvider {
    private final GetGoldDepositeHistoryService getGoldDepositeHistoryService;

    public GetGoldDepositeHistoryServiceProvider(Context context) {
        getGoldDepositeHistoryService = APIServiceFactory.createService(GetGoldDepositeHistoryService.class, context);
    }

    public void getGoldDepositeHistory(String user_id,final APICallback apiCallback) {
        Call<GetGoldDepositeHistoryModel> call = null;
        call = getGoldDepositeHistoryService.getGoldDepositegHistory(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetGoldDepositeHistoryModel>() {
            @Override
            public void onResponse(Call<GetGoldDepositeHistoryModel> call, Response<GetGoldDepositeHistoryModel> response) {
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
            public void onFailure(Call<GetGoldDepositeHistoryModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}

