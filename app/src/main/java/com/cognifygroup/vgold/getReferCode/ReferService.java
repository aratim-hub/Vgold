package com.cognifygroup.vgold.getReferCode;

import com.cognifygroup.vgold.AddBank.AddBankModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface ReferService {

    @POST("send_refer_link.php?")
    @FormUrlEncoded
    Call<ReferModel> getBankAddDetails(@Field("user_id") String user_id,
                                       @Field("name") String name,
                                       @Field("email") String email,
                                       @Field("mobile_no") String mobile_no,
                                       @Field("refer_link") String referLink);

    @POST("get_referal_code.php?")
    @FormUrlEncoded
    Call<ReferModel> getReferenceCode(@Field("user_id") String user_id);

    @POST("membership_upgrade.php?")
    @FormUrlEncoded
    Call<ReferModel> getWalletPoint(@Field("user_id") String user_id, @Field("data") String data);
}
