package com.cognifygroup.vgold.getImageUpload;

import android.content.Context;


import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 8/7/18.
 */

public class GetSingleImageServiceProvider0 {

    private final GetSingleImageService0 getSingleImageService0;

    public GetSingleImageServiceProvider0(Context context) {
        getSingleImageService0 = APIServiceFactory.createService(GetSingleImageService0.class, context);
    }

    public void getReg(String client_id, String image, final APICallback apiCallback) {
        Call<GetSingleImage0> call = null;
        call = getSingleImageService0.getReg(client_id,image);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetSingleImage0>() {
            @Override
            public void onResponse(Call<GetSingleImage0> call, Response<GetSingleImage0> response) {
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
            public void onFailure(Call<GetSingleImage0> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
