package com.cognifygroup.vgold.getMaturityWeight;

import android.content.Context;

import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.AddBank.AddBankService;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 6/15/18.
 */

public class MaturityWeightServiceProvider {

    private final MaturityWeightService maturityWeightService;

    public MaturityWeightServiceProvider(Context context) {
        maturityWeightService = APIServiceFactory.createService(MaturityWeightService.class, context);
    }

    public void getMaturityWeight(String gold_weight, String tennure, String guarantee, final APICallback apiCallback) {
        Call<MaturityWeightModel> call = null;
        call = maturityWeightService.getMaturityWeight(gold_weight,tennure,guarantee);
        String url = call.request().url().toString();

        call.enqueue(new Callback<MaturityWeightModel>() {
            @Override
            public void onResponse(Call<MaturityWeightModel> call, Response<MaturityWeightModel> response) {
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
            public void onFailure(Call<MaturityWeightModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
