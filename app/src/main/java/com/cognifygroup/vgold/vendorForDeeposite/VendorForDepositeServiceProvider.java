package com.cognifygroup.vgold.vendorForDeeposite;

import android.content.Context;

import com.cognifygroup.vgold.getBankDetails.GetBankModel;
import com.cognifygroup.vgold.getBankDetails.GetBankService;
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

public class VendorForDepositeServiceProvider {

    private final VendorForDepositeService vendorForDepositeService;

    public VendorForDepositeServiceProvider(Context context) {
        vendorForDepositeService = APIServiceFactory.createService(VendorForDepositeService.class, context);
    }

    public void getVendorForDeposite(final APICallback apiCallback) {
        Call<VendorForDepositeModel> call = null;
        call = vendorForDepositeService.addGold();
        String url = call.request().url().toString();

        call.enqueue(new Callback<VendorForDepositeModel>() {
            @Override
            public void onResponse(Call<VendorForDepositeModel> call, Response<VendorForDepositeModel> response) {
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
            public void onFailure(Call<VendorForDepositeModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
