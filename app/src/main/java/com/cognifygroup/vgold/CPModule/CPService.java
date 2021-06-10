package com.cognifygroup.vgold.CPModule;

import com.cognifygroup.vgold.ChannelPartner.UserCommissionDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIStatusDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserGoldDetailsModel;
import com.cognifygroup.vgold.getOtp.OtpModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 7/26/18.
 */

public interface CPService {

    @POST("cp_user_list.php?")
    @FormUrlEncoded
    Call<UserDetailsModel> getCPUserDetails(@Field("user_id") String id);

    @POST("cp_gold_booking_history.php?")
    @FormUrlEncoded
    Call<UserGoldDetailsModel> getCPUserGoldDetails(@Field("user_id") String id);

    @POST("cp_commission_list.php?")
    @FormUrlEncoded
    Call<UserCommissionDetailsModel> getCPUserCommissionDetails(@Field("user_id") String id);

    @POST("cp_gold_booking_emi.php?")
    @FormUrlEncoded
    Call<UserEMIDetailsModel> getCPUserEMIDetails(@Field("user_id") String id);

    @POST("cp_gold_booking_transactions.php?")
    @FormUrlEncoded
    Call<UserEMIStatusDetailsModel> getCPUserEMIStatusDetails(@Field("gold_booking_id") String id);
}
