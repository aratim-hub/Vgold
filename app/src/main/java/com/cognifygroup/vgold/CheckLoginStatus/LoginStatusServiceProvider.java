package com.cognifygroup.vgold.CheckLoginStatus;

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

public class LoginStatusServiceProvider {

    private final CheckUserSession userSession;

    public LoginStatusServiceProvider(Context context) {
        userSession = APIServiceFactory.createService(CheckUserSession.class, context);
    }

    public void getLoginStatus(String user_id, final APICallback apiCallback) {
        Call<LoginSessionModel> call = null;
        call = userSession.checkSession(user_id);
        String url = call.request().url().toString();

        call.enqueue(new Callback<LoginSessionModel>() {
            @Override
            public void onResponse(Call<LoginSessionModel> call, Response<LoginSessionModel> response) {
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
            public void onFailure(Call<LoginSessionModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
