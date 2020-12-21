package com.cognifygroup.vgold.getAllTransactionForGold;

import android.content.Context;

import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyService;
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

public class GetAllTransactionGoldServiceProvider {
    private final GetAllTransactionGoldService getAllTransactionMoneyService;

    public GetAllTransactionGoldServiceProvider(Context context) {
        getAllTransactionMoneyService = APIServiceFactory.createService(GetAllTransactionGoldService.class, context);
    }

    public void getAllTransactionGoldHistory(String user_id,final APICallback apiCallback) {
        Call<GetAllTransactionGoldModel> call = null;
        call = getAllTransactionMoneyService.getAllTransactionForGold(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetAllTransactionGoldModel>() {
            @Override
            public void onResponse(Call<GetAllTransactionGoldModel> call, Response<GetAllTransactionGoldModel> response) {
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
            public void onFailure(Call<GetAllTransactionGoldModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}


