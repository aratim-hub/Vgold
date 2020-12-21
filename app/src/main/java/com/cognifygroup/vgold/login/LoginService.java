package com.cognifygroup.vgold.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by USER1 on 1/15/2018.
 */

public interface LoginService {

    @POST("login.php?")
    @FormUrlEncoded
    Call<LoginModel> getLogin(@Field("email") String contact_no);

    @POST("login.php?")
    @FormUrlEncoded
    Call<LoginModel> verifyLogin(@Field("email") String contact_no, @Field("otp") String otp, @Field("token") String token);

}
