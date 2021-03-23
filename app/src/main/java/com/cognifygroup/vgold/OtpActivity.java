package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalModel;
import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalServiceProvider;
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpServiceProvider;
import com.cognifygroup.vgold.transferMoney.TransferMoneyModel;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class OtpActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edtOtp)
    EditText edtOtp;
    @InjectView(R.id.btnTransferMoney)
    Button btnTransferMoney;

    String otp = "", amount = "", no = "";

    private TransferMoneyFinalServiceProvider transferMoneyFinalServiceProvider;
    private AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
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

    public void init() {

        progressDialog = new TransparentProgressDialog(OtpActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        transferMoneyFinalServiceProvider = new TransferMoneyFinalServiceProvider(this);

        if (getIntent().hasExtra("OTP")) {

            otp = getIntent().getStringExtra("OTP");
            amount = getIntent().getStringExtra("AMOUNT");
            no = getIntent().getStringExtra("NO");
        }

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
                            AlertDialogs.alertDialogOk(OtpActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(OtpActivity.this, "Alert", message,
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
                        PrintUtil.showToast(OtpActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OtpActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OtpActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @OnClick(R.id.btnTransferMoney)
    public void onClickOfBtnTransferMoney() {

        if (otp.equals(edtOtp.getText().toString())) {

            TransferMoney(VGoldApp.onGetUerId(), no, amount);

        } else {
            AlertDialogs.alertDialogOk(OtpActivity.this, "Alert", "Otp not Matched",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(OtpActivity.this, "Otp not Matched");
        }

    }


    private void TransferMoney(String user_id, String no, String amount) {

        progressDialog.show();
        transferMoneyFinalServiceProvider.transferMoney(user_id, no, amount, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((TransferMoneyFinalModel) serviceResponse).getStatus();
                String message = ((TransferMoneyFinalModel) serviceResponse).getMessage();
                try {
                    if (Status.equals("200")) {
                        AlertDialogs.alertDialogOk(OtpActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);


                       /* mAlert.onShowToastNotification(OtpActivity.this, message);
                        startActivity(new Intent(OtpActivity.this,MainActivity.class));*/

                    } else {
                        AlertDialogs.alertDialogOk(OtpActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(OtpActivity.this, message);
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
                        PrintUtil.showToast(OtpActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OtpActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OtpActivity.this);
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
                startActivity(new Intent(OtpActivity.this, MainActivity.class));
                break;
            case 11:
                Intent LogIntent = new Intent(OtpActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
