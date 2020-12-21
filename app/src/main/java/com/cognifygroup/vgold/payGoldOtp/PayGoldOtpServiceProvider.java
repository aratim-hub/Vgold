package com.cognifygroup.vgold.payGoldOtp;

import android.content.Context;

import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpModel;
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpService;
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

public class PayGoldOtpServiceProvider {

    private final PayGoldOtpService payGoldOtpService;

    public PayGoldOtpServiceProvider(Context context) {
        payGoldOtpService = APIServiceFactory.createService(PayGoldOtpService.class, context);
    }

    public void getAddBankDetails(String user_id,final APICallback apiCallback) {
        Call<PayGoldOtpModel> call = null;
        call = payGoldOtpService.addGold(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<PayGoldOtpModel>() {
            @Override
            public void onResponse(Call<PayGoldOtpModel> call, Response<PayGoldOtpModel> response) {
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
            public void onFailure(Call<PayGoldOtpModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
