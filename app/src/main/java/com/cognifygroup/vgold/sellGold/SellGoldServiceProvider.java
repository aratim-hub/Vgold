package com.cognifygroup.vgold.sellGold;

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

public class SellGoldServiceProvider {

    private final SellGoldService sellGoldService;

    public SellGoldServiceProvider(Context context) {
        sellGoldService = APIServiceFactory.createService(SellGoldService.class, context);
    }

    public void getAddBankDetails(String user_id, String gold, String amount, final APICallback apiCallback) {
        Call<SellGoldModel> call = null;
        call = sellGoldService.addGold(user_id, gold, amount);
        String url = call.request().url().toString();

        call.enqueue(new Callback<SellGoldModel>() {
            @Override
            public void onResponse(Call<SellGoldModel> call, Response<SellGoldModel> response) {
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
            public void onFailure(Call<SellGoldModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getOTP(String user_id, String action, final APICallback apiCallback) {
        Call<SellGoldModel> call = null;
        call = sellGoldService.getOTP(user_id, action);
        String url = call.request().url().toString();

        call.enqueue(new Callback<SellGoldModel>() {
            @Override
            public void onResponse(Call<SellGoldModel> call, Response<SellGoldModel> response) {
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
            public void onFailure(Call<SellGoldModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void verifyOTP(String user_id, String action, String otp, final APICallback apiCallback) {
        Call<SellGoldModel> call = null;
        call = sellGoldService.verifyOTP(user_id, action, otp);
        String url = call.request().url().toString();

        call.enqueue(new Callback<SellGoldModel>() {
            @Override
            public void onResponse(Call<SellGoldModel> call, Response<SellGoldModel> response) {
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
            public void onFailure(Call<SellGoldModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
