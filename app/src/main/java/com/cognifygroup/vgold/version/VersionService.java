package com.cognifygroup.vgold.version;

import com.cognifygroup.vgold.loginImage.LoginImageModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 8/21/18.
 */

public interface VersionService {


    @POST("appversioncheck.php?")
    @FormUrlEncoded
    Call<VersionModel> getVersion(@Field("version") String version);
}
