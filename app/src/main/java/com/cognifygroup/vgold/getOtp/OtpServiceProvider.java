package com.cognifygroup.vgold.getOtp;

import android.content.Context;


import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 7/26/18.
 */

public class OtpServiceProvider {
    private final OtpService otpService;

    public OtpServiceProvider(Context context) {
        otpService = APIServiceFactory.createService(OtpService.class, context);
    }

    public void getReg(String id,final APICallback apiCallback) {
        Call<OtpModel> call = null;
        call = otpService.getOtp(id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("200")) {
                    apiCallback.onSuccess(response.body());
                } else if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("300")) {
                    apiCallback.onSuccess(response.body());
                } else {
                    BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
