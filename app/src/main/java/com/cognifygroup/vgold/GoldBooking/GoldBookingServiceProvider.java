package com.cognifygroup.vgold.GoldBooking;

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

public class GoldBookingServiceProvider {

    private final GoldBookingService goldBookingService;

    public GoldBookingServiceProvider(Context context) {
        goldBookingService = APIServiceFactory.createService(GoldBookingService.class, context);
    }

    public void getAddBankDetails(String quantity, String tennure, String pc, final APICallback apiCallback) {
        Call<GoldBookingModel> call = null;
        call = goldBookingService.getGoldBooking(quantity,tennure,pc);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GoldBookingModel>() {
            @Override
            public void onResponse(Call<GoldBookingModel> call, Response<GoldBookingModel> response) {
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
            public void onFailure(Call<GoldBookingModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
