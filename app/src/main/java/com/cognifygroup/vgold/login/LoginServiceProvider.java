package com.cognifygroup.vgold.login;

import android.content.Context;

import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER1 on 1/15/2018.
 */

public class LoginServiceProvider {

    private final LoginService mLoginService;

    public LoginServiceProvider(Context context) {
        mLoginService = APIServiceFactory.createService(LoginService.class, context);
    }

    public void callUserLogin(String email,String appSignature, final APICallback apiCallback) {
        Call<LoginModel> call = null;
        call = mLoginService.getLogin(email,appSignature);
        String url = call.request().url().toString();

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("200")) {
                    apiCallback.onSuccess(response.body());
                } else if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("400")) {
                    apiCallback.onSuccess(response.body());
                } else {
                   /* BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());*/
                     apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void callVerifyUserLogin(String email,String otp,String token, final APICallback apiCallback) {
        Call<LoginModel> call = null;
        call = mLoginService.verifyLogin(email,otp, token);
        String url = call.request().url().toString();

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("200")) {
                    apiCallback.onSuccess(response.body());
                } else if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("400")) {
                    apiCallback.onSuccess(response.body());
                } else {
                   /* BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());*/
                    apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
