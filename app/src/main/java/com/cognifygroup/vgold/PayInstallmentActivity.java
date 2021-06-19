package com.cognifygroup.vgold;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.Payumoney.PayUMoneyActivity;
import com.cognifygroup.vgold.fetchDownPayment.FetchDownPaymentModel;
import com.cognifygroup.vgold.fetchDownPayment.FetchDownPaymentServiceProvider;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldModel;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldServiceProvider;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyServiceProvider;
import com.cognifygroup.vgold.getBookingId.GetBookingIdModel;
import com.cognifygroup.vgold.getBookingId.GetGoldBookingIdServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
import com.cognifygroup.vgold.payInstallment.PayInstallmentModel;
import com.cognifygroup.vgold.payInstallment.PayInstallmentServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseActivity;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.ColorSpinnerAdapter;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PayInstallmentActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.spinner_goldBookingId)
    Spinner spinner_goldBookingId;
    @InjectView(R.id.txtAmount)
    TextView txtAmount;

    @InjectView(R.id.txtRupee)
    TextView txtRupee;

    @InjectView(R.id.spinner_payment_option)
    Spinner spinner_payment_option;
    @InjectView(R.id.llCheque)
    LinearLayout llCheque;
    @InjectView(R.id.llRTGS)
    LinearLayout llRTGS;
    @InjectView(R.id.btnProceedToPayment1)
    Button btnProceedToPayment1;

    @InjectView(R.id.radioMinAmt)
    RadioButton radioMinAmt;
    @InjectView(R.id.radioOtherAmt)
    RadioButton radioOtherAmt;
    @InjectView(R.id.radioMoneyWallet)
    RadioButton radioMoneyWallet;
    @InjectView(R.id.radioGoldWallet)
    RadioButton radioGoldWallet;
    @InjectView(R.id.radioGroup)
    RadioGroup radioGroup;

    @InjectView(R.id.edtBankDetail)
    EditText edtBankDetail;
    @InjectView(R.id.edtChequeNo)
    EditText edtChequeNo;

    @InjectView(R.id.edtRtgsBankDetail)
    EditText edtRtgsBankDetail;
    @InjectView(R.id.edtTxnId)
    EditText edtTxnId;

    @InjectView(R.id.txtPayableAmount)
    EditText txtPayableAmount;
    @InjectView(R.id.txtOtherAmount)
    EditText txtOtherAmount;

    @InjectView(R.id.txtWalletAmount)
    TextView txtWalletAmount;

    @InjectView(R.id.txtGoldAmount)
    TextView txtGoldAmount;

    @InjectView(R.id.api)
    LinearLayout api;
    @InjectView(R.id.txtWalletRupee)
    TextView txtWalletRupee;
    @InjectView(R.id.txtGoldValue)
    TextView txtGoldValue;

    @InjectView(R.id.calculationLayout)
    LinearLayoutCompat calculationLayout;

    @InjectView(R.id.txtGSTAmtId)
    TextView txtGSTAmtId;
    @InjectView(R.id.txtGSTPayableAmtId)
    TextView txtGSTPayableAmtId;
    @InjectView(R.id.txtDeductedGoldId)
    TextView txtDeductedGoldId;
    @InjectView(R.id.txtSaleRateId)
    TextView txtSaleRateId;
    @InjectView(R.id.txtPayableAmtId)
    TextView txtPayableAmtId;
    @InjectView(R.id.txtWalletAmtId)
    TextView txtWalletAmtId;
    @InjectView(R.id.txtBalanceRemainId)
    TextView txtBalanceRemainId;

    private String moneyWalletBal;
    private String GoldAmt;

    final int UPI_PAYMENT = 0;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    double result = 0;
    double amount = 0;
    String goldWeight = "0.0";
    String payment_option = "";
    String todayGoldRate = "0";
    private String msg;

    double remainWalletAmt = 0.0;
    private PayInstallmentModel.Data dataModel;

    AlertDialogs mAlert;
    GetGoldBookingIdServiceProvider getGoldBookingIdServiceProvider;
    FetchDownPaymentServiceProvider fetchDownPaymentServiceProvider;
    PayInstallmentServiceProvider payInstallmentServiceProvider;
    GetAllTransactionMoneyServiceProvider getAllTransactionMoneyServiceProvider;
    GetAllTransactionGoldServiceProvider getAllTransactionGoldServiceProvider;
    GetTodayGoldRateServiceProvider getTodayGoldRateServiceProvider;

    String bookingId;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_installment);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new TransparentProgressDialog(PayInstallmentActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        getGoldBookingIdServiceProvider = new GetGoldBookingIdServiceProvider(this);
        fetchDownPaymentServiceProvider = new FetchDownPaymentServiceProvider(this);
        payInstallmentServiceProvider = new PayInstallmentServiceProvider(this);
        loginStatusServiceProvider = new LoginStatusServiceProvider(this);
        getAllTransactionMoneyServiceProvider = new GetAllTransactionMoneyServiceProvider(this);
        getAllTransactionGoldServiceProvider = new GetAllTransactionGoldServiceProvider(this);
        getTodayGoldRateServiceProvider = new GetTodayGoldRateServiceProvider(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spinner_payment_option.setAdapter(adapter);

        spinner_payment_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String paymentoption = (String) parent.getItemAtPosition(pos);
                if (paymentoption.equals("cheque")) {
                    payment_option = "Cheque";
                    llCheque.setVisibility(View.VISIBLE);
                    llRTGS.setVisibility(View.GONE);
                } else if (paymentoption.equals("NEFT/RTGS/Online Transfer")) {
                    payment_option = "RTGS";
                    llRTGS.setVisibility(View.VISIBLE);
                    llCheque.setVisibility(View.GONE);
                } /*else if (paymentoption.equals("Gold Wallet")) {
                    payment_option = "Gold Wallet";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                } else if (paymentoption.equals("Money Wallet")) {
                    payment_option = "Money Wallet";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                }*/ else if (paymentoption.equals("Credit/Debit/Net Banking(Payment Gateway)")) {
                    payment_option = "Credit/Debit/Net Banking(Payment Gateway)";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                } else if (paymentoption.equals("UPI Payment")) {
                    payment_option = "UPI Payment";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioMinAmt.isChecked()) {
                    calculationLayout.setVisibility(View.GONE);
                    txtAmount.setVisibility(View.VISIBLE);
                    txtOtherAmount.setVisibility(View.GONE);
                    txtPayableAmount.setVisibility(View.GONE);
                    txtGoldAmount.setVisibility(View.GONE);
                    txtGoldValue.setVisibility(View.GONE);
                    txtWalletAmount.setVisibility(View.GONE);
                    txtWalletRupee.setVisibility(View.GONE);
                    api.setVisibility(View.VISIBLE);

                } else if (radioOtherAmt.isChecked()) {
                    calculationLayout.setVisibility(View.GONE);
                    txtOtherAmount.setText("");
                    txtOtherAmount.setVisibility(View.VISIBLE);
                    txtPayableAmount.setVisibility(View.GONE);
                    api.setVisibility(View.VISIBLE);
                    txtGoldAmount.setVisibility(View.GONE);
                    txtGoldValue.setVisibility(View.GONE);
                    txtWalletAmount.setVisibility(View.GONE);
                    txtWalletRupee.setVisibility(View.GONE);
                } else if (radioMoneyWallet.isChecked()) {
                    AttemptToGetMoneyBalance(VGoldApp.onGetUerId());
                    txtPayableAmount.setText("");
                    txtPayableAmount.setError(null);
                    txtOtherAmount.setVisibility(View.GONE);
                    calculationLayout.setVisibility(View.GONE);
                    txtPayableAmount.setVisibility(View.VISIBLE);
                    txtGoldAmount.setVisibility(View.GONE);
                    txtGoldValue.setVisibility(View.GONE);
                    txtWalletAmount.setVisibility(View.VISIBLE);
                    txtWalletRupee.setVisibility(View.VISIBLE);
                    api.setVisibility(View.GONE);
                } else {
                    AttemptToGetGoldBalance(VGoldApp.onGetUerId());
                    txtPayableAmount.setText("");
                    txtPayableAmount.setError(null);
                    calculationLayout.setVisibility(View.GONE);
                    txtOtherAmount.setVisibility(View.GONE);
                    txtPayableAmount.setVisibility(View.VISIBLE);
                    txtGoldAmount.setVisibility(View.VISIBLE);
                    txtGoldValue.setVisibility(View.VISIBLE);
                    txtWalletAmount.setText("");
                    txtWalletRupee.setText("");
                    api.setVisibility(View.GONE);
                }
            }
        });


        txtPayableAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (radioMoneyWallet.isChecked()) {
                    if (s.length() > 0) {
                        calculateMoneyAmount(String.valueOf(s));
                    } else {
                        txtWalletAmount.setText(moneyWalletBal);
                        calculationLayout.setVisibility(View.GONE);
                    }
                } else if (radioGoldWallet.isChecked()) {

                    if (s.length() > 0) {
                        AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                "" + txtAmount.getText().toString(),
                                "Gold Wallet",
                                "", "",
                                txtPayableAmount.getText().toString(),
                                "", "0");
                    } else {
                        txtWalletAmount.setText(moneyWalletBal);
                        calculationLayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        checkLoginSession();
        AttemptTogetBookingId(VGoldApp.onGetUerId());
    }

    private void calculateMoneyAmount(String payableAmt) {
        if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equalsIgnoreCase("null")) {
            remainWalletAmt = Double.parseDouble(moneyWalletBal) - Double.parseDouble(payableAmt);
            if (remainWalletAmt < 0) {
                txtPayableAmount.setError("Amount Exceed");
//                txtWalletAmount.setText(String.format("%.2f", remainWalletAmt));
            } else {

            }
        }
        calculationLayout.setVisibility(View.VISIBLE);
        txtGSTAmtId.setVisibility(View.GONE);
        txtGSTPayableAmtId.setVisibility(View.GONE);
        txtDeductedGoldId.setVisibility(View.GONE);
        txtSaleRateId.setVisibility(View.GONE);
        txtPayableAmtId.setText("Payable Amount : " + getResources().getString(R.string.rs)
                + txtPayableAmount.getText().toString());
        txtWalletAmtId.setText("Money in Wallet : " + getResources().getString(R.string.rs)
                + moneyWalletBal);
        txtBalanceRemainId.setText("Remaining Money in Wallet : "
                + getResources().getString(R.string.rs) + remainWalletAmt);
    }

    private void AttemptToGetMoneyBalance(String user_id) {
        progressDialog.show();
        getAllTransactionMoneyServiceProvider.getAllTransactionMoneyHistory(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetAllTransactionMoneyModel) serviceResponse).getStatus();
                    String message = ((GetAllTransactionMoneyModel) serviceResponse).getMessage();
                    moneyWalletBal = ((GetAllTransactionMoneyModel) serviceResponse).getWallet_Balance();

