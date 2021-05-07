package com.cognifygroup.vgold.register;

import android.content.Context;
import android.util.Log;

import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 3/15/18.
 */

public class RegServiceProvider {
    private final RegService regService;

    public RegServiceProvider(Context context) {
        regService = APIServiceFactory.createService(RegService.class, context);
    }

    public void getReg(String first, String last, String email, String no, String pancard, String refer_code,
                       String aadhar_no, String aadharF, String aadharB, String panPic, final APICallback apiCallback) {
        Call<RegModel> call = null;
        call = regService.getReg(first, last, email, no, pancard, refer_code, panPic, aadharF, aadharB, aadhar_no);
        String url = call.request().url().toString();

        call.enqueue(new Callback<RegModel>() {
            @Override
            public void onResponse(Call<RegModel> call, Response<RegModel> response) {
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
            public void onFailure(Call<RegModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}