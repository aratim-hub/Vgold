package com.cognifygroup.vgold;

import android.app.Dialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cognifygroup.vgold.Adapter.GoldTransactionAdapter;
import com.cognifygroup.vgold.Adapter.MoneyTransactionAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldModel;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldServiceProvider;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;
import com.cognifygroup.vgold.getTodaySellRate.GetTodayGoldSellModel;
import com.cognifygroup.vgold.getTodaySellRate.GetTodayGoldSellRateServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GoldWalletActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.txtWalletGoldWeight)
    TextView txtWalletGoldWeight;
    @InjectView(R.id.txSaleAmt)
    TextView txSaleAmt;
    @InjectView(R.id.btnAddGoldToWallet)
    Button btnAddGoldToWallet;
  /*  @InjectView(R.id.btnSell)
    Button btnSell;*/
    @InjectView(R.id.recyclerViewGoldWallet)
    RecyclerView recyclerViewGoldWallet;
    Dialog dialog;
    AlertDialogs mAlert;
    GetAllTransactionGoldServiceProvider getAllTransactionGoldServiceProvider;
    GetTodayGoldSellRateServiceProvider getTodayGoldSellRateServiceProvider;
    GetTodayGoldRateServiceProvider getTodayGoldRateServiceProvider;

    private String GetAmount;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_wallet);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

    }

    public void init() {
        progressDialog = new TransparentProgressDialog(GoldWalletActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        getAllTransactionGoldServiceProvider = new GetAllTransactionGoldServiceProvider(this);
        getTodayGoldSellRateServiceProvider = new GetTodayGoldSellRateServiceProvider(this);
        getTodayGoldRateServiceProvider = new GetTodayGoldRateServiceProvider(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

  /*  @OnClick(R.id.btnSell)
    public void BtnSell() {
        startActivity(new Intent(GoldWalletActivity.this, SellGoldActivity.class));
    }*/


    @OnClick(R.id.btnAddGoldToWallet)
    public void AddMoney() {

        startActivity(new Intent(GoldWalletActivity.this, AddGoldActivity.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
        AttemptToGetGoldTransactionHistory(VGoldApp.onGetUerId());
    }


    private void AttemptToGetGoldTransactionHistory(String user_id) {
        progressDialog.show();
        getAllTransactionGoldServiceProvider.getAllTransactionGoldHistory(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetAllTransactionGoldModel) serviceResponse).getStatus();
                    String message = ((GetAllTransactionGoldModel) serviceResponse).getMessage();
                    String balance = ((GetAllTransactionGoldModel) serviceResponse).getGold_Balance();
                    double gold = Double.parseDouble(balance);
                    DecimalFormat numberFormat = new DecimalFormat("#.000");
                    gold = Double.parseDouble(numberFormat.format(gold));
                    txtWalletGoldWeight.setText(gold + " GM");



                    ArrayList<GetAllTransactionGoldModel.Data> mArrGoldTransactonHistory = ((GetAllTransactionGoldModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        recyclerViewGoldWallet.setLayoutManager(new LinearLayoutManager(GoldWalletActivity.this));
                        recyclerViewGoldWallet.setAdapter(new GoldTransactionAdapter(GoldWalletActivity.this, mArrGoldTransactonHistory));


                        AttemptToGetTodayGoldRate(gold);

                        //  mAlert.onShowToastNotification(GoldWalletActivity.this, message);

                    } else {

                        AlertDialogs.alertDialogOk(GoldWalletActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(GoldWalletActivity.this, message);
                        // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
                        //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(MoneyWalletActivity.this,mArrGoldBookingHistory));

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
                        PrintUtil.showToast(GoldWalletActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldWalletActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldWalletActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void
    AttemptToGetTodayGoldRate(double gold) {
        // mAlert.onShowProgressDialog(SellGoldActivity.this, true);
        getTodayGoldRateServiceProvider.getTodayGoldRate(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetTodayGoldRateModel) serviceResponse).getStatus();
                    String message = ((GetTodayGoldRateModel) serviceResponse).getMessage();
                    String todayGoldPurchaseRate = ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate();

                    double sellingRate = gold * Double.parseDouble(todayGoldPurchaseRate);

                    String amt =  new DecimalFormat("##.##").format(sellingRate);
                    txSaleAmt.setText("₹ " + amt);

                    if (status.equals("200")) {
                        //  mAlert.onShowToastNotification(SellGoldActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(GoldWalletActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(SellGoldActivity.this, message);

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
                        PrintUtil.showToast(GoldWalletActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldWalletActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldWalletActivity.this);
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
