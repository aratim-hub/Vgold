package com.cognifygroup.vgold.transferMoney;

import android.content.Context;

import com.cognifygroup.vgold.sellGold.SellGoldModel;
import com.cognifygroup.vgold.sellGold.SellGoldService;
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

public class TransferMoneyServiceProvider {

    private final TransferMoneyService transferMoneyService;

    public TransferMoneyServiceProvider(Context context) {
        transferMoneyService = APIServiceFactory.createService(TransferMoneyService.class, context);
    }

    public void getAddBankDetails(String no,final APICallback apiCallback) {
        Call<TransferMoneyModel> call = null;
        call = transferMoneyService.addGold(no);
        String url = call.request().url().toString();

        call.enqueue(new Callback<TransferMoneyModel>() {
            @Override
            public void onResponse(Call<TransferMoneyModel> call, Response<TransferMoneyModel> response) {
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
            public void onFailure(Call<TransferMoneyModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
