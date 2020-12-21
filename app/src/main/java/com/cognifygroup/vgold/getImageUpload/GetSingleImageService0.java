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

}
