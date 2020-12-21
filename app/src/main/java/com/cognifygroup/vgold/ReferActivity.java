package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.AddBank.AddBankServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.getReferCode.ReferModel;
import com.cognifygroup.vgold.getReferCode.ReferServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ReferActivity extends AppCompatActivity implements AlertDialogOkListener {

    AlertDialogs mAlert;
    ReferServiceProvider referServiceProvider;

    @InjectView(R.id.edtNameR)
    EditText edtNameR;
    @InjectView(R.id.edtEmailR)
    EditText edtEmailR;
    @InjectView(R.id.edtMobileR)
    EditText edtMobileR;
    @InjectView(R.id.btnSubmitR)
    Button btnSubmitR;

    String no;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
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

        mAlert = AlertDialogs.getInstance();

        progressDialog = new TransparentProgressDialog(ReferActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);
        referServiceProvider = new ReferServiceProvider(this);

        if (getIntent().hasExtra("no")) {
            no = getIntent().getStringExtra("no");
        }

        edtMobileR.setText(no);

    }

    @OnClick(R.id.btnSubmitR)
    public void onClickOfBtnRefer() {
        String email = edtEmailR.getText().toString();
        String name = edtNameR.getText().toString();
        String mobile = edtMobileR.getText().toString();
        if (!email.equals("") && email != null && !name.equals("") && name != null && !mobile.equals("") && mobile != null) {

            getReferCode(VGoldApp.onGetUerId());

        } else {
            AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", "All Data required",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            Toast.makeText(ReferActivity.this, "All Data required", Toast.LENGTH_LONG).show();
        }
    }

    private void getReferCode(String user_id) {
        progressDialog.show();
        referServiceProvider.getReferenceCode(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    progressDialog.hide();
                    String status = ((ReferModel) serviceResponse).getStatus();
                    String message = ((ReferModel) serviceResponse).getMessage();
                    String data = ((ReferModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        if (data != null && !TextUtils.isEmpty(data)) {
                            createDynamicLink(data);
                        }
                    } else {
//                        mAlert.onShowToastNotification(ReferActivity.this, message);
                        AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.hide();
                } finally {
                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    progressDialog.hide();
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(ReferActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void createDynamicLink(String data) {

        String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "&referrer=" + data;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://vgold.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.cognifygroup.vgold")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.cognifygroup.vgold")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        String mInvitationUrl = String.valueOf(shortDynamicLink.getShortLink());

                        if (mInvitationUrl != null && !TextUtils.isEmpty(mInvitationUrl)) {

//                            Log.d("TAG", mInvitationUrl);

                            AttemptToRefer(VGoldApp.onGetUerId(), edtNameR.getText().toString(), edtEmailR.getText().toString(),
                                    edtMobileR.getText().toString(), mInvitationUrl);

                        }

                    }
                });
    }


    private void AttemptToRefer(String user_id, String name, String email, String mobile_no, String refLink) {
        progressDialog.show();
        referServiceProvider.getAddBankDetails(user_id, name, email, mobile_no, refLink, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((ReferModel) serviceResponse).getStatus();
                    String message = ((ReferModel) serviceResponse).getMessage();

                    if (status.equals("200")) {


                        AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ReferActivity.this, message);
                       /* Intent intent = new Intent(ReferActivity.this, MainActivity.class);
                        startActivity(intent);*/
                    } else {
                        AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ReferActivity.this, message);
//                        Intent intent = new Intent(ReferActivity.this, MainActivity.class);
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
                        PrintUtil.showToast(ReferActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode)
        {
            case 1:
                Intent intent = new Intent(ReferActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
