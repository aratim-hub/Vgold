package com.cognifygroup.vgold.getVendorOffer;

import android.content.Context;

import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryService;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 6/16/18.
 */

public class VendorOfferServiceProvider {
    private final VendorOfferService vendorOfferService;

    public VendorOfferServiceProvider(Context context) {
        vendorOfferService = APIServiceFactory.createService(VendorOfferService.class, context);
    }

    public void getGoldBookingHistory(final APICallback apiCallback) {
        Call<VendorOfferModel> call = null;
        call = vendorOfferService.vendorOffer();
        String url = call.request().url().toString();

        call.enqueue(new Callback<VendorOfferModel>() {
            @Override
            public void onResponse(Call<VendorOfferModel> call, Response<VendorOfferModel> response) {
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
            public void onFailure(Call<VendorOfferModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}

