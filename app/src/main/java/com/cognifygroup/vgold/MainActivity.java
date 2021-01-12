package com.cognifygroup.vgold;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cognifygroup.vgold.Adapter.VendorOfferAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.addGold.AddGoldServiceProvider;
import com.cognifygroup.vgold.getReferCode.ReferModel;
import com.cognifygroup.vgold.getReferCode.ReferServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel;
import com.cognifygroup.vgold.getVendorOffer.VendorOfferServiceProvider;
import com.cognifygroup.vgold.plan.PlanActivity;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.sunfusheng.marqueeview.MarqueeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AlertDialogOkListener {

    @InjectView(R.id.rleGoldWallet)
    RelativeLayout rleGoldWallet;
    @InjectView(R.id.rlMoneyWallet)
    RelativeLayout rlMoneyWallet;
    @InjectView(R.id.rleTransferGold)
    RelativeLayout rleTransferGold;
    @InjectView(R.id.rlAddGold)
    RelativeLayout rlAddGold;
    @InjectView(R.id.rlSellGold)
    RelativeLayout rlSellGold;
    @InjectView(R.id.rlAddMoney)
    RelativeLayout rlAddMoney;
    @InjectView(R.id.rlTransferMoney)
    RelativeLayout rlTransferMoney;
    @InjectView(R.id.rlWithdrawMoney)
    RelativeLayout rlWithdrawMoney;
    @InjectView(R.id.rlMembershipCard)
    RelativeLayout rlMembershipCard;
    @InjectView(R.id.imgGoldBooking)
    ImageView imgGoldBooking;
    @InjectView(R.id.imgGoldDeposite)
    ImageView imgGoldDeposite;
    @InjectView(R.id.imgOurVenders)
    ImageView imgOurVenders;
    @InjectView(R.id.imgReferEarn)
    ImageView imgReferEarn;
    @InjectView(R.id.imageView3)
    ImageView imageView3;
    @InjectView(R.id.rc_vendorOffer)
    RecyclerView rc_vendorOffer;

    @InjectView(R.id.txtClientName)
    TextView txtClientName;
    @InjectView(R.id.txtCrnNo)
    TextView txtCrnNo;
    @InjectView(R.id.txtValiditydate)
    TextView txtValiditydate;
    @InjectView(R.id.imgClient)
    ImageView imgClient;
    private String referCode = "";

    @InjectView(R.id.rate_scroll_title)
    TextView rate_scroll_title;

    @InjectView(R.id.imgPlan)
    ImageView imgPlan;

    @InjectView(R.id.imgPay)
    ImageView imgPay;


    AlertDialogs mAlert;
    private VendorOfferAdapter vendorOfferAdapter;
    private VendorOfferServiceProvider vendorOfferServiceProvider;
    private ReferServiceProvider referServiceProvider;
    double updatedversioncode = 1.0;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    GetTodayGoldRateServiceProvider getTodayGoldRateServiceProvider;
    AddGoldServiceProvider addGoldServiceProvider;
    private int pressBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle("Welcome " + VGoldApp.onGetFirst());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        init();

        AttemptToGetTodayGoldRate();    
    }

    private void init() {

//        Log.d("USerRole", VGoldApp.onGetUserRole());

        rate_scroll_title.setSelected(true);
        progressDialog = new TransparentProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();


        vendorOfferServiceProvider = new VendorOfferServiceProvider(this);
        referServiceProvider = new ReferServiceProvider(this);

        getTodayGoldRateServiceProvider = new GetTodayGoldRateServiceProvider(this);
        addGoldServiceProvider = new AddGoldServiceProvider(this);

        txtCrnNo.setText("CRN - " + VGoldApp.onGetUerId());
        txtClientName.setText("" + VGoldApp.onGetFirst() + " " + VGoldApp.onGetLast());


        /*try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            double abc = Double.parseDouble(version);

            updatedversioncode = Double.parseDouble(VGoldApp.onGetVersionCode());

            if (abc < updatedversioncode) {
                UpdateAppAlert();
                // Toast.makeText(this,"you need to update App",Toast.LENGTH_LONG).show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/

        if (VGoldApp.onGetUserRole().equals("member")) {
            rlMembershipCard.setBackgroundResource(R.drawable.membershipcardlifetime);
            txtValiditydate.setVisibility(View.GONE);
        } else {
            txtValiditydate.setText(VGoldApp.onGetValidity());
            rlMembershipCard.setBackgroundResource(R.drawable.membershipcardlimited);
        }

//        AttemptToGetVendorOffer();

        Picasso.with(this)
                .load(VGoldApp.onGetQrCode())
                //.placeholder(R.drawable.images)
                .resize(400, 400)
                .into(imageView3, new Callback() {
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

        if (!VGoldApp.onGetUserImg().equals("")) {

            Picasso.with(this)
                    .load(VGoldApp.onGetUserImg())
                    //.placeholder(R.drawable.images)
                    .resize(400, 400)
                    .into(imgClient, new Callback() {
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

        }

        checkWalletAmt(VGoldApp.onGetUerId());


    }

    private void checkWalletAmt(String userId) {
//        progressDialog.show();
        referServiceProvider.getCheckWallet(userId, "request", new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
//                    progressDialog.hide();
                    String status = ((ReferModel) serviceResponse).getStatus();
                    String message = ((ReferModel) serviceResponse).getMessage();
                    String data = ((ReferModel) serviceResponse).getData();


                    if (status.equals("200")) {
                        if (data.equalsIgnoreCase("true")) {

                            if (VGoldApp.onGetUserRole().equals("Customer")) {
                                addMembershipDialog();
                            }
                        }
                    } /*else {
                        mAlert.onShowToastNotification(MainActivity.this, message);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
//                    progressDialog.hide();
                } finally {
//                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    progressDialog.hide();
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(MainActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(MainActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(MainActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void addMembershipDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Add Membership");

        // Setting Dialog Message
        alertDialog.setMessage("Your wallet amount have been reached to a membership amount. Would you like to be member?\n" +
                "Once you are a member you can use these points.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                beMember(VGoldApp.onGetUerId());
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private void beMember(String userId) {
        progressDialog.show();
        referServiceProvider.getCheckWallet(userId, "update", new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    progressDialog.hide();
                    String status = ((ReferModel) serviceResponse).getStatus();
                    String message = ((ReferModel) serviceResponse).getMessage();
                    String data = ((ReferModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        progressDialog.hide();
//                        mAlert.onShowToastNotification(MainActivity.this, "You are now a member of VGold");

                        VGoldApp.onSetUserRole("member", data);


                        if (VGoldApp.onGetUserRole().equals("member")) {
                            rlMembershipCard.setBackgroundResource(R.drawable.membershipcardlifetime);
                            txtValiditydate.setVisibility(View.GONE);
                        } else {
                            txtValiditydate.setText(VGoldApp.onGetValidity());
                            rlMembershipCard.setBackgroundResource(R.drawable.membershipcardlimited);
                        }


                        AlertDialogs.alertDialogOk(MainActivity.this, "Alert", "You are now a member of VGold",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

                       /* if (message.equalsIgnoreCase("Not Eligible for upgrade") && data.equalsIgnoreCase("true")) {

                            addMembershipDialog();
                        }*/
                    } else {
                        progressDialog.hide();
                        AlertDialogs.alertDialogOk(MainActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(MainActivity.this, message);

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
                        PrintUtil.showToast(MainActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(MainActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(MainActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            pressBack++;
            if (pressBack == 1) {
                Toast.makeText(getApplicationContext(), "Press once again to exit !", Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
//            super.onBackPressed();
        }
    }

    @OnClick(R.id.rleGoldWallet)
    public void onClickGoldWallet() {

        Intent intent = new Intent(MainActivity.this, GoldWalletActivity.class);
        startActivity(intent);

    }


    @OnClick(R.id.rleTransferGold)
    public void
    onClickImgTransferGold() {
        if (VGoldApp.onGetUserRole().equals("member")) {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        } else {

            AlertDialogs.alertDialogOk(MainActivity.this, "Alert", "You do not have this Privilege as you are not a registered member",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
        }
    }

    @OnClick(R.id.rlAddGold)
    public void onClickImgAddGold() {
        Intent intent = new Intent(MainActivity.this, AddGoldActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rlSellGold)
    public void onClickImgSaleGold() {
        Intent intent = new Intent(MainActivity.this, SellGoldActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.rlMoneyWallet)
    public void onClickMoneyWallet() {
        Intent intent = new Intent(MainActivity.this, MoneyWalletActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imgReferEarn)
    public void onClickReferandEarn() {
        Intent intent = new Intent(MainActivity.this, ReferActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.imgOurVenders)
    public void onClickOurVenders() {
        Intent intent = new Intent(MainActivity.this, OurVendersActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rlAddMoney)
    public void onClickImgAddMoney() {
        Intent intent = new Intent(MainActivity.this, AddMoneyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rlTransferMoney)
    public void onClickImgTransferMoney() {
        Intent intent = new Intent(MainActivity.this, ScanActivityForMoney.class);
        startActivity(intent);
    }

    @OnClick(R.id.rlWithdrawMoney)
    public void onClickImgWithdrawMoney() {
        Intent intent = new Intent(MainActivity.this, WithdrawActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imgGoldBooking)
    public void onClickImgGoldBooking() {
        Intent intent = new Intent(MainActivity.this, GoldBookingHistoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imgGoldDeposite)
    public void onClickImgGoldDeposite() {
        Intent intent = new Intent(MainActivity.this, GoldDepositeHistoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imgPlan)
    public void onClickImgPlan() {
        Intent intent = new Intent(MainActivity.this, PlanActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imgPay)
    public void onClickImgPay() {
        startActivity(new Intent(this, PayInstallmentActivity.class));
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));

        } else if (id == R.id.nav_profile) {

            startActivity(new Intent(this, ProfileActivity.class));

        } else if (id == R.id.nav_eGoldWallet) {
            startActivity(new Intent(this, GoldWalletActivity.class));

        } else if (id == R.id.nav_moneyWallet) {
            startActivity(new Intent(this, MoneyWalletActivity.class));
        } else if (id == R.id.nav_addBank) {
            startActivity(new Intent(this, AddBankActivity.class));
        } else if (id == R.id.nav_goldBookingHistory) {
            startActivity(new Intent(this, GoldBookingHistoryActivity.class));
        } else if (id == R.id.nav_goldDepositeHistory) {
            startActivity(new Intent(this, GoldDepositeHistoryActivity.class));
        } else if (id == R.id.nav_payInstallment) {
            startActivity(new Intent(this, PayInstallmentActivity.class));

        } else if (id == R.id.nav_Share) {
            OnShare();

        } else if (id == R.id.nav_refer) {
            startActivity(new Intent(this, ReferActivity.class));

        } else if (id == R.id.nav_logout) {
            logoutAlert();
        } else if (id == R.id.nav_addComplain) {
            startActivity(new Intent(this, ComplainActivity.class));
        } else if (id == R.id.nav_review) {
            startActivity(new Intent(this, ReviewActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OnShare() {
        AttemptToRefer(VGoldApp.onGetUerId());
    }

    private void logoutAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure to logout?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }

    private void UpdateAppAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

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
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }

    private void AttemptToRefer(String user_id) {
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
                        } /*else {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            String packageName = getApplicationContext().getPackageName();
                            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + packageName + "&referrer=" + data);
                            startActivity(Intent.createChooser(intent, "Share App with Friends!"));
                        }*/


                    } else {
                        AlertDialogs.alertDialogOk(MainActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(MainActivity.this, message);
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
                        PrintUtil.showToast(MainActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(MainActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(MainActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void createDynamicLink(String refCode) {

        String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "&referrer=" + refCode;
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
//                        Log.d("TAG", mInvitationUrl);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, mInvitationUrl);
                            startActivity(Intent.createChooser(intent, "Share App with Friends!"));
                        }

                    }
                });
    }


    @Override
    public void onDialogOk(int resultCode) {

    }

    private void AttemptToGetTodayGoldRate() {
        // progressDialog.show();
        getTodayGoldRateServiceProvider.getTodayGoldRate(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    progressDialog.hide();
                    String status = ((GetTodayGoldRateModel) serviceResponse).getStatus();
                    String message = ((GetTodayGoldRateModel) serviceResponse).getMessage();

                    Log.i("TAG", "onSuccess: " + status);
                    Log.i("TAG", "onSuccess: " + message);

                    //   todayGoldRate = ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate();
                    //   todayGoldRateWithGst = ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate_with_gst();
//                    txtGoldRate.setText("₹ " + todayGoldRate + "/GM");
                    //  txtGoldRate.setText("₹ " + todayGoldRateWithGst + "/GM");

                    if (status.equals("200")) {
                        // mAlert.onShowToastNotification(AddGoldActivity.this, message);

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                        String formattedDate = df.format(c);

                        rate_scroll_title.setText("Today -" + df.format(c) + "(99.5ct Gold Rate) Purchase Rate: " + getResources().getString(R.string.rs) + ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate_with_gst() + "/gm" + "  Sale Rate: " + getResources().getString(R.string.rs) + ((GetTodayGoldRateModel) serviceResponse).getGold_sale_rate() + "/gm              ");

                    } else {
                        AlertDialogs.alertDialogOk(MainActivity.this, "Alert", message,
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
                        PrintUtil.showToast(MainActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(MainActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(MainActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }
}
