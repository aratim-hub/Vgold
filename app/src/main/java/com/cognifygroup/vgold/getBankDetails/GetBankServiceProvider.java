package com.cognifygroup.vgold.getBankDetails;

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

public class GetBankServiceProvider {

    private final GetBankService getBankService;

    public GetBankServiceProvider(Context context) {
        getBankService = APIServiceFactory.createService(GetBankService.class, context);
    }

    public void getAddBankDetails(String user_id, final APICallback apiCallback) {
        Call<GetBankModel> call = null;
        call = getBankService.addGold(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GetBankModel>() {
            @Override
            public void onResponse(Call<GetBankModel> call, Response<GetBankModel> response) {
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
            public void onFailure(Call<GetBankModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
