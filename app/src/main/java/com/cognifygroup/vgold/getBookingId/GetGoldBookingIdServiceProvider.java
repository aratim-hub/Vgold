package com.cognifygroup.vgold.getBookingId;

import android.content.Context;

import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpModel;
import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpService;
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

public class GetGoldBookingIdServiceProvider {

    private final GetGoldBookingIdService getGoldBookingIdService;

    public GetGoldBookingIdServiceProvider(Context context) {
        getGoldBookingIdService = APIServiceFactory.createService(GetGoldBookingIdService.class, context);
    }

    public void getGoldBookingId(String user_id,final APICallback apiCallback) {
        Call<GetBookingIdModel> call = null;
        call = getGoldBookingIdService.getGoldBookingId(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetBookingIdModel>() {
            @Override
            public void onResponse(Call<GetBookingIdModel> call, Response<GetBookingIdModel> response) {
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
            public void onFailure(Call<GetBookingIdModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
