package com.cognifygroup.vgold.CheckLoginStatus;

import com.cognifygroup.vgold.addGold.AddGoldModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface CheckUserSession {

    @POST("login_check.php?")
    @FormUrlEncoded
    Call<LoginSessionModel> checkSession(@Field("user_id") String user_id);
}