//                    walletAmtId.setText("Money Wallet Balance " + getResources().getString(R.string.rs) + "" + moneyWalletBal);
                    txtWalletRupee.setText(getResources().getString(R.string.rs));
                    txtWalletAmount.setText(moneyWalletBal);

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
                        PrintUtil.showToast(PayInstallmentActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToGetGoldBalance(String user_id) {
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
//                    walletAmtId.setText("Gold Wallet Balance " + gold + " GM");
                    txtGoldAmount.setText(gold + " GM");

                    AttemptToGetTodayGoldRate(gold);

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
                        PrintUtil.showToast(PayInstallmentActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToGetTodayGoldRate(double gold) {
        getTodayGoldRateServiceProvider.getTodayGoldRate(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetTodayGoldRateModel) serviceResponse).getStatus();
                    String message = ((GetTodayGoldRateModel) serviceResponse).getMessage();
                    String todayGoldPurchaseRate = ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate();

                    double sellingRate = gold * Double.parseDouble(todayGoldPurchaseRate);

                    GoldAmt = new DecimalFormat("##.##").format(sellingRate);
                    txtGoldValue.setText("(Worth ₹ " + GoldAmt + ")");

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
                        PrintUtil.showToast(PayInstallmentActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
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
                            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", message,
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
                        PrintUtil.showToast(PayInstallmentActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
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

    @OnClick(R.id.btnProceedToPayment1)
    public void OnClickOfProceedToPayment1() {
        if (spinner_goldBookingId.getSelectedItemPosition() != 0) {

//            if (radioMinAmt.isChecked()) {
//                txtOtherAmount.setVisibility(View.GONE);
//            } else {
//                txtOtherAmount.setVisibility(View.VISIBLE);
//            }

            if (api.getVisibility() == View.VISIBLE) {
                if (spinner_payment_option.getSelectedItemPosition() != 0) {

                    if (txtOtherAmount.getVisibility() == View.VISIBLE) {

                        if (txtOtherAmount.getText().toString() != null && !TextUtils.isEmpty(txtOtherAmount.getText().toString())) {
                            Double otherAmt = Double.valueOf(txtOtherAmount.getText().toString());

//                    if (!txtAmount.getText().toString().equals("") && txtAmount.getText().toString() != null) {
//                        Double minAmt = Double.valueOf(txtAmount.getText().toString());

                            if (otherAmt > 0) {
                                if (payment_option.equals("Cheque")) {

                                    AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                            "" + txtAmount.getText().toString(), payment_option,
                                            edtBankDetail.getText().toString(), "", txtOtherAmount.getText().toString(),
                                            edtChequeNo.getText().toString(), "0");


                                } else if (payment_option.equals("RTGS")) {

                                    AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                            "" + txtAmount.getText().toString(), payment_option,
                                            edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(),
                                            txtOtherAmount.getText().toString(), "", "0");

                                } /*else if (payment_option.equals("Money Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "", txtOtherAmount.getText().toString(),
                                        "", "0");

                            } else if (payment_option.equals("Gold Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "", txtOtherAmount.getText().toString(),
                                        "", "0");

                            }*/ else if (payment_option.equals("UPI Payment")) {
                                    integrateGpay(bookingId, txtOtherAmount.getText().toString());

                                } else if (payment_option.equals("Credit/Debit/Net Banking(Payment Gateway)")) {
                                    startActivity(new Intent(PayInstallmentActivity.this, PayUMoneyActivity.class)
                                            .putExtra("AMOUNT", "" + txtAmount.getText().toString())
                                            .putExtra("OTHERAMOUNT", "" + txtOtherAmount.getText().toString())
                                            .putExtra("bookingId", bookingId));

                                } else {

                                    AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please select payment option",
                                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

                                }
                            } else {
                                AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Other amount should be greater than 0",
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                            }
                        } else {
                            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please enter vaild Amount",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                        }

                    } else {
                        if (!txtAmount.getText().toString().equals("") && txtAmount.getText().toString() != null) {

                            if (payment_option.equals("Cheque")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        edtBankDetail.getText().toString(), "", txtOtherAmount.getText().toString(),
                                        edtChequeNo.getText().toString(), "0");


                            } else if (payment_option.equals("RTGS")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(),
                                        txtOtherAmount.getText().toString(), "", "0");

                            } /*else if (payment_option.equals("Money Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "",
                                        txtOtherAmount.getText().toString(), "", "0");

                            } else if (payment_option.equals("Gold Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "",
                                        txtOtherAmount.getText().toString(), "", "0");

                            }*/ else if (payment_option.equals("UPI Payment")) {
                                integrateGpay(bookingId, txtAmount.getText().toString());

                            } else if (payment_option.equals("Credit/Debit/Net Banking(Payment Gateway)")) {
                                startActivity(new Intent(PayInstallmentActivity.this, PayUMoneyActivity.class)
                                        .putExtra("AMOUNT", "" + txtAmount.getText().toString())
                                        .putExtra("OTHERAMOUNT", "" + txtOtherAmount.getText().toString())
                                        .putExtra("bookingId", bookingId));

                            } else {
                                AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please select payment option",
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

                            }
                        } else {
                            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please enter vaild Amount",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                        }

                    }
                } else {
                    AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please Select Payment Option",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                }
            } else if (radioMoneyWallet.isChecked()) {
                if (txtPayableAmount.getText().toString() != null && !TextUtils.isEmpty(txtPayableAmount.getText().toString())) {
                    double otherAmt = Double.valueOf(txtPayableAmount.getText().toString());

                    if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equalsIgnoreCase("null")) {
                        remainWalletAmt = Double.parseDouble(moneyWalletBal) - otherAmt;
                        if (remainWalletAmt > 0) {
                            if (otherAmt > 0) {
                                ShowPopupDialog(null, "moneyWallet");
                            } else {
                                AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Payable amount should be greater than 0",
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                            }
                        } else {
                            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Money Wallet balance is less then entered amount",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                        }
                    } else {
                        if (otherAmt > 0) {
                            ShowPopupDialog(null, "moneyWallet");
                        } else {
                            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Payable amount should be greater than 0",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                        }
                    }
                } else {
                    AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please enter Payable Amount",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                }
            } else if (radioGoldWallet.isChecked()) {
                if (txtPayableAmount.getText().toString() != null && !TextUtils.isEmpty(txtPayableAmount.getText().toString())) {
                    double otherAmt = Double.valueOf(txtPayableAmount.getText().toString());
                    if (GoldAmt != null && !TextUtils.isEmpty(GoldAmt) && !GoldAmt.equalsIgnoreCase("null")) {
                        remainWalletAmt = Double.parseDouble(GoldAmt) - otherAmt;
                        if (remainWalletAmt > 0) {
                            if (otherAmt > 0) {

                                ShowPopupDialog(dataModel, "goldWallet");
                            } else {
                                AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Payable amount should be greater than 0",
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                            }
                        } else {
                            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Gold Wallet balance is less then entered amount",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                        }
                    } else {
                        if (otherAmt > 0) {
                            ShowPopupDialog(dataModel, "goldWallet");
                        } else {
                            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Payable amount should be greater than 0",
                                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                        }
                    }
                } else {
                    AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please enter Payable Amount",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                }
            } else {
                AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please Select Payment Mode",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
            }

        } else {
            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please Select Booking Id",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
        }
    }

    private void ShowPopupDialog(PayInstallmentModel.Data dataModel, String key) {
        final Dialog dialog = new Dialog(PayInstallmentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wallet_payment_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatButton btnYes = dialog.findViewById(R.id.btnYes);
        AppCompatButton btnNo = dialog.findViewById(R.id.btnNo);
        TextView txtBalanceRemainId = dialog.findViewById(R.id.txtBalanceRemainId);
        TextView txtPayableAmtId = dialog.findViewById(R.id.txtPayableAmtId);
        TextView txtSaleRateId = dialog.findViewById(R.id.txtSaleRateId);
        TextView txtGSTAmtId = dialog.findViewById(R.id.txtGSTAmtId);
        TextView txtWalletAmtId = dialog.findViewById(R.id.txtWalletAmtId);
        TextView txtGSTPayableAmtId = dialog.findViewById(R.id.txtGSTPayableAmtId);
        TextView txtDeductedGoldId = dialog.findViewById(R.id.txtDeductedGoldId);

        if (key.equalsIgnoreCase("moneyWallet")) {
            txtGSTAmtId.setVisibility(View.GONE);
            txtGSTPayableAmtId.setVisibility(View.GONE);
            txtDeductedGoldId.setVisibility(View.GONE);
            txtSaleRateId.setVisibility(View.GONE);
            txtPayableAmtId.setText("Payable Amount : " + getResources().getString(R.string.rs)
                    + txtPayableAmount.getText().toString());
            txtWalletAmtId.setText("Money in Wallet : " + getResources().getString(R.string.rs)
                    + moneyWalletBal);
            txtBalanceRemainId.setText("Remaining Money in Wallet : "
                    + getResources().getString(R.string.rs) + remainWalletAmt);
        } else {
            if (dataModel != null) {
                txtGSTAmtId.setVisibility(View.VISIBLE);
                txtGSTPayableAmtId.setVisibility(View.VISIBLE);
                txtDeductedGoldId.setVisibility(View.VISIBLE);
                txtSaleRateId.setVisibility(View.VISIBLE);

                txtPayableAmtId.setText("Payable Amount : " + getResources().getString(R.string.rs)
                        + dataModel.getAmount_to_pay());
                txtWalletAmtId.setText("Gold in Wallet : " + dataModel.getGold_in_wallet() + " gm");
                txtSaleRateId.setText("Today's Gold Sale Rate : " + getResources().getString(R.string.rs)
                        + dataModel.getToday_sale_rate());
                txtGSTAmtId.setText("GST for Today's Gold Sale Rate : " + dataModel.getGst_per() + "%");
                txtGSTPayableAmtId.setText("GST on Payable Amount : " + getResources().getString(R.string.rs)
                        + dataModel.getAmount_to_pay_gst());
                txtDeductedGoldId.setText("Deducted Gold from Wallet : " + dataModel.getGold_reduce() + " gm");
                txtBalanceRemainId.setText("Remaining Gold in Wallet : "
                        + dataModel.getReduced_gold_in_wallet() + " gm");
            }
        }

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equalsIgnoreCase("moneyWallet")) {
                    AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                            "" + txtAmount.getText().toString(), "Money Wallet",
                            "", "", txtPayableAmount.getText().toString(),
                            "", "0");
                } else {
                    AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                            "" + txtAmount.getText().toString(), "Gold Wallet",
                            "", "",
                            txtPayableAmount.getText().toString(), "", "1");
                }

                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void integrateGpay(String bookingId, String amount) {
        String no = "00000";
        if (VGoldApp.onGetNo() != null && !TextUtils.isEmpty(VGoldApp.onGetNo())) {
            no = VGoldApp.onGetNo().substring(0, 5);
        }

        String transNo = VGoldApp.onGetUerId() + "-" + BaseActivity.getDate();

        String name;
        if (VGoldApp.onGetFirst() != null && !TextUtils.isEmpty(VGoldApp.onGetFirst())) {
            if (VGoldApp.onGetLast() != null && !TextUtils.isEmpty(VGoldApp.onGetLast())) {
                name = VGoldApp.onGetFirst() + " " + VGoldApp.onGetLast();
            } else {
                name = VGoldApp.onGetFirst();
            }
        } else {
            name = "NA";
        }


        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
//                        .appendQueryParameter("pa", "vgold@hdfcbank")
                        .appendQueryParameter("pa", "9881136531@okbizaxis")
                        .appendQueryParameter("pn", "VGold Pvt. Ltd.")
                        .appendQueryParameter("mc", "")
                        .appendQueryParameter("tr", transNo)
                        .appendQueryParameter("tn", "Inst " + name + "(" + bookingId + ")")
                        .appendQueryParameter("am", amount)
                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
                        .build();

       /* Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
//                        .appendQueryParameter("pa", "9881136531@okbizaxis")
                        .appendQueryParameter("pa", "vgold@hdfcbank")
                        .appendQueryParameter("pn", "VGold Pvt. Ltd.")
//                        .appendQueryParameter("mc", "101222")
                        .appendQueryParameter("mc", "1012")
                        .appendQueryParameter("tr", transNo)
                        .appendQueryParameter("tn", "Inst " + name + "(" + bookingId + ")")
//                        .appendQueryParameter("am", amount)
                        .appendQueryParameter("am", "10.00")
                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
                        .build();*/


        /*Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9881136531@okbizaxis")
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", transNo)
                .appendQueryParameter("tn", "Inst " + name + "(" + bookingId + ")")
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();*/

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PayInstallmentActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();

        }

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response " + resultCode);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PayInstallmentActivity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
//                Toast.makeText(PayInstallmentActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "payment successfull: "+approvalRefNo);

                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                        "" + txtAmount.getText().toString(), payment_option,
                        "", approvalRefNo, txtOtherAmount.getText().toString(),
                        "", "0");

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PayInstallmentActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(PayInstallmentActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(PayInstallmentActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    private void AttemptTogetDownPayment(String gbid) {
        // mAlert.onShowProgressDialog(SignUpActivity.this, true);
        fetchDownPaymentServiceProvider.getAddBankDetails(gbid, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((FetchDownPaymentModel) serviceResponse).getStatus();
                    String message = ((FetchDownPaymentModel) serviceResponse).getMessage();
                    String amount = ((FetchDownPaymentModel) serviceResponse).getMonthly_installment();


                    if (status.equals("200")) {

                        txtAmount.setText(amount);
                        txtRupee.setText(getResources().getString(R.string.rs));
                        txtAmount.setVisibility(View.VISIBLE);

//                        if (radioOtherAmt.isChecked()) {
//                            txtOtherAmount.setVisibility(View.GONE);
//                        } else {
//                            txtOtherAmount.setVisibility(View.VISIBLE);
//                        }


                    } else {
                        txtAmount.setText("");
                        // mAlert.onShowToastNotification(PayInstallmentActivity.this, message);
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
                        PrintUtil.showToast(PayInstallmentActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }

    private void AttemptTogetBookingId(String user_id) {
        // mAlert.onShowProgressDialog(SignUpActivity.this, true);
//        getGoldBookingIdServiceProvider.getGoldBookingId(user_id, new APICallback() {
        getGoldBookingIdServiceProvider.getGoldBookingId("1113", new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetBookingIdModel) serviceResponse).getStatus();
                    String message = ((GetBookingIdModel) serviceResponse).getMessage();
                    final ArrayList<GetBookingIdModel.Data> mArrCity = ((GetBookingIdModel) serviceResponse).getData();

                    if (status.equals("200")) {

                        ColorSpinnerAdapter maritalStatusSpinnerAdapter = new ColorSpinnerAdapter(PayInstallmentActivity.this,
                                R.layout.support_simple_spinner_dropdown_item, mArrCity);
                        maritalStatusSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
                        maritalStatusSpinnerAdapter.notifyDataSetChanged();
                        spinner_goldBookingId.setAdapter(maritalStatusSpinnerAdapter);

                    } else {

                        AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 4, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(PayInstallmentActivity.this, message);
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
                        PrintUtil.showToast(PayInstallmentActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }

    private void AttemptToPayInstallment(String user_id, String gbid, String amountr,
                                         String payment_option, String bank_details,
                                         String tr_id, String otherAmount, String cheque_no,
                                         String confirmVal) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        payInstallmentServiceProvider.payInstallment(user_id, gbid, amountr, payment_option,
                bank_details, tr_id, otherAmount, cheque_no, confirmVal, new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        try {
                            String status = ((PayInstallmentModel) serviceResponse).getStatus();
                            String message = ((PayInstallmentModel) serviceResponse).getMessage();
                            dataModel = ((PayInstallmentModel) serviceResponse).getData();

                            if (status.equals("200")) {
                                msg = message;

                                if (payment_option.equalsIgnoreCase("Gold Wallet") &&
                                        confirmVal.equalsIgnoreCase("0")) {

                                    calculationLayout.setVisibility(View.VISIBLE);
                                    txtGSTAmtId.setVisibility(View.VISIBLE);
                                    txtGSTPayableAmtId.setVisibility(View.VISIBLE);
                                    txtDeductedGoldId.setVisibility(View.VISIBLE);
                                    txtSaleRateId.setVisibility(View.VISIBLE);

                                    txtPayableAmtId.setText("Payable Amount : " + getResources().getString(R.string.rs)
                                            + dataModel.getAmount_to_pay());
                                    txtWalletAmtId.setText("Gold in Wallet : " + dataModel.getGold_in_wallet() + " gm");
                                    txtSaleRateId.setText("Today's Gold Sale Rate : " + getResources().getString(R.string.rs)
                                            + dataModel.getToday_sale_rate());
                                    txtGSTAmtId.setText("GST for Today's Gold Sale Rate : " + dataModel.getGst_per() + "%");
                                    txtGSTPayableAmtId.setText("GST on Payable Amount : " + getResources().getString(R.string.rs)
                                            + dataModel.getAmount_to_pay_gst());
                                    txtDeductedGoldId.setText("Deducted Gold from Wallet : " + dataModel.getGold_reduce() + " gm");
                                    txtBalanceRemainId.setText("Remaining Gold in Wallet : "
                                            + dataModel.getReduced_gold_in_wallet() + " gm");

//                                    ShowPopupDialog(dataModel, "goldWallet");

                                } else {
                                    Intent intent = new Intent(PayInstallmentActivity.this, SuccessActivity.class);
                                    intent.putExtra("message", message);
                                    startActivity(intent);
                                }

                            } else {

                                AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayInstallmentActivity.this, message);
//                        Intent intent=new Intent(PayInstallmentActivity.this,MainActivity.class);
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
                                PrintUtil.showToast(PayInstallmentActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                            } else {
                                PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.showNetworkAvailableToast(PayInstallmentActivity.this);
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
                Intent intent = new Intent(PayInstallmentActivity.this, SuccessActivity.class);
                intent.putExtra("message", msg);
                startActivity(intent);
                break;
            case 4:
                Intent intentHome = new Intent(PayInstallmentActivity.this, MainActivity.class);
                startActivity(intentHome);
                break;

            case 11:
                Intent LogIntent = new Intent(PayInstallmentActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }

    }
}
