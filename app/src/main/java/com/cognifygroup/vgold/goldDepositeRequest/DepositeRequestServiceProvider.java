package com.cognifygroup.vgold.goldDepositeRequest;

import android.content.Context;

import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.addGold.AddGoldService;
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

public class DepositeRequestServiceProvider {

    private final DepositeRequestService depositeRequestService;

    public DepositeRequestServiceProvider(Context context) {
        depositeRequestService = APIServiceFactory.createService(DepositeRequestService.class, context);
    }

    public void getDepositeRequest(String user_id, String gw, String tennure, String cmw, String vendor_id, String guarantee, final APICallback apiCallback) {
        Call<DepositeRequestModel> call = null;
        call = depositeRequestService.addGold(user_id,gw,tennure,cmw,vendor_id,guarantee);
        String url = call.request().url().toString();

        call.enqueue(new Callback<DepositeRequestModel>() {
            @Override
            public void onResponse(Call<DepositeRequestModel> call, Response<DepositeRequestModel> response) {
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
            public void onFailure(Call<DepositeRequestModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
