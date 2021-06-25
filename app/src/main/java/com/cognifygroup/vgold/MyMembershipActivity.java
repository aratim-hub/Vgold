package com.cognifygroup.vgold;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.Loan.LoanModel;
import com.cognifygroup.vgold.Loan.LoanServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyMembershipActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.txtClientName)
    TextView txtClientName;
    @InjectView(R.id.txtCrnNo)
    TextView txtCrnNo;
    @InjectView(R.id.txtValiditydate)
    TextView txtValiditydate;
    @InjectView(R.id.imgClient)
    ImageView imgClient;
    @InjectView(R.id.imageView3)
    ImageView imageView3;
    @InjectView(R.id.rlMembershipCard)
    RelativeLayout rlMembershipCard;

    AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        ButterKnife.inject(MyMembershipActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new TransparentProgressDialog(MyMembershipActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        loginStatusServiceProvider = new LoginStatusServiceProvider(this);

        checkLoginSession();
        loadData();
    }

    private void loadData() {
        txtCrnNo.setText("CRN - " + VGoldApp.onGetUerId());
        txtClientName.setText("" + VGoldApp.onGetFirst() + " " + VGoldApp.onGetLast());

        if (VGoldApp.onGetUserRole().equals("member")) {
            rlMembershipCard.setBackgroundResource(R.drawable.membershipcardlifetime);
            txtValiditydate.setVisibility(View.GONE);
        } else {
            txtValiditydate.setText(VGoldApp.onGetValidity());
            rlMembershipCard.setBackgroundResource(R.drawable.membershipcardlimited);
        }

        Picasso.with(this)
                .load(VGoldApp.onGetQrCode())
                //.placeholder(R.drawable.images)
                .resize(400, 400)
                .into(imageView3, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                    }
                });

        if (!VGoldApp.onGetUserImg().equals("")) {
            Glide.with(this).load(VGoldApp.onGetUserImg())
                    .placeholder(R.drawable.grayavator)
                    .error(R.drawable.grayavator)
                    .into(imgClient);
        }
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
                            AlertDialogs.alertDialogOk(MyMembershipActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(MyMembershipActivity.this, "Alert", message,
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
                        PrintUtil.showToast(MyMembershipActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(MyMembershipActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(MyMembershipActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 11:
                Intent LogIntent = new Intent(MyMembershipActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;

            case 1:
                Intent mainIntent = new Intent(MyMembershipActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                break;
        }

    }
}
