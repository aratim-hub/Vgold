package com.cognifygroup.vgold.payInstallment;

import android.content.Context;

import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.addGold.AddGoldService;
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

public class PayInstallmentServiceProvider {

    private final PayInstallmentService payInstallmentService;

    public PayInstallmentServiceProvider(Context context) {
        payInstallmentService = APIServiceFactory.createService(PayInstallmentService.class, context);
    }

    public void payInstallment(String user_id, String gbid, String amountr, String payment_option,
                               String bank_details, String tr_id, String otherAmount,
                               String cheque_no,String status, final APICallback apiCallback) {
        Call<PayInstallmentModel> call = null;
        call = payInstallmentService.addGold(user_id, gbid, amountr, payment_option, bank_details,
                tr_id, otherAmount,
                cheque_no, status);
        String url = call.request().url().toString();

        call.enqueue(new Callback<PayInstallmentModel>() {
            @Override
            public void onResponse(Call<PayInstallmentModel> call, Response<PayInstallmentModel> response) {
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
            public void onFailure(Call<PayInstallmentModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
