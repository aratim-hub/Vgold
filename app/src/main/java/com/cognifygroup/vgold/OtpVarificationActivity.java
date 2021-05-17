package com.cognifygroup.vgold;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.login.LoginModel;
import com.cognifygroup.vgold.login.LoginServiceProvider;
import com.cognifygroup.vgold.sellGold.SellGoldModel;
import com.cognifygroup.vgold.sellGold.SellGoldServiceProvider;
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalModel;
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.AppSignatureHashHelper;
import com.cognifygroup.vgold.utils.BaseActivity;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class OtpVarificationActivity extends AppCompatActivity implements
        AlertDialogOkListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Button btn_otp_verify;
    private LinearLayout bottomLayout;
    private TextView tv_otp_var_msg, tv_resend_otp, tv_not_receive, tv_msg;

    private EditText et_otp_var1, et_otp_var2, et_otp_var3, et_otp_var4, et_otp_var5, et_otp_var6;
    private String otpstring, finalOtp, student_mobile, newOTP;
    private String studentId, user_otp;
    private Handler handler;
    private String mobNo, token, moveFrom, weight, amount, otp, no;
    BroadcastReceiver receiver;
    GoogleApiClient mCredentialsApiClient = null;
    TimerTask task;
    Timer timer;
    private TransparentProgressDialog progressDialog;
    private LoginServiceProvider mLoginServiceProvider;
    private TransferGoldFinalServiceProvider transferGoldFinalServiceProvider;
    private SellGoldServiceProvider sellGoldServiceProvider;
    private AlertDialogOkListener alertDialogOkListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);


        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

        Log.i("TAG", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));

        initAllViews();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    progressDialog.hide();
                    tv_msg.setVisibility(View.GONE);
                    String message = intent.getStringExtra("message");
                    if(message!=null && !TextUtils.isEmpty(message)){
                       String trimMsg =  message.trim();

                        String[] arr = trimMsg.split("");
                        String c1 = arr[0];
                        String c2 = arr[1];
                        String c3 = arr[2];
                        String c4 = arr[3];
                        et_otp_var1.setText(c1);
                        et_otp_var2.setText(c2);
                        et_otp_var3.setText(c3);
                        et_otp_var4.setText(c4);
                        concatOTP();
                    }


                }
            }
        };


        //call text change listener on edit text
        et_otp_var1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    et_otp_var1.clearFocus();
                    et_otp_var2.requestFocus();
                    et_otp_var2.setCursorVisible(true);
                }
            }
        });
        et_otp_var2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    et_otp_var2.clearFocus();
                    et_otp_var3.requestFocus();
                    et_otp_var3.setCursorVisible(true);
                }
            }
        });
        et_otp_var3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    et_otp_var3.clearFocus();
                    et_otp_var4.requestFocus();
                    et_otp_var4.setCursorVisible(true);
                }
            }
        });
      /*  et_otp_var4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    et_otp_var4.clearFocus();
                    et_otp_var5.requestFocus();
                    et_otp_var5.setCursorVisible(true);
                }
            }
        });
        et_otp_var5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    et_otp_var5.clearFocus();
                    et_otp_var6.requestFocus();
                    et_otp_var6.setCursorVisible(true);
                }
            }
        });*/

    }

    private void initAllViews() {

        progressDialog = new TransparentProgressDialog(OtpVarificationActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mLoginServiceProvider = new LoginServiceProvider(OtpVarificationActivity.this);
        transferGoldFinalServiceProvider = new TransferGoldFinalServiceProvider(this);
        sellGoldServiceProvider = new SellGoldServiceProvider(this);

        et_otp_var1 = findViewById(R.id.et_otp_var1);
        et_otp_var2 = findViewById(R.id.et_otp_var2);
        et_otp_var3 = findViewById(R.id.et_otp_var3);
        et_otp_var4 = findViewById(R.id.et_otp_var4);
//        et_otp_var5 = findViewById(R.id.et_otp_var5);
//        et_otp_var6 = findViewById(R.id.et_otp_var6);

        btn_otp_verify = findViewById(R.id.btn_otp_verify);
        tv_otp_var_msg = findViewById(R.id.tv_otp_var_msg);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        tv_not_receive = findViewById(R.id.tv_not_receive);
        bottomLayout = findViewById(R.id.bottomLayout);

        tv_msg = findViewById(R.id.tv_msg);


        mCredentialsApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);

            }
        };
        timer.schedule(task, 30000);
//        timer.schedule(task, 100000);
        handler = new Handler();

        progressDialog.show();

        getIntentData();

        smsRetrivalCode();
        onClick();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            mobNo = getIntent().getStringExtra("mobNo");
            token = getIntent().getStringExtra("token");

            moveFrom = getIntent().getStringExtra("moveFrom");
            weight = getIntent().getStringExtra("Weight");
            amount = getIntent().getStringExtra("AMOUNT");

            otp = getIntent().getStringExtra("OTP");
            no = getIntent().getStringExtra("NO");


            if (moveFrom.equals("SellGold")) {
                btn_otp_verify.setText("Sell");
                bottomLayout.setVisibility(View.GONE);
            } else if (moveFrom.equals("login")) {
                btn_otp_verify.setText("VERIFY");
                bottomLayout.setVisibility(View.VISIBLE);
            } else {
                btn_otp_verify.setText("Transfer");
                bottomLayout.setVisibility(View.GONE);
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressDialog.hide();
            tv_msg.setVisibility(View.GONE);
            tv_otp_var_msg.setText("Please enter verification code received on " + mobNo);
        }
    };


    private void smsRetrivalCode() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
