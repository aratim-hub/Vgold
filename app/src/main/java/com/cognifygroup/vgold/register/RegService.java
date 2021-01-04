package com.cognifygroup.vgold.register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 3/15/18.
 */

public interface RegService {
    @POST("register.php?")
    @FormUrlEncoded
    Call<RegModel> getReg(@Field("first") String first,
                          @Field("last") String last,
                          @Field("email") String email,
                          @Field("no") String no,
//                          @Field("pass") String pass,
                          @Field("pancard") String pancard,
                          @Field("refer_code") String refer_code);
}
