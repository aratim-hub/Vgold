package com.cognifygroup.vgold.updateProfile;

import android.content.Context;

import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.APIServiceFactory;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shivraj on 7/26/18.
 */

public class UpdateUserServiceProvider {
    private final UpdateUserService updateUserService;

    public UpdateUserServiceProvider(Context context) {
        updateUserService = APIServiceFactory.createService(UpdateUserService.class, context);
    }

    public void getReg(String user_id,String email,String no, String address,String city,String state,String aadhar_no,String pan_no,final APICallback apiCallback) {
        Call<UpdateUserModel> call = null;
        call = updateUserService.updateUser(user_id,email,no,address,city,state,aadhar_no,pan_no);
        String url = call.request().url().toString();

        call.enqueue(new Callback<UpdateUserModel>() {
            @Override
            public void onResponse(Call<UpdateUserModel> call, Response<UpdateUserModel> response) {
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
            public void onFailure(Call<UpdateUserModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
