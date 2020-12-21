package com.cognifygroup.vgold.fetchDownPayment;

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

public class FetchDownPaymentServiceProvider {

    private final FetchDownPaymentService fetchDownPaymentService;

    public FetchDownPaymentServiceProvider(Context context) {
        fetchDownPaymentService = APIServiceFactory.createService(FetchDownPaymentService.class, context);
    }

    public void getAddBankDetails(String gbid,final APICallback apiCallback) {
        Call<FetchDownPaymentModel> call = null;
        call = fetchDownPaymentService.fetchDownPayment(gbid);
        String url = call.request().url().toString();

        call.enqueue(new Callback<FetchDownPaymentModel>() {
            @Override
            public void onResponse(Call<FetchDownPaymentModel> call, Response<FetchDownPaymentModel> response) {
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
            public void onFailure(Call<FetchDownPaymentModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
