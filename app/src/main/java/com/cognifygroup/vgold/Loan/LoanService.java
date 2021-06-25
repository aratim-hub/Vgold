package com.cognifygroup.vgold.Loan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 6/15/18.
 */

public interface LoanService {

    @POST("get_user_loan_eligiblity.php?")
    @FormUrlEncoded
    Call<LoanModel> getEligibility(@Field("user_id") String user_id);


    @POST("save_loan_request.php?")
    @FormUrlEncoded
    Call<LoanModel> applyLoan(@Field("user_id") String user_id,
                              @Field("amount") String amt,
                              @Field("comment") String comment);
}
