package com.cognifygroup.vgold.TransferMoneyFinal;

import android.content.Context;

import com.cognifygroup.vgold.transferMoney.TransferMoneyModel;
import com.cognifygroup.vgold.transferMoney.TransferMoneyService;
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

public class TransferMoneyFinalServiceProvider {

    private final TransferMoneyFinalService transferMoneyFinalService;

    public TransferMoneyFinalServiceProvider(Context context) {
        transferMoneyFinalService = APIServiceFactory.createService(TransferMoneyFinalService.class, context);
    }

    public void transferMoney(String user_id,String no,String amount,final APICallback apiCallback) {
        Call<TransferMoneyFinalModel> call = null;
        call = transferMoneyFinalService.transferMoney(user_id,no,amount);
        String url = call.request().url().toString();

        call.enqueue(new Callback<TransferMoneyFinalModel>() {
            @Override
            public void onResponse(Call<TransferMoneyFinalModel> call, Response<TransferMoneyFinalModel> response) {
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
            public void onFailure(Call<TransferMoneyFinalModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
