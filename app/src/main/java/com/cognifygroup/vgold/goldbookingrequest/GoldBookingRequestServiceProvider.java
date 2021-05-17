package com.cognifygroup.vgold.goldbookingrequest;

import android.content.Context;

import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.addGold.AddGoldService;
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

public class GoldBookingRequestServiceProvider {

    private final GoldBookingRequestService goldBookingRequestService;

    public GoldBookingRequestServiceProvider(Context context) {
        goldBookingRequestService = APIServiceFactory.createService(GoldBookingRequestService.class, context);
    }

    public void getGoldBookingRequest(String user_id, String booking_value, String down_payment,
                                      String monthly, String rate, String gold_weight,
                                      String tennure, String pc, String payment_option,
                                      String bank_details, String tr_id, String cheque_no,
                                      String initBookingCharge,
                                      String disc, String booking_charge,
                                      final APICallback apiCallback) {
        Call<GoldBookingRequestModel> call = null;
        call = goldBookingRequestService.addGold(user_id, booking_value, down_payment, monthly, rate,
                gold_weight, tennure, pc, payment_option, bank_details, tr_id, cheque_no, initBookingCharge,
                disc, booking_charge);
        String url = call.request().url().toString();

        call.enqueue(new Callback<GoldBookingRequestModel>() {
            @Override
            public void onResponse(Call<GoldBookingRequestModel> call, Response<GoldBookingRequestModel> response) {
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
            public void onFailure(Call<GoldBookingRequestModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
