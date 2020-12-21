package com.cognifygroup.vgold.WithdrawMoney;

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

public class WithdrawMoneyServiceProvider {

    private final WithdrawMoneyService withdrawMoneyService;

    public WithdrawMoneyServiceProvider(Context context) {
        withdrawMoneyService = APIServiceFactory.createService(WithdrawMoneyService.class, context);
    }

    public void getAddBankDetails(String user_id, String bank_id, String amount,String comment,final APICallback apiCallback) {
        Call<WithdrawMoneyModel> call = null;
        call = withdrawMoneyService.withdrawMoney(user_id,bank_id,amount,comment);
        String url = call.request().url().toString();

        call.enqueue(new Callback<WithdrawMoneyModel>() {
            @Override
            public void onResponse(Call<WithdrawMoneyModel> call, Response<WithdrawMoneyModel> response) {
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
            public void onFailure(Call<WithdrawMoneyModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
