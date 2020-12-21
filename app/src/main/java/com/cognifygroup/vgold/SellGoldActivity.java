package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Adapter.GoldTransactionAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldModel;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldServiceProvider;
import com.cognifygroup.vgold.getBankDetails.GetBankModel;
import com.cognifygroup.vgold.getBankDetails.GetBankServiceProvider;
import com.cognifygroup.vgold.getTodaySellRate.GetTodayGoldSellModel;
import com.cognifygroup.vgold.getTodaySellRate.GetTodayGoldSellRateServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpModel;
import com.cognifygroup.vgold.sellGold.SellGoldModel;
import com.cognifygroup.vgold.sellGold.SellGoldServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SellGoldActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.edtGoldWeight)
    EditText edtGoldWeight;
    @InjectView(R.id.txtSellAmount)
    TextView txtSellAmount;
    @InjectView(R.id.btnSell)
    TextView btnSell;
    @InjectView(R.id.txtWalletBalence)
    TextView txtWalletBalence;
    @InjectView(R.id.txtGoldRate)
    TextView txtGoldRate;

    double result = 0;
    double amount = 0;
    double weight = 0;
    double enterbalence = 0;
    String TodayGoldSellRate;
    String balance;
    private String goldWeight, amt;

    AlertDialogs mAlert;
    SellGoldServiceProvider sellGoldServiceProvider;
    GetBankServiceProvider getBankServiceProvider;
    GetAllTransactionGoldServiceProvider getAllTransactionGoldServiceProvider;
    GetTodayGoldSellRateServiceProvider getTodayGoldSellRateServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_gold);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new TransparentProgressDialog(SellGoldActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        sellGoldServiceProvider = new SellGoldServiceProvider(this);
        getBankServiceProvider = new GetBankServiceProvider(this);
        getAllTransactionGoldServiceProvider = new GetAllTransactionGoldServiceProvider(this);
        getTodayGoldSellRateServiceProvider = new GetTodayGoldSellRateServiceProvider(this);

        AttemptToGetTodayGoldRate();
        AttemptToGetGoldTransactionHistory(VGoldApp.onGetUerId());


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //edtGoldWeight.addTextChangedListener(null);

                if (s.length() != 0 && !s.toString().equals(".")) {
                    weight = Double.parseDouble(edtGoldWeight.getText().toString());
                    result = weight * Double.parseDouble(TodayGoldSellRate);

                    if (result != 0.00) {
                        txtSellAmount.setText("" + new DecimalFormat("##.##").format(result) + " ₹");
                    }
                } else {
                    txtSellAmount.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //edtGoldWeight.addTextChangedListener(null);


            }
        };

        edtGoldWeight.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnSell)
    public void onClickOfBtnbtnSell() {



        double walletbalence = Double.parseDouble(balance);
        if (!edtGoldWeight.getText().toString().equals("")) {
            enterbalence = Double.parseDouble(edtGoldWeight.getText().toString());


//            AttemptToSellGold(VGoldApp.onGetUerId(), edtGoldWeight.getText().toString(), "" + result);

            if (enterbalence != 0.0 && enterbalence >= 1.0) {
                if (enterbalence <= walletbalence) {

                    goldWeight = edtGoldWeight.getText().toString();
                    amt = "" + result;

                    getOtp(VGoldApp.onGetUerId());

//                    AttemptToSellGold(VGoldApp.onGetUerId(), edtGoldWeight.getText().toString(), "" + result);
                } else {
                    AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", "Insufficient wallet balance",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                }
            } else {
                AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", "Amount should be grater than 1 gm to sell",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

            }
        } else {
            AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", "Please enter amount",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

        }


    }

    private void getOtp(String user_id) {

        progressDialog.show();
        sellGoldServiceProvider.getOTP(user_id, "send_otp", new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((SellGoldModel) serviceResponse).getStatus();
                String message = ((SellGoldModel) serviceResponse).getMessage();
                try {
                    if (Status.equals("200")) {

                        AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", "Otp sent to your register mobile no and mail",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

                        /*startActivity(new Intent(PayActivity.this, Otp1Activity.class)
                                .putExtra("OTP", otp)
                                .putExtra("AMOUNT", "" + result)
                                .putExtra("Weight", weight)
                                .putExtra("NO", mobNo));*/

                    } else {
                        AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivity.this, message);
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
                        PrintUtil.showToast(SellGoldActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToSellGold(String user_id, String gold, String amount) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        sellGoldServiceProvider.getAddBankDetails(user_id, gold, amount, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((SellGoldModel) serviceResponse).getStatus();
                    String message = ((SellGoldModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
                        Intent intent = new Intent(SellGoldActivity.this, SuccessActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                        finish();
                    } else {

                        AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
//                        Intent intent = new Intent(SellGoldActivity.this, MainActivity.class);
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
                        PrintUtil.showToast(SellGoldActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void AttemptToGetGoldTransactionHistory(String user_id) {
        // mAlert.onShowProgressDialog(SellGoldActivity.this, true);
        getAllTransactionGoldServiceProvider.getAllTransactionGoldHistory(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetAllTransactionGoldModel) serviceResponse).getStatus();
                    String message = ((GetAllTransactionGoldModel) serviceResponse).getMessage();
                    balance = ((GetAllTransactionGoldModel) serviceResponse).getGold_Balance();
                    txtWalletBalence.setText(balance + "GM");
                    ArrayList<GetAllTransactionGoldModel.Data> mArrGoldTransactonHistory = ((GetAllTransactionGoldModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        // recyclerViewGoldWallet.setLayoutManager(new LinearLayoutManager(SellGoldActivity.this));
                        //recyclerViewGoldWallet.setAdapter(new GoldTransactionAdapter(SellGoldActivity.this,mArrGoldTransactonHistory));

                        //  mAlert.onShowToastNotification(SellGoldActivity.this, message);

                    } else {

                        AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
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
                        PrintUtil.showToast(SellGoldActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void AttemptToGetTodayGoldRate() {
        // mAlert.onShowProgressDialog(SellGoldActivity.this, true);
        getTodayGoldSellRateServiceProvider.getTodayGoldRate(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetTodayGoldSellModel) serviceResponse).getStatus();
                    String message = ((GetTodayGoldSellModel) serviceResponse).getMessage();
                    TodayGoldSellRate = ((GetTodayGoldSellModel) serviceResponse).getGold_sale_rate();
                    txtGoldRate.setText("₹ " + TodayGoldSellRate + "/GM");

                    if (status.equals("200")) {
                        //  mAlert.onShowToastNotification(SellGoldActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", message,
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
                        PrintUtil.showToast(SellGoldActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(SellGoldActivity.this);
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
                startActivity(new Intent(SellGoldActivity.this, Otp1Activity.class)
                        .putExtra("moveFrom", "SellGold")
                        .putExtra("Weight", goldWeight)
                        .putExtra("AMOUNT", amt)
                );
                break;
        }
    }
}
