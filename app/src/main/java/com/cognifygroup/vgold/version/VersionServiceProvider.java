package com.cognifygroup.vgold.version;

import android.content.Context;

import com.cognifygroup.vgold.loginImage.LoginImageModel;
import com.cognifygroup.vgold.loginImage.LoginImageService;
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

public class VersionServiceProvider {
    private final VersionService versionService;

    public VersionServiceProvider(Context context) {
        versionService = APIServiceFactory.createService(VersionService.class, context);
    }

    public void getVerionCheck(String version, final APICallback apiCallback) {
        Call<VersionModel> call = null;
        call = versionService.getVersion(version);
        String url = call.request().url().toString();

        call.enqueue(new Callback<VersionModel>() {
            @Override
            public void onResponse(Call<VersionModel> call, Response<VersionModel> response) {
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
            public void onFailure(Call<VersionModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}

