package com.cognifygroup.vgold.CPModule;

import android.content.Context;

import com.cognifygroup.vgold.ChannelPartner.UserCommissionDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIStatusDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserGoldDetailsModel;
import com.cognifygroup.vgold.getOtp.OtpModel;
import com.cognifygroup.vgold.getOtp.OtpService;
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

public class CPServiceProvider {
    private final CPService cpService;

    public CPServiceProvider(Context context) {
        cpService = APIServiceFactory.createService(CPService.class, context);
    }

    public void getUserDetails(String id,final APICallback apiCallback) {
        Call<UserDetailsModel> call = null;
        call = cpService.getCPUserDetails(id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<UserDetailsModel>() {
            @Override
            public void onResponse(Call<UserDetailsModel> call, Response<UserDetailsModel> response) {
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
            public void onFailure(Call<UserDetailsModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getUserGoldDetails(String id,final APICallback apiCallback) {
        Call<UserGoldDetailsModel> call = null;
        call = cpService.getCPUserGoldDetails(id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<UserGoldDetailsModel>() {
            @Override
            public void onResponse(Call<UserGoldDetailsModel> call, Response<UserGoldDetailsModel> response) {
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
            public void onFailure(Call<UserGoldDetailsModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getUserCommissionDetails(String id,final APICallback apiCallback) {
        Call<UserCommissionDetailsModel> call = null;
        call = cpService.getCPUserCommissionDetails(id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<UserCommissionDetailsModel>() {
            @Override
            public void onResponse(Call<UserCommissionDetailsModel> call, Response<UserCommissionDetailsModel> response) {
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
            public void onFailure(Call<UserCommissionDetailsModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getUserEMIDetails(String id,final APICallback apiCallback) {
        Call<UserEMIDetailsModel> call = null;
        call = cpService.getCPUserEMIDetails(id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<UserEMIDetailsModel>() {
            @Override
            public void onResponse(Call<UserEMIDetailsModel> call, Response<UserEMIDetailsModel> response) {
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
            public void onFailure(Call<UserEMIDetailsModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void getUserEMIStatusDetails(String id,final APICallback apiCallback) {
        Call<UserEMIStatusDetailsModel> call = null;
        call = cpService.getCPUserEMIStatusDetails(id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<UserEMIStatusDetailsModel>() {
            @Override
            public void onResponse(Call<UserEMIStatusDetailsModel> call, Response<UserEMIStatusDetailsModel> response) {
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
            public void onFailure(Call<UserEMIStatusDetailsModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
