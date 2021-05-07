package com.cognifygroup.vgold;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.login.LoginModel;
import com.cognifygroup.vgold.login.LoginServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
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
    private TextView tv_otp_var_msg, tv_resend_otp, tv_not_receive, tv_msg;

    private EditText et_otp_var1, et_otp_var2, et_otp_var3, et_otp_var4, et_otp_var5, et_otp_var6;
    private String otpstring, finalOtp, student_mobile, newOTP;
    private String studentId, user_otp;
    private Handler handler;
    private String mobNo, token;
    BroadcastReceiver receiver;
    GoogleApiClient mCredentialsApiClient = null;
    TimerTask task;
    Timer timer;
    private TransparentProgressDialog progressDialog;
    private LoginServiceProvider mLoginServiceProvider;
    private AlertDialogOkListener alertDialogOkListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);

        initAllViews();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    progressDialog.hide();
                    tv_msg.setVisibility(View.GONE);
                    final String message = intent.getStringExtra("message");
                    String[] arr = message.split("");
                    String c1 = arr[1];
                    String c2 = arr[2];
                    String c3 = arr[3];
                    String c4 = arr[4];
                    String c5 = arr[5];
                    String c6 = arr[6];
                    et_otp_var1.setText(c1);
                    et_otp_var2.setText(c2);
                    et_otp_var3.setText(c3);
                    et_otp_var4.setText(c4);
                    et_otp_var5.setText(c5);
                    et_otp_var6.setText(c6);
                    //Do whatever you want with the code here
                    concatOTP();
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
        et_otp_var4.addTextChangedListener(new TextWatcher() {
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
        });

    }

    private void initAllViews() {

        progressDialog = new TransparentProgressDialog(OtpVarificationActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mLoginServiceProvider = new LoginServiceProvider(OtpVarificationActivity.this);


        et_otp_var1 = findViewById(R.id.et_otp_var1);
        et_otp_var2 = findViewById(R.id.et_otp_var2);
        et_otp_var3 = findViewById(R.id.et_otp_var3);
        et_otp_var4 = findViewById(R.id.et_otp_var4);
        et_otp_var5 = findViewById(R.id.et_otp_var5);
        et_otp_var6 = findViewById(R.id.et_otp_var6);

        btn_otp_verify = findViewById(R.id.btn_otp_verify);
        tv_otp_var_msg = findViewById(R.id.tv_otp_var_msg);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        tv_not_receive = findViewById(R.id.tv_not_receive);

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
                // Toast.makeText(getApplicationContext(), "SRetriever", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                //  Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
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
                loginApi();
                break;
        }
    }

    private void concatOTP() {
        String card1 = et_otp_var1.getText().toString().trim();
        String card2 = et_otp_var2.getText().toString().trim();
        String card3 = et_otp_var3.getText().toString().trim();
        String card4 = et_otp_var4.getText().toString().trim();
        String card5 = et_otp_var5.getText().toString().trim();
        String card6 = et_otp_var6.getText().toString().trim();

        if (validateDetails(card1, card2, card3, card4, card5, card6)) {
            finalOtp = (card1 + card2 + card3 + card4 + card5 + card6);
            if (!finalOtp.equalsIgnoreCase("")) {
                verifyOtp(finalOtp);

            }
        }
    }

    private boolean validateDetails(String otpDig1, String otpDig2, String otpDig3, String otpDig4, String otpDig5, String otpDig6) {
        if (!BaseActivity.isOkToSave(otpDig1) && !BaseActivity.isOkToSave(otpDig2) && !BaseActivity.isOkToSave(otpDig3) &&
                !BaseActivity.isOkToSave(otpDig4) && !BaseActivity.isOkToSave(otpDig5) && !BaseActivity.isOkToSave(otpDig6)) {

            AlertDialogs.alertDialogOk(OtpVarificationActivity.this, "Alert", "Please enter the OTP you received on your mobile. If not received use Resend.",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
            return false;
        } else
            return true;
    }

    private void verifyOtp(String mOTP) {

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

                        setEditTextBlank();

                        Intent intent = new Intent(OtpVarificationActivity.this, MainActivity.class);
                        startActivity(intent);

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
        et_otp_var5.setText("");
        et_otp_var6.setText("");
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

    }
}


