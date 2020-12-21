package com.cognifygroup.vgold.vendorForDeeposite;

import com.cognifygroup.vgold.getBankDetails.GetBankModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 8/21/18.
 */

public interface VendorForDepositeService {

    @POST("vendor_for_deposite.php?")
    Call<VendorForDepositeModel> addGold();
}
