package com.cognifygroup.vgold;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldModel;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpModel;
import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpServiceProvider;
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpModel;
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpServiceProvider;
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalModel;
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalServiceProvider;
import com.cognifygroup.vgold.transferMoney.TransferMoneyModel;
import com.cognifygroup.vgold.transferMoney.TransferMoneyServiceProvider;
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

public class PayActivity extends AppCompatActivity implements AlertDialogOkListener {
    String name;
    String mobile;
    String mobileno, otp;
    String whichactivity = "0";
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.txtMobile)
    TextView txtMobile;
    @InjectView(R.id.edtAmount)
    EditText edtAmount;
    @InjectView(R.id.btnPay)
    Button btnPay;
    @InjectView(R.id.txtError)
    TextView txtError;
    @InjectView(R.id.btnRefer)
    Button btnRefer;
    @InjectView(R.id.txtGoldRate)
    TextView txtGoldRate;
    @InjectView(R.id.txtViewForGold)
    TextView txtViewForGold;
    @InjectView(R.id.tt)
    TextView tt;

    @InjectView(R.id.txtWalletBalence)
    TextView txtWalletBalence;
    @InjectView(R.id.txtGoldWeight)
    TextView txtGoldWeight;

    @InjectView(R.id.btnAddGoldWallet)
    Button btnAddGoldWallet;

    @InjectView(R.id.llConversion)
    LinearLayout llConversion;

    @InjectView(R.id.acg)
    LinearLayout acg;
    @InjectView(R.id.edtGoldWeight)
    EditText edtGoldWeight;
    @InjectView(R.id.txtSellAmount)
    TextView txtSellAmount;

    @InjectView(R.id.imgSwap)
    ImageView imgSwap;
    @InjectView(R.id.imgSwap1)
    ImageView imgSwap1;

    String balance;
    double result = 0;
    double result1 = 0;
    double amount = 0;
    double weight = 0;
    double walletbalence = 0;
    String goldWeight = "0.0";
    String payment_option = "";
    String todayGoldRate = "0";
    double enterbalence = 0;
    double enterbalence1 = 0;

    private AlertDialogOkListener alertDialogOkListener = this;

    private TransferMoneyServiceProvider transferMoneyServiceProvider;
    private PayGoldOtpServiceProvider payGoldOtpServiceProvider;
    GetTodayGoldRateServiceProvider getTodayGoldRateServiceProvider;
    GetAllTransactionGoldServiceProvider getAllTransactionGoldServiceProvider;
    private TransferGoldFinalServiceProvider transferGoldFinalServiceProvider;
    private AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init() {

        progressDialog = new TransparentProgressDialog(PayActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        transferMoneyServiceProvider = new TransferMoneyServiceProvider(this);
        payGoldOtpServiceProvider = new PayGoldOtpServiceProvider(this);
        getAllTransactionGoldServiceProvider = new GetAllTransactionGoldServiceProvider(this);
        getTodayGoldRateServiceProvider = new GetTodayGoldRateServiceProvider(this);
        transferGoldFinalServiceProvider = new TransferGoldFinalServiceProvider(this);


        TextWatcher textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //edtGoldWeight.addTextChangedListener(null);

                if (s.length() != 0 && !s.toString().equals(".")) {
                    weight = Double.parseDouble(edtGoldWeight.getText().toString());
                    result1 = weight * Double.parseDouble(todayGoldRate);

                    if (result1 != 0.00) {
                        txtSellAmount.setText("" + new DecimalFormat("##.##").format(result1) + " ₹");
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

        edtGoldWeight.addTextChangedListener(textWatcher1);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //edtGoldWeight.addTextChangedListener(null);

                if (s.length() != 0 && !s.toString().equals(".")) {
                    amount = Double.parseDouble(edtAmount.getText().toString());
                    result = amount / Double.parseDouble(todayGoldRate);

                    if (result != 0.00) {
                        goldWeight = "" + new DecimalFormat("##.###").format(result);
                        txtGoldWeight.setText("" + new DecimalFormat("##.###").format(result) + " GM");
                    }
                } else {
                    txtGoldWeight.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //edtGoldWeight.addTextChangedListener(null);


            }
        };

        edtAmount.addTextChangedListener(textWatcher);


        AttemptToGetGoldTransactionHistory(VGoldApp.onGetUerId());
        AttemptToGetTodayGoldRate();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mobile = intent.getStringExtra("number");
        mobileno = intent.getStringExtra("mobileno");
        String barcode = intent.getStringExtra("code");
        whichactivity = intent.getStringExtra("whichactivity");

        if (whichactivity.equals("1")) {
            txtName.setText(name);
            txtMobile.setText(mobile);
        }

        if (whichactivity.equals("3")) {
            txtMobile.setText(mobileno);
        }


        if (whichactivity.equals("2")) {
            // close the activity in case of empty barcode
            if (TextUtils.isEmpty(barcode)) {

                AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "Barcode is empty!",
                        getResources().getString(R.string.btn_ok), 4, false, alertDialogOkListener);

//                mAlert.onShowToastNotification(PayActivity.this, "Barcode is empty!");

            }

            if (!barcode.equals(null) || !barcode.equals("")) {
                searchBarcode(barcode);
            }
        }

    }

    private void searchBarcode(String barcode) {

        if (!barcode.equals("") || !barcode.equals(null)) {
            txtMobile.setText(barcode.toString());
        }


    }

    @OnClick(R.id.imgSwap)
    public void onClickOfImgSwap() {
        llConversion.setVisibility(View.GONE);
        acg.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.imgSwap1)
    public void onClickOfImgSwap1() {
        llConversion.setVisibility(View.VISIBLE);
        acg.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        getTransferMoney(txtMobile.getText().toString());
    }

    @OnClick(R.id.btnAddGoldWallet)
    public void onClickOfBtnAddGoldTowallet() {
        startActivity(new Intent(PayActivity.this, PayActivity.class));
    }

    public void showGoldTransferDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to transfer gold");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (llConversion.getVisibility() == View.VISIBLE) {

                    String name = txtName.getText().toString();

                    walletbalence = Double.parseDouble(balance);
                    String GoldW = goldWeight;
                    if (!GoldW.equals("") && !goldWeight.equals("null")) {
                        enterbalence = Double.parseDouble(GoldW);
                    }


                    if (name.equals("") || name == null) {

                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "He is not a Registered Member, hence refer him.",
                                getResources().getString(R.string.btn_ok), 2, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivity.this, "He is not a Registered Member, hence refer him.");

//                        Toast.makeText(PayActivity.this,"this number is not register with Vgold",Toast.LENGTH_LONG).show();

                    } else {

                        if (result != 0) {

                            if (enterbalence <= walletbalence) {
                                // Toast.makeText(PayActivity.this,""+GoldW,Toast.LENGTH_LONG).show();

                                getOtp(VGoldApp.onGetUerId(), txtMobile.getText().toString(), GoldW);
//                                TransferGold(VGoldApp.onGetUerId(),txtMobile.getText().toString(),GoldW);
                            } else {
                                btnAddGoldWallet.setVisibility(View.VISIBLE);
                                AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "Insufficient wallet balence",
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                                mAlert.onShowToastNotification(PayActivity.this, "Insufficient wallet balence");
                            }
                        } else {
                            AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "enter Amount",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                            mAlert.onShowToastNotification(PayActivity.this, "enter Amount");
                        }
                    }
                } else {
                    String name = txtName.getText().toString();
                    walletbalence = Double.parseDouble(balance);
                    if (name.equals("") || name == null) {

                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "He is not a Registered Member, hence refer him.",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivity.this, "You do not have this Privilege as you are not a registered member");
//                        mAlert.onShowToastNotification(PayActivity.this, "He is not a Registered Member, hence refer him.");

                    } else if (!edtGoldWeight.getText().toString().equals("")) {

                        enterbalence1 = Double.parseDouble(edtGoldWeight.getText().toString());

                        if (enterbalence1 != 0.0) {
                            if (enterbalence1 <= walletbalence) {
                                getOtp(VGoldApp.onGetUerId(), txtMobile.getText().toString(), edtGoldWeight.getText().toString());
//TODO..........................
//                                TransferGold(VGoldApp.onGetUerId(),txtMobile.getText().toString(),edtGoldWeight.getText().toString());
                            } else {
                                AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "Insufficient wallet balence",
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                                mAlert.onShowToastNotification(PayActivity.this, "Insufficient wallet balence");
                            }
                        } else {
                            AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "Please enter amount",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                            mAlert.onShowToastNotification(PayActivity.this, "Please enter amount");
                        }
                    } else {
                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "Please enter amount",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(PayActivity.this, "Please enter amount");


                        /*String GoldWe=edtGoldWeight.getText().toString();
                        if (!GoldWe.equals("") && !GoldWe.equals("null")){
                            enterbalence1= Double.parseDouble(GoldWe);
                        }

                        double walletbalence= Double.parseDouble(balance);

                        if (enterbalence1 <= walletbalence){
                          //  Toast.makeText(PayActivity.this,""+GoldWe,Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(PayActivity.this,"Insufficient wallet balence",Toast.LENGTH_LONG).show();
                        }*/
                    }
                }


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

    @OnClick(R.id.btnPay)
    public void onClickOfbtnPay() {


        showGoldTransferDialog();


    }

    @OnClick(R.id.btnRefer)
    public void onClickOfBtnRefer() {
        startActivity(new Intent(PayActivity.this, ReferActivity.class)
                .putExtra("no", txtMobile.getText().toString()));
    }

    private void getOtp(String user_id, String mobNo, String weight) {

        progressDialog.show();
        payGoldOtpServiceProvider.getAddBankDetails(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((PayGoldOtpModel) serviceResponse).getStatus();
                String message = ((PayGoldOtpModel) serviceResponse).getMessage();
                otp = ((PayGoldOtpModel) serviceResponse).getOtp();
                try {
                    if (Status.equals("200")) {

                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "Otp sent to your register mobile no and mail",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

                        /*startActivity(new Intent(PayActivity.this, Otp1Activity.class)
                                .putExtra("OTP", otp)
                                .putExtra("AMOUNT", "" + result)
                                .putExtra("Weight", weight)
                                .putExtra("NO", mobNo));*/

                    } else {
                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", message,
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
                        PrintUtil.showToast(PayActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void getTransferMoney(String no) {

        progressDialog.show();
        transferMoneyServiceProvider.getAddBankDetails(no, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((TransferMoneyModel) serviceResponse).getStatus();
                String message = ((TransferMoneyModel) serviceResponse).getMessage();
                String Name = ((TransferMoneyModel) serviceResponse).getName();

                txtName.setText(Name);
                try {
                    if (Status.equals("200")) {

                        //  Toast.makeText(PayActivity.this,message,Toast.LENGTH_LONG).show();

                    } else {

//                        mAlert.onShowToastNotification(PayActivity.this, message);
                        txtError.setVisibility(View.VISIBLE);
                        btnRefer.setVisibility(View.VISIBLE);
                        btnPay.setVisibility(View.GONE);
                        edtAmount.setVisibility(View.GONE);
                        llConversion.setVisibility(View.GONE);
                        acg.setVisibility(View.GONE);
                        txtViewForGold.setVisibility(View.GONE);
                        tt.setVisibility(View.GONE);
                        txtGoldRate.setVisibility(View.GONE);

                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", message,
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
                        PrintUtil.showToast(PayActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToGetGoldTransactionHistory(String user_id) {
        // mAlert.onShowProgressDialog(PayActivity.this, true);
        getAllTransactionGoldServiceProvider.getAllTransactionGoldHistory(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetAllTransactionGoldModel) serviceResponse).getStatus();
                    String message = ((GetAllTransactionGoldModel) serviceResponse).getMessage();
                    balance = ((GetAllTransactionGoldModel) serviceResponse).getGold_Balance();
                    txtWalletBalence.setText(balance + " GM");
                    ArrayList<GetAllTransactionGoldModel.Data> mArrGoldTransactonHistory = ((GetAllTransactionGoldModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        // recyclerViewGoldWallet.setLayoutManager(new LinearLayoutManager(PayActivity.this));
                        //recyclerViewGoldWallet.setAdapter(new GoldTransactionAdapter(PayActivity.this,mArrGoldTransactonHistory));

                        // mAlert.onShowToastNotification(PayActivity.this, message);

                    } else {
//                        mAlert.onShowToastNotification(PayActivity.this, message);

                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

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
                        PrintUtil.showToast(PayActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void TransferGold(String user_id, String no, String amount) {

        progressDialog.show();
        transferGoldFinalServiceProvider.transferMoney(user_id, no, amount, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((TransferGoldFinalModel) serviceResponse).getStatus();
                String message = ((TransferGoldFinalModel) serviceResponse).getMessage();
                try {
                    if (Status.equals("200")) {
                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 3, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivity.this, message);


                    } else {
                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", message,
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
                        PrintUtil.showToast(PayActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToGetTodayGoldRate() {
        //mAlert.onShowProgressDialog(PayActivity.this, true);
        getTodayGoldRateServiceProvider.getTodayGoldRate(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetTodayGoldRateModel) serviceResponse).getStatus();
                    String message = ((GetTodayGoldRateModel) serviceResponse).getMessage();
                    todayGoldRate = ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate();
                    txtGoldRate.setText("₹ " + todayGoldRate + "/GM");

                    if (status.equals("200")) {
                        // mAlert.onShowToastNotification(PayActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", message,
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
                        PrintUtil.showToast(PayActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 2:
                Intent intent = new Intent(PayActivity.this, ReferActivity.class);
                startActivity(intent);
                break;
            case 1:
                startActivity(new Intent(PayActivity.this, Otp1Activity.class)
                        .putExtra("OTP", otp)
                        .putExtra("AMOUNT", "" + result)
                        .putExtra("Weight", weight)
                        .putExtra("NO", txtMobile.getText().toString())
                        .putExtra("moveFrom", "payActivity")
                );
                break;
            case 3:
                startActivity(new Intent(PayActivity.this, SuccessActivity.class));
                break;
            case 4:
                finish();
                break;
        }

    }
}
