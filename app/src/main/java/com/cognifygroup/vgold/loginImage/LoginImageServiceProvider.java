package com.cognifygroup.vgold.loginImage;

import android.content.Context;

import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel;
import com.cognifygroup.vgold.getVendorOffer.VendorOfferService;
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

public class LoginImageServiceProvider {
    private final LoginImageService loginImageService;

    public LoginImageServiceProvider(Context context) {
        loginImageService = APIServiceFactory.createService(LoginImageService.class, context);
    }

    public void getGoldBookingHistory(final APICallback apiCallback) {
        Call<LoginImageModel> call = null;
        call = loginImageService.getImage();
        String url = call.request().url().toString();

        call.enqueue(new Callback<LoginImageModel>() {
            @Override
            public void onResponse(Call<LoginImageModel> call, Response<LoginImageModel> response) {
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
            public void onFailure(Call<LoginImageModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }


}

