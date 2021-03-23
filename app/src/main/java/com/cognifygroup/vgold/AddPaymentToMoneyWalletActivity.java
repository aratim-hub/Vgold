package com.cognifygroup.vgold;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;


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


public class AddPaymentToMoneyWalletActivity extends AppCompatActivity implements AlertDialogOkListener{

    @InjectView(R.id.btnPay)
    Button btnPay;


    String amount;
    String mPaymentFlag="";
    private LoginStatusServiceProvider loginStatusServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_to_money_wallet);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
        Intent intent=getIntent();
          amount=intent.getStringExtra("AMOUNT");
          btnPay.setText("Pay "+"\u20B9"+amount);

        progressDialog = new TransparentProgressDialog(AddPaymentToMoneyWalletActivity.this);
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
                            AlertDialogs.alertDialogOk(AddPaymentToMoneyWalletActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(AddPaymentToMoneyWalletActivity.this, "Alert", message,
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
                        PrintUtil.showToast(AddPaymentToMoneyWalletActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(AddPaymentToMoneyWalletActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(AddPaymentToMoneyWalletActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 11:
                Intent LogIntent = new Intent(AddPaymentToMoneyWalletActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
