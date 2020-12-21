package com.cognifygroup.vgold.addGold;

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

public class AddGoldServiceProvider {

    private final AddGoldService addGoldService;

    public AddGoldServiceProvider(Context context) {
        addGoldService = APIServiceFactory.createService(AddGoldService.class, context);
    }

    public void getAddBankDetails(String user_id, String gold, String amount, String payment_option, String bank_details, String tr_id, String cheque_no, final APICallback apiCallback) {
        Call<AddGoldModel> call = null;
        call = addGoldService.addGold(user_id,gold,amount,payment_option,bank_details,tr_id,cheque_no);
        String url = call.request().url().toString();

        call.enqueue(new Callback<AddGoldModel>() {
            @Override
            public void onResponse(Call<AddGoldModel> call, Response<AddGoldModel> response) {
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
            public void onFailure(Call<AddGoldModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
