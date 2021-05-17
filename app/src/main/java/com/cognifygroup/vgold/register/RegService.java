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
                          @Field("pancard") String pancard,
                          @Field("refer_code") String refer_code,
                          @Field("identity_proff") String identity_proff,
                          @Field("aadhar_front") String aadhar_front,
                          @Field("aadhar_back") String aadhar_back,
                          @Field("aadhar_no") String aadhar_no);
}
