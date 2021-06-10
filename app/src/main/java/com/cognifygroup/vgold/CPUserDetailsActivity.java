package com.cognifygroup.vgold;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.cognifygroup.vgold.CPModule.CPCommissionFragment;
import com.cognifygroup.vgold.CPModule.CPEMIFragment;
import com.cognifygroup.vgold.CPModule.CPGoldBookingFragment;
import com.cognifygroup.vgold.CPModule.CustomViewPager;
import com.cognifygroup.vgold.Adapter.ViewPagerAdapter;
import com.cognifygroup.vgold.AddComplain.AddComplainServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.ChannelPartner.UserDetailsModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CPUserDetailsActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;

    @InjectView(R.id.viewPager)
    CustomViewPager viewPager;

    @InjectView(R.id.txtName)
    TextView txtName;

    @InjectView(R.id.txtMobNo)
    TextView txtMobNo;

    @InjectView(R.id.txtEmail)
    TextView txtEmail;
    @InjectView(R.id.txtCommission)
    TextView txtCommission;

    @InjectView(R.id.userImg)
    CircleImageView userImg;

    AddComplainServiceProvider addComplainServiceProvider;
    AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    private ArrayList<UserDetailsModel> userDetailsList;
    private int[] tabIcons = {
            R.drawable.gold_coin_progress,
            R.drawable.emi,
            R.drawable.commission
    };
    private UserDetailsModel.Data userDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp_user_details);
        ButterKnife.inject(CPUserDetailsActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIntentDate();
        init();
    }

    private void getIntentDate() {
        try {
            userDetailsModel = (UserDetailsModel.Data) getIntent().getSerializableExtra("userDetailsModel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        progressDialog = new TransparentProgressDialog(CPUserDetailsActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        addComplainServiceProvider = new AddComplainServiceProvider(this);

        loginStatusServiceProvider = new LoginStatusServiceProvider(this);
        checkLoginSession();

        setTabViews();
        setUserDetails();

    }

    private void setUserDetails() {
        if (userDetailsModel != null) {
            txtName.setText(userDetailsModel.getUname());
            txtMobNo.setText(userDetailsModel.getUmobile());
            txtEmail.setText(userDetailsModel.getUemail());
            txtCommission.setText("Commission Earned : "+getResources().getString(R.string.rs) + userDetailsModel.getTotal_commission_created());

            Glide.with(this).load(userDetailsModel.getUpic())
                    .placeholder(R.drawable.black_user)
                    .error(R.drawable.black_user)
                    .into(userImg);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTabViews() {
        VGoldApp.viewPagerr = viewPager;

        VGoldApp.viewPagerr.setOffscreenPageLimit(1);
        setupViewPager(VGoldApp.viewPagerr);
        tabLayout.setupWithViewPager(VGoldApp.viewPagerr);
        VGoldApp.viewPagerr.setPagingEnabled(false);
        setupTabIcons();

//        setupTabIcons();
        VGoldApp.viewPagerr.setCurrentItem(0);

        VGoldApp.viewPagerr.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                VGoldApp.viewPagerr.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    private void setupViewPager(CustomViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CPGoldBookingFragment fragment1 = new CPGoldBookingFragment(userDetailsModel.getUid());
        CPEMIFragment fragment2 = new CPEMIFragment(userDetailsModel.getUid());
        CPCommissionFragment fragment3 = new CPCommissionFragment(userDetailsModel.getUid());

        adapter.addFragment(fragment1, "Gold Booking");
        adapter.addFragment(fragment2, "EMI");
        adapter.addFragment(fragment3, "Commission");
        viewPager.setAdapter(adapter);

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
                            AlertDialogs.alertDialogOk(CPUserDetailsActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(CPUserDetailsActivity.this, "Alert", message,
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
                        PrintUtil.showToast(CPUserDetailsActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(CPUserDetailsActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(CPUserDetailsActivity.this);
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
                Intent LogIntent = new Intent(CPUserDetailsActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }

    }
}
