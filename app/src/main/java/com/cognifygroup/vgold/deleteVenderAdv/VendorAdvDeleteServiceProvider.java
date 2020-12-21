package com.cognifygroup.vgold.deleteVenderAdv;

import android.content.Context;

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

public class VendorAdvDeleteServiceProvider {
    private final VendorDeleteAdvService vendorAdvService;

    public VendorAdvDeleteServiceProvider(Context context) {
        vendorAdvService = APIServiceFactory.createService(VendorDeleteAdvService.class, context);
    }

    public void getDeleteVenderAdv(String venderId, final APICallback apiCallback) {
        Call<VendorAdvModel> call = null;
        call = vendorAdvService.vendorDelete("delete", venderId);
        String url = call.request().url().toString();

        call.enqueue(new Callback<VendorAdvModel>() {
            @Override
            public void onResponse(Call<VendorAdvModel> call, Response<VendorAdvModel> response) {
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
            public void onFailure(Call<VendorAdvModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    public void addVenderAdv(String operation, String venderId, String imagePath, final APICallback apiCallback) {
        Call<VendorAdvModel> call = null;
        call = vendorAdvService.vendorAdd(operation, venderId,  imagePath);
        String url = call.request().url().toString();

        call.enqueue(new Callback<VendorAdvModel>() {
            @Override
            public void onResponse(Call<VendorAdvModel> call, Response<VendorAdvModel> response) {
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
            public void onFailure(Call<VendorAdvModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }

    /*public void updateVenderAdv(String venderId, String imagePath, final APICallback apiCallback) {
        Call<VendorAdvModel> call = null;
        call = vendorAdvService.vendorUpdate("update", venderId,  imagePath);
        String url = call.request().url().toString();

        call.enqueue(new Callback<VendorAdvModel>() {
            @Override
            public void onResponse(Call<VendorAdvModel> call, Response<VendorAdvModel> response) {
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
            public void onFailure(Call<VendorAdvModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }*/

}

