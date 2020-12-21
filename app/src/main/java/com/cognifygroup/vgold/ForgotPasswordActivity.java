package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.AddBank.AddBankServiceProvider;
import com.cognifygroup.vgold.getOtp.OtpModel;
import com.cognifygroup.vgold.getOtp.OtpServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edtEmail)
    EditText edtEmail;
    @InjectView(R.id.BtnSubmitEmail)
    Button BtnSubmitEmail;

    AlertDialogs mAlert;
    OtpServiceProvider otpServiceProvider;

    String email;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        progressDialog = new TransparentProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        otpServiceProvider = new OtpServiceProvider(this);

    }


    @OnClick(R.id.BtnSubmitEmail)
    public void onClickofBtnSubmitEmail() {


        email = edtEmail.getText().toString();

        if (edtEmail.getText().toString().length() == 0) {
            AlertDialogs.alertDialogOk(ForgotPasswordActivity.this, "Alert", "Enter Valid Email or Mobile No",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(ForgotPasswordActivity.this, "Enter Valid Email or Mobile No");
        } else {
            OtpApi(email);
        }
    }


    private void OtpApi(String id) {

        progressDialog.show();
        otpServiceProvider.getReg(id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((OtpModel) serviceResponse).getStatus();
                try {
                    String message = ((OtpModel) serviceResponse).getMessage();
                    if (Status.equals("200")) {

                        AlertDialogs.alertDialogOk(ForgotPasswordActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ForgotPasswordActivity.this, message);
//                        startActivity(new Intent(ForgotPasswordActivity.this,ChangePasswordActivity.class).putExtra("id",email));


                    } else {
                        AlertDialogs.alertDialogOk(ForgotPasswordActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(ForgotPasswordActivity.this, message);
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
                        PrintUtil.showToast(ForgotPasswordActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ForgotPasswordActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ForgotPasswordActivity.this);
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
                startActivity(new Intent(ForgotPasswordActivity.this, ChangePasswordActivity.class).putExtra("id", email));
                break;
        }
    }
}
