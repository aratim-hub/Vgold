package com.cognifygroup.vgold;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.androidhive.barcode.BarcodeReader;

public class SuccessActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.ImageView)
    ImageView ImageView;
    @InjectView(R.id.txtb)
    TextView txtb;
    @InjectView(R.id.btnOk)
    Button btnOk;

    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    private TransparentProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.inject(this);


           Glide.with(this).load("https://thumbs.gfycat.com/CourteousPhonyHorsemouse-size_restricted.gif").into(ImageView);

           if (getIntent().hasExtra("message")){
               String message=getIntent().getStringExtra("message");
               txtb.setText(message);
               txtb.setTextSize(12);

           }


        progressDialog = new TransparentProgressDialog(SuccessActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        loginStatusServiceProvider = new LoginStatusServiceProvider(this);
        checkLoginSession();
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
                            AlertDialogs.alertDialogOk(SuccessActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(SuccessActivity.this, "Alert", message,
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
                        PrintUtil.showToast(SuccessActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(SuccessActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(SuccessActivity.this);
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

    @OnClick(R.id.btnOk)
    public void onClickOfBtnok(){

        Intent intent=new Intent(SuccessActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 11:
                Intent LogIntent = new Intent(SuccessActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
