package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.register.RegModel;
import com.cognifygroup.vgold.register.RegServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements AlertDialogOkListener {

    AlertDialogs mAlert;
    RegServiceProvider regServiceProvider;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    @InjectView(R.id.edtfirst)
    MaterialEditText edtfirst;
    @InjectView(R.id.tb_subservices)
    Toolbar tb_subservices;
    @InjectView(R.id.edtlast1)
    MaterialEditText edtlast1;
    @InjectView(R.id.edtmail)
    MaterialEditText edtmail;
    @InjectView(R.id.edtno)
    MaterialEditText edtno;
    @InjectView(R.id.edtPass)
    MaterialEditText edtPass;
    @InjectView(R.id.edtPancard)
    MaterialEditText edtPancard;
    @InjectView(R.id.edtReferCode)
    MaterialEditText edtReferCode;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        setSupportActionBar(tb_subservices);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tb_subservices.setContentInsetStartWithNavigation(0);


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
        progressDialog = new TransparentProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        regServiceProvider = new RegServiceProvider(this);

        checkDeepLink();
    }

    private void checkDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null) {
                            String deepLink = String.valueOf(pendingDynamicLinkData.getLink());

                            if (deepLink != null && !TextUtils.isEmpty(deepLink)) {
                                String refCode = deepLink.substring(deepLink.lastIndexOf("=") + 1);
//                                Log.d("ReferCodeRegister", refCode);
                                edtReferCode.setText(refCode);
                                edtReferCode.setEnabled(false);
                            } else {
                                edtReferCode.setText("");
                                edtReferCode.setEnabled(true);
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "getDynamicLink:onFailure", e);
                    }
                });
    }

    @OnClick(R.id.btnRegister)
    public void onClickOfBtnRegister() {
        String first = edtfirst.getText().toString();
        String last = edtlast1.getText().toString();
        String email = edtmail.getText().toString();
        String no = edtno.getText().toString();
        String pass = edtPass.getText().toString();
        String pancard = edtPancard.getText().toString();
        String refercode = edtReferCode.getText().toString();

        if (first.length() == 0 && last.length() == 0 && email.length() == 0 && no.length() == 0 && pass.length() == 0 && pancard.length() == 0) {
            AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "All data required",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(RegisterActivity.this, "All data required");
        } else if (first.length() == 0) {
            edtfirst.setError("Enter name");
        } else if (last.length() == 0) {
            edtlast1.setError("Enter last name");
        } else if (email.length() == 0) {
            edtmail.setError("Enter valid email");
        } else if (no.length() < 10) {
            edtno.setError("Enter 10 digit contact number");
        } else if (edtPass.length() == 0) {
            edtPass.setError("Enter confirm password");
        } else if (edtPancard.length() == 0) {
            edtPancard.setError("Enter vaild Pancard Number");
        } else {
            AttemptToRegisterApi(first, last, email, no, pass, pancard, refercode);
        }
    }

    private void AttemptToRegisterApi(String first, String last, String email, String no, String pass, String pancard, String refer_code) {
        progressDialog.show();
        regServiceProvider.getReg(first, last, email, no, pass, pancard, refer_code, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((RegModel) serviceResponse).getStatus();
                    String message = ((RegModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", "Registration Successfully done",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(RegisterActivity.this, "Registration Successfully done");
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
                    } else {
                        AlertDialogs.alertDialogOk(RegisterActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(RegisterActivity.this, message);
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
                        PrintUtil.showToast(RegisterActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(RegisterActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(RegisterActivity.this);
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
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
