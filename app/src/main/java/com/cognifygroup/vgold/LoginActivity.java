package com.cognifygroup.vgold;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.getOtp.OtpServiceProvider;
import com.cognifygroup.vgold.login.LoginModel;
import com.cognifygroup.vgold.login.LoginServiceProvider;
import com.cognifygroup.vgold.loginImage.LoginImageModel;
import com.cognifygroup.vgold.loginImage.LoginImageServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.AppSignatureHashHelper;
import com.cognifygroup.vgold.utils.AskPermissions;
import com.cognifygroup.vgold.utils.BaseActivity;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.Constant;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.cognifygroup.vgold.version.VersionModel;
import com.cognifygroup.vgold.version.VersionServiceProvider;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.btnGpay)
    Button btnGpay;

    @InjectView(R.id.BtnLogin)
    Button BtnLogin;
    @InjectView(R.id.edtEmail)
    EditText edtEmail;
    @InjectView(R.id.edtLoginPassword)
    EditText edtLoginPassword;
    @InjectView(R.id.edtLoginOTP)
    EditText edtLoginOTP;
    @InjectView(R.id.otpLayout)
    TextInputLayout otpLayout;
    @InjectView(R.id.imgLogin)
    ImageView imgLogin;
    @InjectView(R.id.tv_forgotpassword)
    TextView tv_forgotpassword;
   /* @InjectView(R.id.progressBar)
    ProgressBar progressBar;*/

    private Context mContext = this;
    private LoginServiceProvider mLoginServiceProvider;
    LoginImageServiceProvider loginImageServiceProvider;
    VersionServiceProvider versionServiceProvider;
    OtpServiceProvider otpServiceProvider;
    private AlertDialogs mAlert;
    private String mEmail;
    private String mPassword;
    private String token;
    double updatedversioncode = 1.0;
    private TransparentProgressDialog progressDialog;
    private String mInvitationUrl;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init() {
        progressDialog = new TransparentProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        mLoginServiceProvider = new LoginServiceProvider(mContext);
        otpServiceProvider = new OtpServiceProvider(this);
        loginImageServiceProvider = new LoginImageServiceProvider(this);
        versionServiceProvider = new VersionServiceProvider(this);

        VersionChecker versionChecker = new VersionChecker();
        try {
            String playVersion = versionChecker.execute().get();

            if (playVersion != null && !TextUtils.isEmpty(playVersion)) {
                getLatestVersion(playVersion);
            }

            /*String mobVersion;
            PackageInfo pInfo = null;

                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                mobVersion = pInfo.versionName;
//                updatedversioncode = Double.parseDouble(VGoldApp.onGetVersionCode());

                if (playVersion != null && !TextUtils.isEmpty(playVersion)) {
                    getLatestVersion(playVersion);
                } else {
                    if (Double.parseDouble(playVersion) > Double.parseDouble(mobVersion)) {
                        getLatestVersion(playVersion);
                    }
                }*/

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /*PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            String version = pInfo.versionName;
            getLatestVersion(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/

        AttemptToLoadLoginImage();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        token = Objects.requireNonNull(task.getResult()).getToken();
//                        Log.d("token", token);

//                        sendToken(token);
                    }
                });


//        progressBar.setSecondaryProgress(10);
//        progressBar.setProgress(10);
//        progressBar.setMax(10);
    }


   /* private class GetLatestVersion extends AsyncTask<String, String, String> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
//                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id= your app package address";
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "&hl=en";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();

            }catch (Exception e){
                e.printStackTrace();

            }

            return latestVersion;
        }
    }*/


    private void getLatestVersion(String version) {
        progressDialog.show();
        versionServiceProvider.getVerionCheck(version, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((VersionModel) serviceResponse).getStatus();
                    String message = ((VersionModel) serviceResponse).getMessage();
                    String data = ((VersionModel) serviceResponse).getData();
                    if (status.equals("200"))
                        checkAppVersion(data);

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
                        PrintUtil.showToast(LoginActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }


    public class VersionChecker extends AsyncTask<String, String, String> {

        String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
//                String packageName = getApplicationContext().getPackageName();
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }
    }

    private void checkAppVersion(String playVersion) {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
//            double appVersion = Double.parseDouble(version);
//            updatedversioncode = Double.parseDouble(playVersion);

            String[] v1 = version.split("\\.");
            String[] v2 = playVersion.split("\\.");


            if (v1.length != v2.length)
                return;

            for (int pos = 0; pos < v1.length; pos++) {
                if (Integer.parseInt(v1[pos]) < Integer.parseInt(v2[pos])) {
                    UpdateAppAlert();
                }
            }

//            if (appVersion < updatedversioncode) {
//                UpdateAppAlert();
//            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void UpdateAppAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle("New Update Available");


        // Setting Dialog Message
        alertDialog.setMessage("Please update app to latest version for new features");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String packageName = getApplicationContext().getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            }
        });

        // on pressing cancel button
      /*  alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });*/
        // Showing Alert Message
        alertDialog.show();

    }


    @OnClick(R.id.BtnLogin)
    public void OnLoginBtnClick() {

        mEmail = edtEmail.getText().toString();
        mPassword = edtLoginPassword.getText().toString();

        if (otpLayout.getVisibility() == View.GONE) {
            if (mEmail.length() == 0 && mPassword.length() == 0) {

                AlertDialogs.alertDialogOk(LoginActivity.this, "Alert", "All data required",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                mAlert.onShowToastNotification(LoginActivity.this, "All data required");

            } else if (mEmail.length() == 0) {
                edtEmail.setError("Enter Valid Email");
            } /*else if (mPassword.length() == 0) {
            edtLoginPassword.setError("Enter Valid password");
        }*/ else {

                AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

                Log.i("TAG", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));

//                Toast.makeText(getApplicationContext(), appSignatureHashHelper.getAppSignatures().get(0), Toast.LENGTH_SHORT).show();

                loginApi(mEmail, mPassword, appSignatureHashHelper.getAppSignatures().get(0));
            }
        } else {
            String mOtp = edtLoginOTP.getText().toString();
            if (!TextUtils.isEmpty(mOtp)) {
                if (token != null && !TextUtils.isEmpty(token)) {
                    VerifyOTPApi(mEmail, mOtp, token);
                }
            } else {
                edtLoginOTP.setError("Enter Valid OTP");
            }
        }
    }

    @OnClick(R.id.tv_forgotpassword)
    public void onClickOftv_forgotpassword() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    @OnClick(R.id.txt_SignUp)
    public void GoToRegistration() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void loginApi(String mEmail, String mPassword, String appSignature) {

        progressDialog.show();
        mLoginServiceProvider.callUserLogin(mEmail, appSignature, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((LoginModel) serviceResponse).getStatus();
                try {
                    String message = ((LoginModel) serviceResponse).getMessage();
                    ArrayList<LoginModel.Data> loginModelArrayList = ((LoginModel) serviceResponse).getData();
                    if (Status.equals("200")) {

//                        otpLayout.setVisibility(View.VISIBLE);

//                        AlertDialogs.alertDialogOk(LoginActivity.this, "Alert", message,
//                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);


                        Intent i = new Intent(LoginActivity.this, OtpVarificationActivity.class);
                        i.putExtra("mobNo", mEmail);
                        i.putExtra("token", token);
                        i.putExtra("moveFrom", "login");
                        startActivity(i);

                    } else {
                        AlertDialogs.alertDialogOk(LoginActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(LoginActivity.this, message);
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
                        PrintUtil.showToast(LoginActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void VerifyOTPApi(String mEmail, String mOTP, String token) {
        progressDialog.show();
        mLoginServiceProvider.callVerifyUserLogin(mEmail, mOTP, token, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((LoginModel) serviceResponse).getStatus();
                try {
                    String message = ((LoginModel) serviceResponse).getMessage();
                    ArrayList<LoginModel.Data> loginModelArrayList = ((LoginModel) serviceResponse).getData();
                    if (Status.equals("200")) {

                        progressDialog.hide();

                        edtEmail.setText("");
                        edtLoginOTP.setText("");

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
                                loginModelArrayList.get(0).getProfile_photo(),
                                loginModelArrayList.get(0).getIs_cp(),
                                loginModelArrayList.get(0).getIdentity_photo(),
                                loginModelArrayList.get(0).getAddress_photo(),
                                loginModelArrayList.get(0).getAddress_photo_back(),
                                loginModelArrayList.get(0).getAadhar_no());

                        VGoldApp.onSetUserRole(loginModelArrayList.get(0).getUser_role(), loginModelArrayList.get(0).getValidity_date());

//                        Log.d("TAG", loginModelArrayList.get(0).getUser_role() + " " + loginModelArrayList.get(0).getValidity_date());

//                        VGoldApp.onSetVersionCode(loginModelArrayList.get(0).getVersion_code());

//                        AlertDialogs.alertDialogOk(LoginActivity.this, "Alert", "Login successfully",
//                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                      /*  mAlert.onShowToastNotification(LoginActivity.this, "Login successfully");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);*/

                    } else {
                        AlertDialogs.alertDialogOk(LoginActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(LoginActivity.this, message);
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
                        PrintUtil.showToast(LoginActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToLoadLoginImage() {
        progressDialog.show();
        loginImageServiceProvider.getGoldBookingHistory(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((LoginImageModel) serviceResponse).getStatus();
                    String message = ((LoginImageModel) serviceResponse).getMessage();
                    String image = ((LoginImageModel) serviceResponse).getImage();
                    if (status.equals("200")) {


                        Picasso.with(LoginActivity.this)
                                .load(image)
                                //.placeholder(R.drawable.images)
                                .resize(400, 400)
                                .into(imgLogin, new Callback() {
                                    @Override
                                    public void onSuccess() {
                        /*if (holder.progressbar_category !=null){
                            holder.progressbar_category.setVisibility(View.GONE);
                        }*/
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });


                    } else {
                        AlertDialogs.alertDialogOk(LoginActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(LoginActivity.this, message);

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
                        PrintUtil.showToast(LoginActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(LoginActivity.this);
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
                Intent i = new Intent(LoginActivity.this, OtpVarificationActivity.class);
                i.putExtra("mobNo", mEmail);
                i.putExtra("token", token);
                i.putExtra("moveFrom", "login");
                startActivity(i);
                break;
        }

    }

}