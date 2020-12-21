package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalModel;
import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalServiceProvider;
import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpModel;
import com.cognifygroup.vgold.sellGold.SellGoldModel;
import com.cognifygroup.vgold.sellGold.SellGoldServiceProvider;
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalModel;
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Otp1Activity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edtOtp)
    EditText edtOtp;
    @InjectView(R.id.btnTransferGold)
    Button btnTransferGold;

    String otp = "", amount = "", no = "", weight = "", moveFrom = "";

    private TransferGoldFinalServiceProvider transferGoldFinalServiceProvider;
    SellGoldServiceProvider sellGoldServiceProvider;
    private AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp1);
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

        progressDialog = new TransparentProgressDialog(Otp1Activity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        transferGoldFinalServiceProvider = new TransferGoldFinalServiceProvider(this);
        sellGoldServiceProvider = new SellGoldServiceProvider(this);

//        if (getIntent().hasExtra("OTP")) {
        otp = getIntent().getStringExtra("OTP");
        amount = getIntent().getStringExtra("AMOUNT");
        no = getIntent().getStringExtra("NO");
        weight = getIntent().getStringExtra("Weight");
        moveFrom = getIntent().getStringExtra("moveFrom");
//        }

        if (moveFrom.equals("SellGold")) {
            btnTransferGold.setText("Sell");
        } else {
            btnTransferGold.setText("Transfer");
        }
    }


    @OnClick(R.id.btnTransferGold)
    public void onClickOfbtnTransferGold() {

        if (moveFrom.equals("SellGold")) {
            String OTP = edtOtp.getText().toString();
            if (!TextUtils.isEmpty(OTP)) {
                verifyOTP(VGoldApp.onGetUerId(), OTP);
            }
        } else {
            if (otp.equals(edtOtp.getText().toString())) {

                TransferGold(VGoldApp.onGetUerId(), no, weight);

            } else {
                AlertDialogs.alertDialogOk(Otp1Activity.this, "Alert", "Otp not Matched",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(Otp1Activity.this, "Otp not Matched");
            }
        }

    }

    private void verifyOTP(String user_id, String otp) {

        progressDialog.show();
        sellGoldServiceProvider.verifyOTP(user_id, "check_otp", otp, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((SellGoldModel) serviceResponse).getStatus();
                String message = ((SellGoldModel) serviceResponse).getMessage();
                try {
                    if (Status.equals("200")) {

                        AttemptToSellGold(VGoldApp.onGetUerId(), weight, amount);
                    } else {
                        AlertDialogs.alertDialogOk(Otp1Activity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivity.this, message);
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
                        PrintUtil.showToast(Otp1Activity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(Otp1Activity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(Otp1Activity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }

    private void AttemptToSellGold(String user_id, String gold, String amount) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        sellGoldServiceProvider.getAddBankDetails(user_id, gold, amount, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((SellGoldModel) serviceResponse).getStatus();
                    String message = ((SellGoldModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
                        Intent intent = new Intent(Otp1Activity.this, SuccessActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                        finish();
                    } else {

                        AlertDialogs.alertDialogOk(Otp1Activity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
//                        Intent intent = new Intent(SellGoldActivity.this, MainActivity.class);
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
                        PrintUtil.showToast(Otp1Activity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(Otp1Activity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(Otp1Activity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void TransferGold(String user_id, String no, String amount) {

        progressDialog.show();
        transferGoldFinalServiceProvider.transferMoney(user_id, no, amount, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((TransferGoldFinalModel) serviceResponse).getStatus();
                String message = ((TransferGoldFinalModel) serviceResponse).getMessage();
                try {
                    if (Status.equals("200")) {
                        AlertDialogs.alertDialogOk(Otp1Activity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);


//                        mAlert.onShowToastNotification(Otp1Activity.this, message);
//                        startActivity(new Intent(Otp1Activity.this,MainActivity.class));

                    } else {
                        AlertDialogs.alertDialogOk(Otp1Activity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(Otp1Activity.this, message);
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
                        PrintUtil.showToast(Otp1Activity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(Otp1Activity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(Otp1Activity.this);
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
                startActivity(new Intent(Otp1Activity.this, MainActivity.class));
                break;
        }
    }
}

