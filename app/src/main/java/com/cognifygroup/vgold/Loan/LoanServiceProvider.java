package com.cognifygroup.vgold.Loan;

import android.content.Context;

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

public class LoanServiceProvider {

    private final LoanService loanService;

    public LoanServiceProvider(Context context) {
        loanService = APIServiceFactory.createService(LoanService.class, context);
    }

    public void getLoanEligibility(String user_id, final APICallback apiCallback) {
        Call<LoanModel> call = null;
        call = loanService.getEligibility(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<LoanModel>() {
            @Override
            public void onResponse(Call<LoanModel> call, Response<LoanModel> response) {
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
            public void onFailure(Call<LoanModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void applyForLoan(String user_id, String amt, String comment, final APICallback apiCallback) {
        Call<LoanModel> call = null;
        call = loanService.applyLoan(user_id, amt, comment);
        String url = call.request().url().toString();

        call.enqueue(new Callback<LoanModel>() {
            @Override
            public void onResponse(Call<LoanModel> call, Response<LoanModel> response) {
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
            public void onFailure(Call<LoanModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
