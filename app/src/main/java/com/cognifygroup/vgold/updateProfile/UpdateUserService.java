package com.cognifygroup.vgold.updateProfile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 7/26/18.
 */

public interface UpdateUserService {

    @POST("update_profile.php?")
    @FormUrlEncoded
    Call<UpdateUserModel> updateUser(@Field("user_id") String user_id,
                                     @Field("email") String email,
                                     @Field("no") String no,
                                     @Field("address") String address,
                                     @Field("city") String city,
                                     @Field("state") String state);
}
