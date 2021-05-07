package com.cognifygroup.vgold.AddComplain;

import com.cognifygroup.vgold.AddBank.AddBankModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface AddComplainService {

    @POST("process_complaints.php?")
    @FormUrlEncoded
    Call<AddComplainModel> AddComplain(@Field("user_id") String user_id,
                                         @Field("complaint") String complaint);

    @POST("user_notes.php?")
    @FormUrlEncoded
    Call<AddComplainModel> AddReview(@Field("user_id") String user_id,
                                       @Field("notes") String complaint);

    @POST("user_feedback.php?")
    @FormUrlEncoded
    Call<AddComplainModel> AddFeedback(@Field("user_id") String user_id,
                                       @Field("description") String desc,
                                       @Field("points") String rating);
}
