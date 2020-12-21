package com.cognifygroup.vgold.getAllTransactionForMoney;

import android.content.Context;

import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 6/20/18.
 */

public class GetAllTransactionMoneyServiceProvider {
    private final GetAllTransactionMoneyService getAllTransactionMoneyService;

    public GetAllTransactionMoneyServiceProvider(Context context) {
        getAllTransactionMoneyService = APIServiceFactory.createService(GetAllTransactionMoneyService.class, context);
    }

    public void getAllTransactionMoneyHistory(String user_id,final APICallback apiCallback) {
        Call<GetAllTransactionMoneyModel> call = null;
        call = getAllTransactionMoneyService.getAllTransactionForMoney(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetAllTransactionMoneyModel>() {
            @Override
            public void onResponse(Call<GetAllTransactionMoneyModel> call, Response<GetAllTransactionMoneyModel> response) {
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
            public void onFailure(Call<GetAllTransactionMoneyModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}


