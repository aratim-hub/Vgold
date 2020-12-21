package com.cognifygroup.vgold.ChangePassword;

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

public class ChangePasswordServiceProvider {
    private final ChangePasswordService changePasswordService;

    public ChangePasswordServiceProvider(Context context) {
        changePasswordService = APIServiceFactory.createService(ChangePasswordService.class, context);
    }

    public void changePassword(String id,String otp,String pass,final APICallback apiCallback) {
        Call<ChangePasswordModel> call = null;
        call = changePasswordService.getChangePassword(id,otp,pass);
        String url = call.request().url().toString();

        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
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
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