//                 Toast.makeText(getApplicationContext(), "SRetriever", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
//                  Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void onClick() {
        btn_otp_verify.setOnClickListener(this);
        tv_resend_otp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_otp_verify:
                concatOTP();
                break;

            case R.id.tv_resend_otp:

                if (moveFrom.equalsIgnoreCase("login")) {
                    loginApi();
                }
                break;
        }
    }

    private void concatOTP() {
        String card1 = et_otp_var1.getText().toString().trim();
        String card2 = et_otp_var2.getText().toString().trim();
        String card3 = et_otp_var3.getText().toString().trim();
        String card4 = et_otp_var4.getText().toString().trim();
//        String card5 = et_otp_var5.getText().toString().trim();
//        String card6 = et_otp_var6.getText().toString().trim();

//        if (validateDetails(card1, card2, card3, card4, card5, card6)) {
        if (validateDetails(card1, card2, card3, card4)) {
            finalOtp = (card1 + card2 + card3 + card4);
            if (!finalOtp.equalsIgnoreCase("")) {

                if (moveFrom.equalsIgnoreCase("login")) {
                    verifyLoginOtp(finalOtp);
                } else if (moveFrom.equals("SellGold")) {
//                    String OTP = edtOtp.getText().toString();
//                    if (!TextUtils.isEmpty(OTP)) {
                    verifyOTP(VGoldApp.onGetUerId(), finalOtp);
//                    }
                } else {
                    if (otp.equals(finalOtp)) {
                        TransferGoldOTP(VGoldApp.onGetUerId(), no, weight);
                    } else {
                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", "Otp not Matched",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                    }
                }
            }
        }
    }

    private boolean validateDetails(String otpDig1, String otpDig2, String otpDig3, String otpDig4) {
        if (!BaseActivity.isOkToSave(otpDig1) && !BaseActivity.isOkToSave(otpDig2) && !BaseActivity.isOkToSave(otpDig3) &&
                !BaseActivity.isOkToSave(otpDig4)) {

            AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", "Please enter the OTP you received on your mobile. If not received use Resend.",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
            return false;
        } else
            return true;
    }

    private void verifyLoginOtp(String mOTP) {

        progressDialog.show();
        mLoginServiceProvider.callVerifyUserLogin(mobNo, mOTP, token, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((LoginModel) serviceResponse).getStatus();
                try {
                    String message = ((LoginModel) serviceResponse).getMessage();
                    ArrayList<LoginModel.Data> loginModelArrayList = ((LoginModel) serviceResponse).getData();
                    if (Status.equals("200")) {

                        progressDialog.hide();

                        VGoldApp.onSetUserDetails(loginModelArrayList.get(0).getUser_ID(),
                                loginModelArrayList.get(0).getFirst_Name(),
                                loginModelArrayList.get(0).getLast_Name(),
                                loginModelArrayList.get(0).getEmail(),
                                loginModelArrayList.get(0).getMobile_no(),
                                loginModelArrayList.get(0).getQrcode(),
                                loginModelArrayList.get(0).getPan_no(),
                                loginModelArrayList.get(0).getAddress(),
                                loginModelArrayList.get(0).getCity(),
                                loginModelArrayList.get(0).getState(),
                                loginModelArrayList.get(0).getProfile_photo());

                        VGoldApp.onSetUserRole(loginModelArrayList.get(0).getUser_role(), loginModelArrayList.get(0).getValidity_date());

//                        setEditTextBlank();

                        Intent intent = new Intent(OtpVarificationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", message,
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
                        PrintUtil.showToast(OtpVarificationActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
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
                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", message,
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
                        PrintUtil.showToast(OtpVarificationActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
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
                        Intent intent = new Intent(OtpVarificationActivity.this, SuccessActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                        finish();
                    } else {

                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", message,
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
                        PrintUtil.showToast(OtpVarificationActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void TransferGoldOTP(String user_id, String no, String amount) {

        progressDialog.show();
        transferGoldFinalServiceProvider.transferMoney(user_id, no, amount, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((TransferGoldFinalModel) serviceResponse).getStatus();
                String message = ((TransferGoldFinalModel) serviceResponse).getMessage();
                try {
                    if (Status.equals("200")) {
                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);


//                        mAlert.onShowToastNotification(Otp1Activity.this, message);
//                        startActivity(new Intent(Otp1Activity.this,MainActivity.class));

                    } else {
                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", message,
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
                        PrintUtil.showToast(OtpVarificationActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void loginApi() {

        progressDialog.show();
        mLoginServiceProvider.callUserLogin(mobNo, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((LoginModel) serviceResponse).getStatus();
                try {
                    String message = ((LoginModel) serviceResponse).getMessage();
                    ArrayList<LoginModel.Data> loginModelArrayList = ((LoginModel) serviceResponse).getData();
                    if (Status.equals("200")) {

                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", "message",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

                    } else {
                        AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", message,
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
                        PrintUtil.showToast(OtpVarificationActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OtpVarificationActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void setEditTextBlank() {
        et_otp_var1.setText("");
        et_otp_var2.setText("");
        et_otp_var3.setText("");
        et_otp_var4.setText("");
//        et_otp_var5.setText("");
//        et_otp_var6.setText("");
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //smsVerifyCatcher.onStart();
        // registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //smsVerifyCatcher.onStop();
        // this.unregisterReceiver(smsReceiver);
    }


    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 1:
                startActivity(new Intent(OtpVarificationActivity.this, MainActivity.class));
                break;
        }
    }
}


