package com.cognifygroup.vgold.GetGoldTransactionHistory;

import android.content.Context;

import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 6/18/18.
 */

public class GetGoldTransactionHistoryServiceProvider {
    private final GetGoldTransactionHistoryService getGoldTransactionHistoryService;

    public GetGoldTransactionHistoryServiceProvider(Context context) {
        getGoldTransactionHistoryService = APIServiceFactory.createService(GetGoldTransactionHistoryService.class, context);
    }

    public void getGoldTransactionHistory(String gold_booking_id,final APICallback apiCallback) {
        Call<GetGoldTransactionHistoryModel> call = null;
        call = getGoldTransactionHistoryService.getGoldTransactionHistory(gold_booking_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetGoldTransactionHistoryModel>() {
            @Override
            public void onResponse(Call<GetGoldTransactionHistoryModel> call, Response<GetGoldTransactionHistoryModel> response) {
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
            public void onFailure(Call<GetGoldTransactionHistoryModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}


