package com.cognifygroup.vgold.AddComplain;

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

public class AddComplainServiceProvider {

    private final AddComplainService addComplainService;

    public AddComplainServiceProvider(Context context) {
        addComplainService = APIServiceFactory.createService(AddComplainService.class, context);
    }

    public void addComplain(String user_id, String complain, final APICallback apiCallback) {
        Call<AddComplainModel> call = null;
        call = addComplainService.AddComplain(user_id,complain);
        String url = call.request().url().toString();

        call.enqueue(new Callback<AddComplainModel>() {
            @Override
            public void onResponse(Call<AddComplainModel> call, Response<AddComplainModel> response) {
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
            public void onFailure(Call<AddComplainModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void addReview(String user_id, String comment, final APICallback apiCallback) {
        Call<AddComplainModel> call = null;
        call = addComplainService.AddReview(user_id,comment);
        String url = call.request().url().toString();

        call.enqueue(new Callback<AddComplainModel>() {
            @Override
            public void onResponse(Call<AddComplainModel> call, Response<AddComplainModel> response) {
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
            public void onFailure(Call<AddComplainModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
