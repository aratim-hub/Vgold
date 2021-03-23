package com.cognifygroup.vgold;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cognifygroup.vgold.Adapter.GoldBookingHistoryAdapter;
import com.cognifygroup.vgold.Adapter.GoldTransactionHistoryAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.GetGoldTransactionHistory.GetGoldTransactionHistoryModel;
import com.cognifygroup.vgold.GetGoldTransactionHistory.GetGoldTransactionHistoryServiceProvider;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
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

public class GoldTransactionHistoryActivity extends AppCompatActivity implements AlertDialogOkListener {
    AlertDialogs mAlert;
    GetGoldTransactionHistoryServiceProvider getGoldTransactionHistoryServiceProvider;
    @InjectView(R.id.rvGoldTransactioHistory)
    RecyclerView rvGoldTransactioHistory;
    String booking_id;

    @InjectView(R.id.tb_goldTransactionHistory)
    Toolbar tb_goldTransactionHistory;
    @InjectView(R.id.imgGoldTransactionHistory)
    ImageView imgGoldTransactionHistory;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_transaction_history);
        ButterKnife.inject(this);
        setSupportActionBar(tb_goldTransactionHistory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tb_goldTransactionHistory.setContentInsetStartWithNavigation(0);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(){
        progressDialog = new TransparentProgressDialog(GoldTransactionHistoryActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        getGoldTransactionHistoryServiceProvider=new GetGoldTransactionHistoryServiceProvider(this);
        Intent intent=getIntent();
        booking_id=intent.getStringExtra("GOLD_BOOKING_ID");

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
                            AlertDialogs.alertDialogOk(GoldTransactionHistoryActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(GoldTransactionHistoryActivity.this, "Alert", message,
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
                        PrintUtil.showToast(GoldTransactionHistoryActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldTransactionHistoryActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldTransactionHistoryActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AttemptToGetGoldTransactionHistory(booking_id);
    }

    @OnClick(R.id.imgGoldTransactionHistory)
            public void onClickOfImgTrHistory(){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vgold.co.in/dashboard/user/module/goldbooking/transaction_pdf.php?bid="+booking_id+"&&user_id="+ VGoldApp.onGetUerId()));
        startActivity(browserIntent);
    }


    private void AttemptToGetGoldTransactionHistory(String gold_booking_id) {
        progressDialog.show();
        getGoldTransactionHistoryServiceProvider.getGoldTransactionHistory(gold_booking_id,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetGoldTransactionHistoryModel) serviceResponse).getStatus();
                    String message = ((GetGoldTransactionHistoryModel) serviceResponse).getMessage();
                    ArrayList<GetGoldTransactionHistoryModel.Data> mArrGoldBookingHistory=((GetGoldTransactionHistoryModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        rvGoldTransactioHistory.setLayoutManager(new LinearLayoutManager(GoldTransactionHistoryActivity.this));
                        rvGoldTransactioHistory.setAdapter(new GoldTransactionHistoryAdapter(GoldTransactionHistoryActivity.this,mArrGoldBookingHistory,booking_id));

                      //  mAlert.onShowToastNotification(GoldTransactionHistoryActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(GoldTransactionHistoryActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(GoldTransactionHistoryActivity.this, message);
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
                        PrintUtil.showToast(GoldTransactionHistoryActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldTransactionHistoryActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldTransactionHistoryActivity.this);
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
                Intent LogIntent = new Intent(GoldTransactionHistoryActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
