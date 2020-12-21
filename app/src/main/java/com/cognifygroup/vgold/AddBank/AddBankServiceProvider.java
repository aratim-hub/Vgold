package com.cognifygroup.vgold.AddBank;

import android.content.Context;


import com.cognifygroup.vgold.register.RegModel;

import com.cognifygroup.vgold.register.RegService;
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

public class AddBankServiceProvider {

    private final AddBankService addBankService;

    public AddBankServiceProvider(Context context) {
        addBankService = APIServiceFactory.createService(AddBankService.class, context);
    }

    public void getAddBankDetails(String user_id, String name, String bank_name, String acc_no, String ifsc, String acc_type, String branch, final APICallback apiCallback) {
        Call<AddBankModel> call = null;
        call = addBankService.getBankAddDetails(user_id,name,bank_name,acc_no,ifsc,acc_type,branch);
        String url = call.request().url().toString();

        call.enqueue(new Callback<AddBankModel>() {
            @Override
            public void onResponse(Call<AddBankModel> call, Response<AddBankModel> response) {
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
            public void onFailure(Call<AddBankModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
