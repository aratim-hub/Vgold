package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.AddComplain.AddComplainModel;
import com.cognifygroup.vgold.AddComplain.AddComplainServiceProvider;
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

public class ComplainActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edt_complain)
    EditText edt_complain;
    @InjectView(R.id.btnSubmitComplain)
    Button btnSubmitComplain;

    AddComplainServiceProvider addComplainServiceProvider;
    AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
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

        progressDialog = new TransparentProgressDialog(ComplainActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        addComplainServiceProvider = new AddComplainServiceProvider(this);
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
                            AlertDialogs.alertDialogOk(ComplainActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(ComplainActivity.this, "Alert", message,
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
                        PrintUtil.showToast(ComplainActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ComplainActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ComplainActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @OnClick(R.id.btnSubmitComplain)
    public void onClickOfbtnSubmitComplain() {
        AttemptToAddComplain(VGoldApp.onGetUerId(), edt_complain.getText().toString());
    }


    private void AttemptToAddComplain(String user_id, String complain) {
        progressDialog.show();
        addComplainServiceProvider.addComplain(user_id, complain, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((AddComplainModel) serviceResponse).getStatus();
                    String message = ((AddComplainModel) serviceResponse).getMessage();

                    if (status.equals("200")) {


                        AlertDialogs.alertDialogOk(ComplainActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ComplainActivity.this, message);
//                        Intent intent=new Intent(ComplainActivity.this,MainActivity.class);
//                        startActivity(intent);
                    } else {

                        AlertDialogs.alertDialogOk(ComplainActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ComplainActivity.this, message);
//                        Intent intent=new Intent(ComplainActivity.this,MainActivity.class);
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
                        PrintUtil.showToast(ComplainActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ComplainActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ComplainActivity.this);
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
                Intent intent = new Intent(ComplainActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 11:
                Intent LogIntent = new Intent(ComplainActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
