package com.cognifygroup.vgold;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Adapter.VendorOfferAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.deleteVenderAdv.VendorAdvDeleteServiceProvider;
import com.cognifygroup.vgold.deleteVenderAdv.VendorAdvModel;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.cognifygroup.vgold.utils.Constant.IMAGE_URL;

public class OfferLetterActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.imgLetter)
    ImageView imgLetter;
    @InjectView(R.id.imgDelete)
    TextView imgDelete;
    @InjectView(R.id.txtUpdateAdv)
    TextView txtUpdateAdv;
    @InjectView(R.id.bottom_layout)
    LinearLayout bottom_layout;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.addAdv)
    TextView addAdv;
    String offerImg, offerImg1, BaseUrl;
    boolean flag = false;
    AlertDialogs mAlert;
    private VendorAdvDeleteServiceProvider vendorDeleteAdvServiceProvider;
    private String venderId;
    public static final int PICK_IMAGE = 1;
    private String flagVal = null;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_letter);
        ButterKnife.inject(this);
        init();
    }

    public void init() {
        progressDialog = new TransparentProgressDialog(OfferLetterActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        vendorDeleteAdvServiceProvider = new VendorAdvDeleteServiceProvider(this);

        if (getIntent().hasExtra("offer")) {
            offerImg = getIntent().getStringExtra("offer");
            offerImg1 = getIntent().getStringExtra("offer1");
            venderId = getIntent().getStringExtra("venderId");
        }

        /*if (offerImg1 == null || TextUtils.isEmpty(offerImg1)) {
            addAdv.setVisibility(View.VISIBLE);
            imgLetter.setVisibility(View.GONE);
        }else{
            addAdv.setVisibility(View.GONE);
            imgLetter.setVisibility(View.VISIBLE);
        }*/

        BaseUrl = IMAGE_URL + offerImg;

        Picasso.with(OfferLetterActivity.this)
                .load(BaseUrl).placeholder(R.mipmap.ic_launcher)
                .into(imgLetter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {

//                    bottom_layout.setVisibility(View.VISIBLE);


                    if (VGoldApp.onGetUserRole().equals("admin") || VGoldApp.onGetUserRole().equals("manager")) {
                        if (offerImg1 == null || TextUtils.isEmpty(offerImg1)) {
                            addAdv.setVisibility(View.VISIBLE);
                            imgLetter.setVisibility(View.GONE);
                            bottom_layout.setVisibility(View.GONE);
                        } else {
                            bottom_layout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        imgLetter.setVisibility(View.VISIBLE);
                        addAdv.setVisibility(View.GONE);
                        bottom_layout.setVisibility(View.GONE);
                    }

                    BaseUrl = IMAGE_URL + offerImg1;


                    Picasso.with(OfferLetterActivity.this)
                            .load(BaseUrl).placeholder(R.mipmap.ic_launcher)
                            .into(imgLetter);

                    flag = true;
                } else {

                    bottom_layout.setVisibility(View.GONE);
                    imgLetter.setVisibility(View.VISIBLE);
                    addAdv.setVisibility(View.GONE);

                    BaseUrl = IMAGE_URL + offerImg;

                    Picasso.with(OfferLetterActivity.this)
                            .load(BaseUrl).placeholder(R.mipmap.ic_launcher)
//                            .fit()
                            .into(imgLetter);
                    flag = false;
                }

            }
        });
    }

    @OnClick(R.id.imgDelete)
    public void onClickDelete() {
        DeleteAlert();
    }

    @OnClick(R.id.addAdv)
    public void onClickAddAdv() {
        flagVal = "add";

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    @OnClick(R.id.txtUpdateAdv)
    public void onClickUpdateAdv() {

        flagVal = "update";

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    private void DeleteAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DeleteVenderAdv();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }

    private void DeleteVenderAdv() {
        progressDialog.show();

        vendorDeleteAdvServiceProvider.getDeleteVenderAdv(venderId, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((VendorAdvModel) serviceResponse).getStatus();
                    String message = ((VendorAdvModel) serviceResponse).getMessage();

                    if (status.equals("200")) {
                        AlertDialogs.alertDialogOk(OfferLetterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);


                       /* Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
                        startActivity(intent);
                        finish();*/
                    } else {
                        AlertDialogs.alertDialogOk(OfferLetterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);
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
                        PrintUtil.showToast(OfferLetterActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OfferLetterActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OfferLetterActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AddVenderAdv(String advPath) {
        progressDialog.show();

        vendorDeleteAdvServiceProvider.addVenderAdv("add", venderId, advPath, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((VendorAdvModel) serviceResponse).getStatus();
                    String message = ((VendorAdvModel) serviceResponse).getMessage();
//                    ArrayList<VendorAdvModel.Data> mArrSaloonServices = ((VendorAdvModel) serviceResponse).getData();

                    if (status.equals("200")) {

                        AlertDialogs.alertDialogOk(OfferLetterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

                      /*  mAlert.onShowToastNotification(OfferLetterActivity.this, message);
                        Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
                        startActivity(intent);
                        finish();*/

                    } else {
                        AlertDialogs.alertDialogOk(OfferLetterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);
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
                        PrintUtil.showToast(OfferLetterActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OfferLetterActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OfferLetterActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void UpdateVenderAdv(String advPath) {
        progressDialog.show();

        vendorDeleteAdvServiceProvider.addVenderAdv("add", venderId, advPath, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((VendorAdvModel) serviceResponse).getStatus();
                    String message = ((VendorAdvModel) serviceResponse).getMessage();
//                    ArrayList<VendorAdvModel.Data> mArrSaloonServices = ((VendorAdvModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        AlertDialogs.alertDialogOk(OfferLetterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

                      /*  mAlert.onShowToastNotification(OfferLetterActivity.this, message);

                        Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
                        startActivity(intent);
                        finish();*/

                    } else {
                        AlertDialogs.alertDialogOk(OfferLetterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);
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
                        PrintUtil.showToast(OfferLetterActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OfferLetterActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OfferLetterActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            useImage(uri);
        }
    }

    void useImage(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String advPath = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                if (advPath != null && !TextUtils.isEmpty(advPath)) {
                    if (flagVal != null && !TextUtils.isEmpty(flagVal)) {
                        if (flagVal.equalsIgnoreCase("add"))
                            AddVenderAdv(advPath);
                        else if (flagVal.equalsIgnoreCase("update"))
                            UpdateVenderAdv(advPath);
                    }
                } else {
                    AlertDialogs.alertDialogOk(OfferLetterActivity.this, "Alert", "Please select Image",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                    mAlert.onShowToastNotification(OfferLetterActivity.this, "Please select Image");
                }
            }
//            Log.d("TAG", String.valueOf(bitmap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 1:
                Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
