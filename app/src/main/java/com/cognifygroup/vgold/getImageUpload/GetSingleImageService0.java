package com.cognifygroup.vgold.getImageUpload;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 8/7/18.
 */

public interface GetSingleImageService0 {

    @FormUrlEncoded
    @POST("update_profile_photo.php?")
    Call<GetSingleImage0> getReg(@Field("user_id") String client_id,
                                 @Field("image") String image);


    @FormUrlEncoded
    @POST("update_profile_photo.php?")
    Call<GetSingleImage0> uploadImage(@Field("user_id") String client_id,
                                      @Field("identity_proff") String identity_proff,
                                      @Field("aadhar_back") String aadhar_back,
                                      @Field("aadhar_front") String aadhar_front);

}
