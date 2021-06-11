package com.cognifygroup.vgold;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.getImageUpload.GetSingleImage0;
import com.cognifygroup.vgold.getImageUpload.GetSingleImageServiceProvider0;
import com.cognifygroup.vgold.updateProfile.UpdateUserModel;
import com.cognifygroup.vgold.updateProfile.UpdateUserServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import androidx.appcompat.widget.AppCompatImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.Url;

public class UpdateProfileActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edtAddress)
    EditText edtAddress;
    @InjectView(R.id.edtCity)
    EditText edtCity;
    @InjectView(R.id.edtState)
    EditText edtState;
    @InjectView(R.id.edtEmail)
    EditText edtEmail;

    @InjectView(R.id.edtAadhar)
    EditText edtAadhar;

    @InjectView(R.id.edtPan)
    EditText edtPan;

    @InjectView(R.id.edtMobileNumber)
    EditText edtMobileNumber;
    @InjectView(R.id.btn_submit_sign_up)
    Button btn_submit_sign_up;

    @InjectView(R.id.userImg)
    CircleImageView userImg;
    @InjectView(R.id.imgUserProfile)
    ImageView imgUserProfile;

    @InjectView(R.id.iv_aadharFront)
    AppCompatImageView iv_aadharFront;

    @InjectView(R.id.iv_aadharBack)
    AppCompatImageView iv_aadharBack;

    @InjectView(R.id.iv_pancard)
    AppCompatImageView iv_pancard;

    @InjectView(R.id.btnUploadImage)
    Button btnUploadImage;

    private String first_name, email, mobile_no, city, state;

    AlertDialogs mAlert;
    UpdateUserServiceProvider updateUserServiceProvider;
    GetSingleImageServiceProvider0 getSingleImageServiceProvider0;

    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;
    private static final int IMG_AADHAR_FRONT = 111, IMG_AADHAR_BACK = 222, IMG_PAN = 333;
    private Bitmap bitmapAadharFront, bitmapAadharBack, bitmapPanCard;
    public String ImageAadharFont = "", ImageAadharBack = "", ImagePanCard = "";
    private final int RESULT_CROP = 400;
    Uri uri;
    Boolean aBooleanAadhar_f = false;
    Boolean aBooleanAadhar_b = false;
    Boolean aBoolean_pan = false;
    //String name,email,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    public void init() {
        progressDialog = new TransparentProgressDialog(UpdateProfileActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        updateUserServiceProvider = new UpdateUserServiceProvider(this);
        getSingleImageServiceProvider0 = new GetSingleImageServiceProvider0(this);

        edtEmail.setText(VGoldApp.onGetEmail());
        edtMobileNumber.setText(VGoldApp.onGetNo());
        edtAddress.setText(VGoldApp.onGetAddress());
        edtCity.setText(VGoldApp.onGetCity());
        edtState.setText(VGoldApp.onGetState());
        // edtPan.setText(VGoldApp.onGetPanNo());


        Log.d("TAG", VGoldApp.onGetUserImg());

        if (VGoldApp.onGetUserImg() != null && !TextUtils.isEmpty(VGoldApp.onGetUserImg()) && !VGoldApp.onGetUserImg().equalsIgnoreCase("null")) {

            Glide.with(this).load(VGoldApp.onGetUserImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(userImg);

            /*Picasso.with(UpdateProfileActivity.this)
                    .load(VGoldApp.onGetUserImg())
                    .fit()
                    .into(userImg, new Callback() {
                        @Override
                        public void onSuccess() {
                        *//*if (progressbar_category !=null){
                            progressbar_category.setVisibility(View.GONE);
                        }*//*
                        }

                        @Override
                        public void onError() {
                            Log.d("TAG", "Error while setting");
                        }
                    });*/
        }

        loginStatusServiceProvider = new LoginStatusServiceProvider(this);
        checkLoginSession();

        iv_aadharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // crop image
                aBooleanAadhar_f = true;
                aBooleanAadhar_b = false;
                aBoolean_pan = false;
                CropImage.startPickImageActivity(UpdateProfileActivity.this);
            }
        });

        iv_aadharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // crop image
                aBooleanAadhar_b = true;
                aBooleanAadhar_f = false;
                aBoolean_pan = false;
                CropImage.startPickImageActivity(UpdateProfileActivity.this);
            }
        });

        iv_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // crop image
                aBoolean_pan = true;
                aBooleanAadhar_b = false;
                aBooleanAadhar_f = false;
                CropImage.startPickImageActivity(UpdateProfileActivity.this);
            }
        });

        btnUploadImage.setVisibility(View.GONE);
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDocument(VGoldApp.onGetUerId(), ImagePanCard, ImageAadharBack, ImageAadharFont);
            }
        });
    }

    private void checkLoginSession() {
        loginStatusServiceProvider.getLoginStatus(VGoldApp.onGetUerId(), new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    progressDialog.hide();
                    String status = ((LoginSessionModel) serviceResponse).getStatus();
                    String message = ((LoginSessionModel) serviceResponse).getMessage();
                    Boolean data = ((LoginSessionModel) serviceResponse).getData();

                    Log.i("TAG", "onSuccess: " + status);
                    Log.i("TAG", "onSuccess: " + message);

                    if (status.equals("200")) {

                        if (!data) {
                            AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(AddGoldActivity.this, message);

                    }
                } catch (Exception e) {
                    //  progressDialog.hide();
                    e.printStackTrace();
                } finally {
                    //  progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    progressDialog.hide();
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(UpdateProfileActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);


    }

    @OnClick(R.id.imgUserProfile)
    public void ImageUpload() {

        selectImage();

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    private String imagetostring() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // crop image
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                uri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCrop(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (aBooleanAadhar_f) {
                    iv_aadharFront.setImageURI(result.getUri());
                    try {
                        bitmapAadharFront = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                        ImageAadharFont = imagetostring(bitmapAadharFront);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (aBooleanAadhar_b) {
                    iv_aadharBack.setImageURI(result.getUri());
                    try {
                        bitmapAadharBack = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                        ImageAadharBack = imagetostring(bitmapAadharBack);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (aBoolean_pan) {
                    iv_pancard.setImageURI(result.getUri());
                    try {
                        bitmapPanCard = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                        ImagePanCard = imagetostring(bitmapPanCard);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    @OnClick(R.id.btn_submit_sign_up)
    public void onClickOfBtnRegister() {

        first_name = edtAddress.getText().toString();
        email = edtEmail.getText().toString();
        mobile_no = edtMobileNumber.getText().toString();
        city = edtCity.getText().toString();
        state = edtState.getText().toString();

        if (first_name.length() == 0 && email.length() == 0 && mobile_no.length() == 0 && city.length() == 0 && state.length() == 0) {
            AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", "All data required",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(UpdateProfileActivity.this, "All data required");
        } else if (first_name.length() == 0) {
            edtAddress.setError("Enter Address");
        } else if (city.length() == 0) {
            edtCity.setError("Enter city");
        } else if (state.length() == 0) {
            edtState.setError("Enter state");
        } else if (email.length() == 0) {
            edtEmail.setError("Enter valid email");
        } else if (mobile_no.length() < 10) {
            edtMobileNumber.setError("Enter 10 digit contact number");
        } /*else if (edtAadhar.getText().toString().length() < 12) {
          //  edtAadhar.setError("Enter 12 digit Aadhar Number");
        }*/ else {
            AttemptToUpdateUser(VGoldApp.onGetUerId(), email, mobile_no, first_name, city, state, edtAadhar.getText().toString(), edtPan.getText().toString());
        }
    }


    private void AttemptToUpdateUser(String user_id, final String email, final String no, final String address,
                                     final String city, final String state, final String aadhar_no, final String pan_no) {
        progressDialog.show();

        updateUserServiceProvider.getReg(user_id, email, no, address, city, state, aadhar_no, pan_no, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((UpdateUserModel) serviceResponse).getStatus();
                    String message = ((UpdateUserModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(UpdateProfileActivity.this, message);

                        if (TextUtils.isEmpty(ImagePanCard) && TextUtils.isEmpty(ImageAadharBack) && TextUtils.isEmpty(ImageAadharFont)){
                            AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", message,
                                    getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
                        }else {
                            uploadDocument(VGoldApp.onGetUerId(), ImagePanCard, ImageAadharBack, ImageAadharFont);
                        }



                    } else {
                        AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(UpdateProfileActivity.this, message);
                       /* Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(UpdateProfileActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToUploadSingleImageApi0(String user_id, String image) {
        progressDialog.show();
        getSingleImageServiceProvider0.getReg(user_id, image, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetSingleImage0) serviceResponse).getStatus();
                    String message = ((GetSingleImage0) serviceResponse).getMessage();
                    String url = ((GetSingleImage0) serviceResponse).getPath();

                    if (status.equals("200")) {
                        progressDialog.hide();
                        VGoldApp.onSetUserDetails(VGoldApp.onGetUerId(), VGoldApp.onGetFirst(),
                                VGoldApp.onGetLast(), VGoldApp.onGetEmail(),
                                VGoldApp.onGetNo(), VGoldApp.onGetQrCode(),
                                VGoldApp.onGetPanNo(), VGoldApp.onGetAddress(),
                                VGoldApp.onGetCity(), VGoldApp.onGetState(),
                                url,
                                VGoldApp.onGetIsCP());
                        AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", "Image Uploaded Successfully",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(UpdateProfileActivity.this, "Image Uploaded Successfully");
                    } else {
//                        mAlert.onShowToastNotification(UpdateProfileActivity.this, message);
                        AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(UpdateProfileActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void uploadDocument(String user_id, String identity_proff, String aadhar_back, String aadhar_front) {
        progressDialog.show();
        getSingleImageServiceProvider0.uploadImage(user_id, identity_proff, aadhar_back, aadhar_front, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetSingleImage0) serviceResponse).getStatus();
                    String message = ((GetSingleImage0) serviceResponse).getMessage();
                    String url = ((GetSingleImage0) serviceResponse).getPath();

                    if (status.equals("200")) {
                        progressDialog.hide();
                       /* VGoldApp.onSetUserDetails(VGoldApp.onGetUerId(), VGoldApp.onGetFirst(),
                                VGoldApp.onGetLast(), VGoldApp.onGetEmail(),
                                VGoldApp.onGetNo(), VGoldApp.onGetQrCode(),
                                VGoldApp.onGetPanNo(), VGoldApp.onGetAddress(),
                                VGoldApp.onGetCity(), VGoldApp.onGetState(),
                                url,
                                VGoldApp.onGetIsCP());*/
                        AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", "Image Uploaded Successfully",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(UpdateProfileActivity.this, "Image Uploaded Successfully");
                    } else {
//                        mAlert.onShowToastNotification(UpdateProfileActivity.this, message);
                        AlertDialogs.alertDialogOk(UpdateProfileActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {
                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(UpdateProfileActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(UpdateProfileActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 1:
                Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                startActivity(intent);

                VGoldApp.onSetUserDetails(VGoldApp.onGetUerId(), VGoldApp.onGetFirst(),
                        VGoldApp.onGetLast(), email, mobile_no, VGoldApp.onGetQrCode(),
                        VGoldApp.onGetPanNo(),
                        first_name, city, state,
                        VGoldApp.onGetUserImg(), VGoldApp.onGetIsCP()
                );
                break;

            case 11:
                Intent LogIntent = new Intent(UpdateProfileActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }

    private String imagetostring(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte, Base64.NO_WRAP);
    }


}
