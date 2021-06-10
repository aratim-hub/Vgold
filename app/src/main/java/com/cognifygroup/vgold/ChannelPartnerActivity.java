package com.cognifygroup.vgold;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cognifygroup.vgold.Adapter.CPUserDetailsAdapter;
import com.cognifygroup.vgold.Adapter.GoldBookingHistoryAdapter;
import com.cognifygroup.vgold.Adapter.GoldDepositeHistoryAdapter;
import com.cognifygroup.vgold.AddComplain.AddComplainModel;
import com.cognifygroup.vgold.AddComplain.AddComplainServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CPModule.CPServiceProvider;
import com.cognifygroup.vgold.ChannelPartner.UserDetailsModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.login.LoginModel;
import com.cognifygroup.vgold.login.LoginServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChannelPartnerActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.txtUserCountr)
    TextView txtUserCountr;
    @InjectView(R.id.txtCommission)
    TextView txtCommission;
    @InjectView(R.id.txtGold)
    TextView txtGold;
    @InjectView(R.id.noData)
    TextView noData;

    @InjectView(R.id.recyclerUsers)
    RecyclerView recyclerUsers;

    @InjectView(R.id.etUserSearch)
    AppCompatEditText etUserSearch;

    AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;
    private CPServiceProvider mCPUserServiceProvider;
    private CPUserDetailsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_partner);
        ButterKnife.inject(ChannelPartnerActivity.this);

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

        progressDialog = new TransparentProgressDialog(ChannelPartnerActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        mCPUserServiceProvider = new CPServiceProvider(this);
        loginStatusServiceProvider = new LoginStatusServiceProvider(this);

        recyclerUsers.setLayoutManager(new LinearLayoutManager(ChannelPartnerActivity.this));

        if (VGoldApp.onGetUserTot() != null && !TextUtils.isEmpty(VGoldApp.onGetUserTot())) {
            txtUserCountr.setText(VGoldApp.onGetUserTot());
        }

        if (VGoldApp.onGetCommissionTot() != null && !TextUtils.isEmpty(VGoldApp.onGetCommissionTot())) {
            txtCommission.setText(getResources().getString(R.string.rs) + VGoldApp.onGetCommissionTot());
        }

        if (VGoldApp.onGetGoldTot() != null && !TextUtils.isEmpty(VGoldApp.onGetGoldTot())) {
            txtGold.setText(VGoldApp.onGetGoldTot() + " gm");
        }

        etUserSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    if (mAdapter != null) {
                        mAdapter.resetData();
                    }
                }
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        checkLoginSession();
        getUserDetails();
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
                            AlertDialogs.alertDialogOk(ChannelPartnerActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(ChannelPartnerActivity.this, "Alert", message,
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
                        PrintUtil.showToast(ChannelPartnerActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ChannelPartnerActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ChannelPartnerActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void getUserDetails() {
        progressDialog.show();
        mCPUserServiceProvider.getUserDetails(VGoldApp.onGetUerId(), new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {

                try {
                    String Status = ((UserDetailsModel) serviceResponse).getStatus();
                    String message = ((UserDetailsModel) serviceResponse).getMessage();
                    ArrayList<UserDetailsModel.Data> userDetailsModelArrayList = ((UserDetailsModel) serviceResponse).getData();


                    if (Status.equals("200")) {
                        if (userDetailsModelArrayList != null && userDetailsModelArrayList.size() > 0) {
                            noData.setVisibility(View.GONE);
                            recyclerUsers.setLayoutManager(new LinearLayoutManager(ChannelPartnerActivity.this));
                            mAdapter = new CPUserDetailsAdapter(ChannelPartnerActivity.this, userDetailsModelArrayList);
                            recyclerUsers.setAdapter(mAdapter);
                        } else {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } else {
                        AlertDialogs.alertDialogOk(ChannelPartnerActivity.this, "Alert", message,
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
                        PrintUtil.showToast(ChannelPartnerActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ChannelPartnerActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ChannelPartnerActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 11:
                Intent LogIntent = new Intent(ChannelPartnerActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
