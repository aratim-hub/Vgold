package com.cognifygroup.vgold.loginImage;

import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by shivraj on 8/21/18.
 */

public interface LoginImageService {

    @POST("fetch_login_image.php?")
    Call<LoginImageModel> getImage();

}
