package com.cognifygroup.vgold.AddBank;

import com.cognifygroup.vgold.register.RegModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface AddBankService {

    @POST("bank_details.php?")
    @FormUrlEncoded
    Call<AddBankModel> getBankAddDetails( @Field("user_id") String user_id,
                                          @Field("name") String name,
                                          @Field("bank_name") String bank_name,
                                          @Field("acc_no") String acc_no,
                                          @Field("ifsc") String ifsc,
                                          @Field("acc_type") String acc_type,
                                          @Field("branch") String branch);
}
