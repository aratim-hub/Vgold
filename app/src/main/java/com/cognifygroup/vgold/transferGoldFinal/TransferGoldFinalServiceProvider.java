package com.cognifygroup.vgold.transferGoldFinal;

import android.content.Context;

import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalModel;
import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalService;
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

public class TransferGoldFinalServiceProvider {

    private final TransferGoldFinalService transferGoldFinalService;

    public TransferGoldFinalServiceProvider(Context context) {
        transferGoldFinalService = APIServiceFactory.createService(TransferGoldFinalService.class, context);
    }

    public void transferMoney(String user_id,String no,String gold,final APICallback apiCallback) {
        Call<TransferGoldFinalModel> call = null;
        call = transferGoldFinalService.transferGold(user_id,no,gold);
        String url = call.request().url().toString();

        call.enqueue(new Callback<TransferGoldFinalModel>() {
            @Override
            public void onResponse(Call<TransferGoldFinalModel> call, Response<TransferGoldFinalModel> response) {
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
            public void onFailure(Call<TransferGoldFinalModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
