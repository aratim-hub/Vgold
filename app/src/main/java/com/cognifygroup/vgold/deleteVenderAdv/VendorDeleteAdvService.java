package com.cognifygroup.vgold.deleteVenderAdv;

import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 8/21/18.
 */

public interface VendorDeleteAdvService {

    @POST("vendor_list.php?")
    @FormUrlEncoded
    Call<VendorAdvModel> vendorDelete(@Field("operation") String operation, @Field("vendor_id") String venderId);

    @POST("vendor_list.php?")
    @FormUrlEncoded
    Call<VendorAdvModel> vendorAdd(@Field("operation") String operation, @Field("vendor_id") String venderId, @Field("advertisement_path") String imagePath);

   /* @POST("vendor_list.php?")
    @FormUrlEncoded
    Call<VendorAdvModel> vendorUpdate(@Field("operation") String operation, @Field("vendor_id") String venderId, @Field("advertisement_path") String imagePath);*/
}
