package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.getImageUpload.GetSingleImage0;
import com.cognifygroup.vgold.getImageUpload.GetSingleImageServiceProvider0;
import com.cognifygroup.vgold.register.RegModel;
import com.cognifygroup.vgold.register.RegServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements AlertDialogOkListener {

    AlertDialogs mAlert;
    RegServiceProvider regServiceProvider;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    @InjectView(R.id.edtfirst)
    MaterialEditText edtfirst;
    @InjectView(R.id.tb_subservices)
    Toolbar tb_subservices;
    @InjectView(R.id.edtlast1)
    MaterialEditText edtlast1;
    @InjectView(R.id.edtmail)
    MaterialEditText edtmail;
    @InjectView(R.id.edtno)
    MaterialEditText edtno;
    /* @InjectView(R.id.edtPass)
     MaterialEditText edtPass;*/
    @InjectView(R.id.edtPancard)
    MaterialEditText edtPancard;
    @InjectView(R.id.edtReferCode)
    MaterialEditText edtReferCode;

    @InjectView(R.id.edtAadarCard)
    MaterialEditText edtAadarCard;

    @InjectView(R.id.iv_aadharFront)
    AppCompatImageView iv_aadharFront;

    @InjectView(R.id.iv_aadharBack)
    AppCompatImageView iv_aadharBack;

    @InjectView(R.id.iv_pancard)
    AppCompatImageView iv_pancard;

    @InjectView(R.id.btnUploadImage)
    Button btnUploadImage;

    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private static final int IMG_AADHAR_FRONT = 111, IMG_AADHAR_BACK = 222, IMG_PAN = 333;
    private Bitmap bitmapAadharFront, bitmapAadharBack, bitmapPanCard;
    public String ImageAadharFont = "", ImageAadharBack = "", ImagePanCard = "";
    GetSingleImageServiceProvider0 getSingleImageServiceProvider0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        setSupportActionBar(tb_subservices);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tb_subservices.setContentInsetStartWithNavigation(0);

        getSingleImageServiceProvider0 = new GetSingleImageServiceProvider0(this);


        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        progressDialog = new TransparentProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        regServiceProvider = new RegServiceProvider(this);

        checkDeepLink();

        iv_aadharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMG_AADHAR_FRONT);
            }
        });

        iv_aadharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMG_AADHAR_BACK);
            }
        });

        iv_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMG_PAN);
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDocument(VGoldApp.onGetUerId(), ImagePanCard, ImageAadharBack, ImageAadharFont);
            }
        });
    }

    private void checkDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null) {
                            String deepLink = String.valueOf(pendingDynamicLinkData.getLink());

                            if (deepLink != null && !TextUtils.isEmpty(deepLink)) {
                                String refCode = deepLink.substring(deepLink.lastIndexOf("=") + 1);
//                                Log.d("ReferCodeRegister", refCode);
                                edtReferCode.setText(refCode);
                                edtReferCode.setEnabled(false);
                            } else {
                                edtReferCode.setText("");
                                edtReferCode.setEnabled(true);
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "getDynamicLink:onFailure", e);
                    }
                });
    }

    @OnClick(R.id.btnRegister)
    public void onClickOfBtnRegister() {
        String first = edtfirst.getText().toString().trim();
        String last = edtlast1.getText().toString().trim();
        String email = edtmail.getText().toString().trim();
        String no = edtno.getText().toString().trim();
//        String pass = edtPass.getText().toString();
        String pancard = edtPancard.getText().toString().trim();
        String refercode = edtReferCode.getText().toString().trim();

        if (first.length() == 0 && last.length() == 0 && email.length() == 0 && no.length() == 0 && pancard.length() == 0) {
            AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "All data required",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(RegisterActivity.this, "All data required");
        } else if (first.length() == 0) {
            edtfirst.setError("Enter name");
        } else if (last.length() == 0) {
            edtlast1.setError("Enter last name");
        } else if (email.length() == 0) {
            edtmail.setError("Enter valid email");
        } else if (no.length() < 10) {
            edtno.setError("Enter 10 digit contact number");
        } /*else if (edtPass.length() == 0) {
            edtPass.setError("Enter confirm password");
        } else if (edtPancard.length() == 0) {
            edtPancard.setError("Enter vaild Pancard Number");
        } else if (ImageAadharFont.length() == 0) {
            Toast.makeText(this, "Please add aadhar front pic", Toast.LENGTH_SHORT).show();
        } else if (ImageAadharBack.length() == 0) {
            Toast.makeText(this, "Please add aadhar back pic", Toast.LENGTH_SHORT).show();
        } else if (ImagePanCard.length() == 0) {
            Toast.makeText(this, "Please add pan card pic", Toast.LENGTH_SHORT).show();
        } else if (edtAadarCard.getText().toString().length() < 12) {
            Toast.makeText(this, "Please inter valid aadhar no", Toast.LENGTH_SHORT).show();
        }*/ else {
//            AttemptToRegisterApi(first, last, email, no, pass, pancard, refercode);
            AttemptToRegisterApi(first, last, email, no, pancard, refercode,
                    edtAadarCard.getText().toString(),
                    "",
                    "",
                    "");
        }
    }

    private void AttemptToRegisterApi(String first, String last, String email, String no, String pancard, String refer_code, String aadhar_no, String aadharF, String aadharB, String panCardPic) {
        progressDialog.show();
        regServiceProvider.getReg(first, last, email, no, pancard, refer_code,
                aadhar_no, aadharF, aadharB, panCardPic, new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        try {
                            String status = ((RegModel) serviceResponse).getStatus();
                            String message = ((RegModel) serviceResponse).getMessage();
                            int user_id = ((RegModel) serviceResponse).getData().getUserID();

                            if (status.equals("200")) {

                                if (String.valueOf(user_id) != null) {
                                    Log.i("TAG", "onSuccess: " + user_id);
                                    uploadDocument(String.valueOf(user_id), ImagePanCard, ImageAadharBack, ImageAadharFont);
                                }


                              /*  AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "Registration Successfully done",
                                        getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
*/
//                        mAlert.onShowToastNotification(RegisterActivity.this, "Registration Successfully done");
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
                            } else {
                                AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(RegisterActivity.this, message);
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
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
                                Log.i("TAGTAG", "onFailure: " + ((BaseServiceResponseModel) apiErrorModel).getMessage());
                                if (((BaseServiceResponseModel) apiErrorModel).getMessage() == null || TextUtils.isEmpty(((BaseServiceResponseModel) apiErrorModel).getMessage()) ||
                                        ((BaseServiceResponseModel) apiErrorModel).getMessage().equalsIgnoreCase("null")) {

                                    AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "Uploaded images are too large",
                                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                                }
                            } else {
                                PrintUtil.showNetworkAvailableToast(RegisterActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("TAGTAG", "onFailure: " + e.getMessage());
                            PrintUtil.showNetworkAvailableToast(RegisterActivity.this);
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
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_AADHAR_FRONT && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmapAadharFront = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                iv_aadharFront.setImageBitmap(bitmapAadharFront);
                ImageAadharFont = imagetostring(bitmapAadharFront);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == IMG_AADHAR_BACK && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmapAadharBack = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                iv_aadharBack.setImageBitmap(bitmapAadharBack);
                ImageAadharBack = imagetostring(bitmapAadharBack);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == IMG_PAN && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmapPanCard = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                iv_pancard.setImageBitmap(bitmapPanCard);
                ImagePanCard = imagetostring(bitmapPanCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String imagetostring(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte, Base64.NO_WRAP);
    }

    private void uploadDocument(String user_id, String identity_proff, String aadhar_back, String aadhar_front) {
        progressDialog.show();
        getSingleImageServiceProvider0.uploadImage(user_id, identity_proff, aadhar_back, aadhar_front, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    Log.i("TAG", "onSuccess: ");
                    String status = ((GetSingleImage0) serviceResponse).getStatus();
                    String message = ((GetSingleImage0) serviceResponse).getMessage();
                    String url = ((GetSingleImage0) serviceResponse).getPath();

                    if (status.equals("200")) {
                        Log.i("TAG", "onSuccess: ");
                        progressDialog.hide();
                        AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "Image Uploaded Successfully",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

                        AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "Registration Successfully done",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
                    } else {
                        AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

                        AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "Registration Successfully done",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
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
                        PrintUtil.showToast(RegisterActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(RegisterActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(RegisterActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }
}
