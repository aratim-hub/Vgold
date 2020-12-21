package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.Button;

import com.cognifygroup.vgold.Adapter.GoldBookingHistoryAdapter;
import com.cognifygroup.vgold.Adapter.GoldDepositeHistoryAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.GetGoldDepositeHistory.GetGoldDepositeHistoryModel;
import com.cognifygroup.vgold.GetGoldDepositeHistory.GetGoldDepositeHistoryServiceProvider;
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

public class GoldDepositeHistoryActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.btnDepositeGold)
    Button btnDepositeGold;

    AlertDialogs mAlert;
    GetGoldDepositeHistoryServiceProvider getGoldDepositeHistoryServiceProvider;
    @InjectView(R.id.rvGoldDepositeHistory)
    RecyclerView rvGoldDepositeHistory;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_deposite_history);
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

        progressDialog = new TransparentProgressDialog(GoldDepositeHistoryActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        getGoldDepositeHistoryServiceProvider = new GetGoldDepositeHistoryServiceProvider(this);

        AttemptToGetGoldDepositeHistory(VGoldApp.onGetUerId());

    }

    @OnClick(R.id.btnDepositeGold)
    public void onClickOfbtnDepositeGold() {
        startActivity(new Intent(GoldDepositeHistoryActivity.this, GoldDepositeActivity.class));
    }

    private void AttemptToGetGoldDepositeHistory(String user_id) {
        progressDialog.show();
        getGoldDepositeHistoryServiceProvider.getGoldDepositeHistory(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetGoldDepositeHistoryModel) serviceResponse).getStatus();
                    String message = ((GetGoldDepositeHistoryModel) serviceResponse).getMessage();
                    ArrayList<GetGoldDepositeHistoryModel.Data> mArrGoldBookingHistory = ((GetGoldDepositeHistoryModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        rvGoldDepositeHistory.setLayoutManager(new LinearLayoutManager(GoldDepositeHistoryActivity.this));
                        rvGoldDepositeHistory.setAdapter(new GoldDepositeHistoryAdapter(GoldDepositeHistoryActivity.this, mArrGoldBookingHistory));

                        //    mAlert.onShowToastNotification(GoldDepositeHistoryActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(GoldDepositeHistoryActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);


//                        mAlert.onShowToastNotification(GoldDepositeHistoryActivity.this, message);
//                        rvGoldDepositeHistory.setLayoutManager(new LinearLayoutManager(GoldDepositeHistoryActivity.this));
//                        rvGoldDepositeHistory.setAdapter(new GoldDepositeHistoryAdapter(GoldDepositeHistoryActivity.this, mArrGoldBookingHistory));

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
                        PrintUtil.showToast(GoldDepositeHistoryActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldDepositeHistoryActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldDepositeHistoryActivity.this);
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
