package com.cognifygroup.vgold;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cognifygroup.vgold.Adapter.GoldBookingHistoryAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryServiceProvider;
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

public class GoldBookingHistoryActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.btnBookGold)
    Button btnBookGold;
    AlertDialogs mAlert;
    GetGoldBookingHistoryServiceProvider getGoldBookingHistoryServiceProvider;
    @InjectView(R.id.rvGoldBookingHistory)
    RecyclerView rvGoldBookingHistory;
    @InjectView(R.id.txtGoldDepositeBenfit)
    TextView txtGoldDepositeBenfit;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_booking_history);
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

    private void init(){

        progressDialog = new TransparentProgressDialog(GoldBookingHistoryActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        getGoldBookingHistoryServiceProvider=new GetGoldBookingHistoryServiceProvider(this);

        //AttemptToGetGoldBookingHistory(VGoldApp.onGetUerId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AttemptToGetGoldBookingHistory(VGoldApp.onGetUerId());
    }

    @OnClick(R.id.btnBookGold)
    public void onClickOfBtnBookGold(){
        startActivity(new Intent(GoldBookingHistoryActivity.this,GoldBookingActivity.class));
    }

    private void AttemptToGetGoldBookingHistory(String user_id) {
        progressDialog.show();
        getGoldBookingHistoryServiceProvider.getGoldBookingHistory(user_id,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetGoldBookingHistoryModel) serviceResponse).getStatus();
                    String message = ((GetGoldBookingHistoryModel) serviceResponse).getMessage();
                    ArrayList<GetGoldBookingHistoryModel.Data> mArrGoldBookingHistory=((GetGoldBookingHistoryModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        if (mArrGoldBookingHistory.size()==0){
                            txtGoldDepositeBenfit.setVisibility(View.VISIBLE);
                        }else {
                            rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(GoldBookingHistoryActivity.this));
                            rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(GoldBookingHistoryActivity.this, mArrGoldBookingHistory));
                        }
                     //   mAlert.onShowToastNotification(GoldBookingHistoryActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(GoldBookingHistoryActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(GoldBookingHistoryActivity.this, message);
                       /* rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(GoldBookingHistoryActivity.this));
                        rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(GoldBookingHistoryActivity.this,mArrGoldBookingHistory));*/

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
                        PrintUtil.showToast(GoldBookingHistoryActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldBookingHistoryActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldBookingHistoryActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {

    }
}
