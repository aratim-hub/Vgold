package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.ChangePassword.ChangePasswordModel;
import com.cognifygroup.vgold.ChangePassword.ChangePasswordServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChangePasswordActivity extends AppCompatActivity implements AlertDialogOkListener{
    AlertDialogs mAlert;
    ChangePasswordServiceProvider changePasswordServiceProvider;
    @InjectView(R.id.edtOtp)
    EditText edtOtp;
    @InjectView(R.id.edtPass)
    EditText edtPass;
    @InjectView(R.id.edtCpass)
    EditText edtCpass;
    @InjectView(R.id.btn_submit_otp)
    Button btn_submit_otp;

    String id, otp, pass;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.inject(this);
        init();
    }

    public void init() {
        mAlert = AlertDialogs.getInstance();

        progressDialog = new TransparentProgressDialog(ChangePasswordActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);


        changePasswordServiceProvider = new ChangePasswordServiceProvider(this);

        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }
    }

    @OnClick(R.id.btn_submit_otp)
    public void onClickOfSubmit() {
        otp = edtOtp.getText().toString();
        pass = edtPass.getText().toString();

        if (otp.length() == 0) {
            AlertDialogs.alertDialogOk(ChangePasswordActivity.this, "Alert", "Enter Valid Otp",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(ChangePasswordActivity.this, "Enter Valid Otp");
        } else if (pass.length() == 0) {
            AlertDialogs.alertDialogOk(ChangePasswordActivity.this, "Alert", "Enter Valid password",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(ChangePasswordActivity.this, "Enter Valid password");
        } else if (!pass.equals(edtCpass.getText().toString())) {
            AlertDialogs.alertDialogOk(ChangePasswordActivity.this, "Alert", "password and confirm password not matched",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(ChangePasswordActivity.this, "password and confirm password not matched");
        } else {
            ChangePasswordApi(id, otp, pass);
        }
    }


    private void ChangePasswordApi(String id, String otp, String pass) {

        progressDialog.show();
        changePasswordServiceProvider.changePassword(id, otp, pass, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((ChangePasswordModel) serviceResponse).getStatus();
                try {
                    String message = ((ChangePasswordModel) serviceResponse).getMessage();
                    if (Status.equals("200")) {

//                        mAlert.onShowToastNotification(ChangePasswordActivity.this, message);
                        AlertDialogs.alertDialogOk(ChangePasswordActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
//                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));

                    } else {
                        AlertDialogs.alertDialogOk(ChangePasswordActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(ChangePasswordActivity.this, message);
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
                        PrintUtil.showToast(ChangePasswordActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ChangePasswordActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ChangePasswordActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode){
            case 1:
                startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                break;
        }
    }
}

