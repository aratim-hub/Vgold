package com.cognifygroup.vgold.getReferCode;

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

public class ReferServiceProvider {

    private final ReferService referService;

    public ReferServiceProvider(Context context) {
        referService = APIServiceFactory.createService(ReferService.class, context);
    }

    public void getAddBankDetails(String user_id, String name, String email, String mobile_no, String refLink, final APICallback apiCallback) {
        Call<ReferModel> call = null;
        call = referService.getBankAddDetails(user_id, name, email, mobile_no, refLink);
        String url = call.request().url().toString();

        call.enqueue(new Callback<ReferModel>() {
            @Override
            public void onResponse(Call<ReferModel> call, Response<ReferModel> response) {
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
            public void onFailure(Call<ReferModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getReferenceCode(String user_id, final APICallback apiCallback) {
        Call<ReferModel> call = null;
        call = referService.getReferenceCode(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<ReferModel>() {
            @Override
            public void onResponse(Call<ReferModel> call, Response<ReferModel> response) {
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
            public void onFailure(Call<ReferModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getCheckWallet(String user_id, String data, final APICallback apiCallback) {
        Call<ReferModel> call = null;
        call = referService.getWalletPoint(user_id, data);
        String url = call.request().url().toString();

        call.enqueue(new Callback<ReferModel>() {
            @Override
            public void onResponse(Call<ReferModel> call, Response<ReferModel> response) {
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
            public void onFailure(Call<ReferModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }





}
