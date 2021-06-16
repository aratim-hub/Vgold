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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldModel;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldServiceProvider;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
import com.cognifygroup.vgold.goldbookingrequest.GoldBookingRequestModel;
import com.cognifygroup.vgold.goldbookingrequest.GoldBookingRequestServiceProvider;
import com.cognifygroup.vgold.payInstallment.PayInstallmentModel;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseActivity;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BookingDetailActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.txtBookingValue)
    TextView txtBookingValue;
    @InjectView(R.id.txtGoldRate)
    TextView txtGoldRate;
    @InjectView(R.id.txtDownPayment)
    TextView txtDownPayment;
    @InjectView(R.id.txtBookingCharge)
    TextView txtBookingCharge;
    @InjectView(R.id.txtMonthly)
    TextView txtMonthly;

    @InjectView(R.id.spinner_payment_option)
    Spinner spinner_payment_option;
    @InjectView(R.id.llCheque)
    LinearLayout llCheque;
    @InjectView(R.id.llRTGS)
    LinearLayout llRTGS;
    @InjectView(R.id.btnPayOnline)
    Button btnPayOnline;

    @InjectView(R.id.edtBankDetail)
    EditText edtBankDetail;
    @InjectView(R.id.edtChequeNo)
    EditText edtChequeNo;

    @InjectView(R.id.edtRtgsBankDetail)
    EditText edtRtgsBankDetail;
    @InjectView(R.id.edtTxnId)
    EditText edtTxnId;

    @InjectView(R.id.txtInitBookingCharge)
    TextView txtInitBookingCharge;
    @InjectView(R.id.txtBookingChargeDisc)
    TextView txtBookingChargeDisc;
    @InjectView(R.id.initChargeLbl)
    TextView initChargeLbl;
    @InjectView(R.id.discLbl)
    TextView discLbl;


    @InjectView(R.id.calculationLayout)
    LinearLayoutCompat calculationLayout;
    @InjectView(R.id.txtPayableAmtId)
    TextView txtPayableAmtId;
    @InjectView(R.id.txtWalletAmtId)
    TextView txtWalletAmtId;
    @InjectView(R.id.txtSaleRateId)
    TextView txtSaleRateId;
    @InjectView(R.id.txtGSTAmtId)
    TextView txtGSTAmtId;
    @InjectView(R.id.txtGSTPayableAmtId)
    TextView txtGSTPayableAmtId;
    @InjectView(R.id.txtDeductedGoldId)
    TextView txtDeductedGoldId;
    @InjectView(R.id.txtBalanceRemainId)
    TextView txtBalanceRemainId;

    double remainWalletAmt = 0.0;
    private String GoldAmt;

    String payment_option;
    String monthly;
    String booking_value;
    String down_payment;
    String gold_rate;
    String quantity;
    String tennure;
    String pc;
    String initBookingCharge;
    String disc;
    String booking_charge;
    final int UPI_PAYMENT = 0;
    GetAllTransactionMoneyServiceProvider getAllTransactionMoneyServiceProvider;
    GetAllTransactionGoldServiceProvider getAllTransactionGoldServiceProvider;
    GetTodayGoldRateServiceProvider getTodayGoldRateServiceProvider;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    private String moneyWalletBal;

    AlertDialogs mAlert;
    GoldBookingRequestServiceProvider goldBookingRequestServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    private GoldBookingRequestModel.Data dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
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

    public void init() {

        progressDialog = new TransparentProgressDialog(BookingDetailActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        goldBookingRequestServiceProvider = new GoldBookingRequestServiceProvider(this);
        getAllTransactionMoneyServiceProvider = new GetAllTransactionMoneyServiceProvider(this);
        getAllTransactionGoldServiceProvider = new GetAllTransactionGoldServiceProvider(this);
        getTodayGoldRateServiceProvider = new GetTodayGoldRateServiceProvider(this);

        if (getIntent().hasExtra("monthly")) {

            monthly = getIntent().getStringExtra("monthly");
            booking_value = getIntent().getStringExtra("booking_value");
            down_payment = getIntent().getStringExtra("down_payment");
            gold_rate = getIntent().getStringExtra("gold_rate");
            booking_charge = getIntent().getStringExtra("booking_charge");
            quantity = getIntent().getStringExtra("quantity");
            tennure = getIntent().getStringExtra("tennure");
            pc = getIntent().getStringExtra("pc");
            initBookingCharge = getIntent().getStringExtra("initBookingCharge");
            disc = getIntent().getStringExtra("disc");


            txtMonthly.setText(monthly);
            txtBookingValue.setText(booking_value);
            txtDownPayment.setText(down_payment);
            txtBookingCharge.setText(booking_charge);
            txtGoldRate.setText(gold_rate);

            if (disc.equalsIgnoreCase("0")) {
                txtBookingChargeDisc.setVisibility(View.GONE);
                txtInitBookingCharge.setVisibility(View.GONE);
                initChargeLbl.setVisibility(View.GONE);
                discLbl.setVisibility(View.GONE);

            } else {
                txtBookingChargeDisc.setVisibility(View.VISIBLE);
                txtInitBookingCharge.setVisibility(View.VISIBLE);
                initChargeLbl.setVisibility(View.VISIBLE);
                discLbl.setVisibility(View.VISIBLE);

                txtBookingChargeDisc.setText(disc);
                txtInitBookingCharge.setText(initBookingCharge);
            }


            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.payment_option_wallet, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_item);
// Apply the adapter to the spinner
            spinner_payment_option.setAdapter(adapter);
            spinner_payment_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String paymentoption = (String) parent.getItemAtPosition(pos);
                    if (paymentoption.equals("cheque")) {
                        payment_option = "Cheque";
                        calculationLayout.setVisibility(View.GONE);
                        llCheque.setVisibility(View.VISIBLE);
                        llRTGS.setVisibility(View.GONE);
                    } else if (paymentoption.equals("NEFT/RTGS/Online Transfer")) {
                        payment_option = "Online";
                        calculationLayout.setVisibility(View.GONE);
                        llRTGS.setVisibility(View.VISIBLE);
                        llCheque.setVisibility(View.GONE);
                    } else if (paymentoption.equals("Gold Wallet")) {
                        payment_option = "Gold Wallet";
                        calculationLayout.setVisibility(View.VISIBLE);
                        llCheque.setVisibility(View.GONE);
                        llRTGS.setVisibility(View.GONE);

                        AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment,
                                monthly, gold_rate, quantity, tennure, pc, payment_option,
                                edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), "",
                                initBookingCharge, disc, booking_charge,"0");
                    } else if (paymentoption.equals("Money Wallet")) {
                        calculateMoneyAmount(Double.parseDouble(down_payment) + Double.parseDouble(txtBookingCharge.getText().toString().trim()));
                        payment_option = "Money Wallet";
                        calculationLayout.setVisibility(View.VISIBLE);
                        llCheque.setVisibility(View.GONE);
                        llRTGS.setVisibility(View.GONE);
                    } else if (paymentoption.equals("UPI Payment")) {
                        calculationLayout.setVisibility(View.GONE);
                        payment_option = "UPI Payment";
                        llCheque.setVisibility(View.GONE);
                        llRTGS.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            loginStatusServiceProvider = new LoginStatusServiceProvider(this);

            checkLoginSession();
            AttemptToGetMoneyBalance(VGoldApp.onGetUerId());
            AttemptToGetGoldBalance(VGoldApp.onGetUerId());

        }
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
                            AlertDialogs.alertDialogOk(BookingDetailActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(BookingDetailActivity.this, "Alert", message,
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
                        PrintUtil.showToast(BookingDetailActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void calculateMoneyAmount(Double payableAmt) {

        if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equalsIgnoreCase("null")) {
            remainWalletAmt = Double.parseDouble(moneyWalletBal) - payableAmt;
            if (remainWalletAmt > 0) {
                txtWalletAmtId.setText(String.format("%.2f", remainWalletAmt));
            } else {
                AlertDialogs.alertDialogOk(BookingDetailActivity.this, "Alert", "Low balance in money wallet",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
            }
        }
        calculationLayout.setVisibility(View.VISIBLE);
        txtGSTAmtId.setVisibility(View.GONE);
        txtGSTPayableAmtId.setVisibility(View.GONE);
        txtDeductedGoldId.setVisibility(View.GONE);
        txtSaleRateId.setVisibility(View.GONE);
        txtPayableAmtId.setText("Payable Amount : " + getResources().getString(R.string.rs)
                + payableAmt);
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

//                    txtWalletRupee.setText(getResources().getString(R.string.rs));
//                    txtWalletAmount.setText(moneyWalletBal);

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
                        PrintUtil.showToast(BookingDetailActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
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
//                    txtGoldAmount.setText(gold + " GM");

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
                        PrintUtil.showToast(BookingDetailActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
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
//                    txtGoldValue.setText("(Worth ₹ " + amt + ")");

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
                        PrintUtil.showToast(BookingDetailActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @OnClick(R.id.btnPayOnline)
    public void onClickOfBtnPayOnline() {

        if (payment_option.equals("Cheque")) {

            AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment,
                    monthly, gold_rate, quantity, tennure, pc, payment_option,
                    edtBankDetail.getText().toString(), "",
                    edtChequeNo.getText().toString(), initBookingCharge, disc, booking_charge,"0" );
        } else if (payment_option.equals("Online")) {

            AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment,
                    monthly, gold_rate, quantity, tennure, pc, payment_option,
                    edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), ""
                    , initBookingCharge, disc, booking_charge,"0");

        } else if (payment_option.equals("Money Wallet")) {

            double payableAmt = Double.parseDouble(down_payment) + Double.parseDouble(txtBookingCharge.getText().toString().trim());
            if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equalsIgnoreCase("null")) {
                double remainWalletAmt = Double.parseDouble(moneyWalletBal) - payableAmt;
                if (remainWalletAmt > 0) {
                    ShowPopupDialog(null, "moneyWallet", payableAmt);
                } else {
                    AlertDialogs.alertDialogOk(BookingDetailActivity.this, "Alert", "Money Wallet balance is less then entered amount",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                }
            } else {
                ShowPopupDialog(null, "moneyWallet", payableAmt);
            }

        } else if (payment_option.equals("Gold Wallet")) {

            double payableAmt = Double.parseDouble(down_payment) + Double.parseDouble(txtBookingCharge.getText().toString().trim());
            if (GoldAmt != null && !TextUtils.isEmpty(GoldAmt) && !GoldAmt.equalsIgnoreCase("null")) {
                remainWalletAmt = Double.parseDouble(GoldAmt) - payableAmt;
                if (remainWalletAmt > 0) {

                    ShowPopupDialog(dataModel, "goldWallet",payableAmt);
                } else {
                    AlertDialogs.alertDialogOk(BookingDetailActivity.this, "Alert", "Gold Wallet balance is less then entered amount",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                }
            } else {
                ShowPopupDialog(dataModel, "goldWallet",payableAmt);
            }

        } else if (payment_option.equals("UPI Payment")) {
            integrateGpay(Double.parseDouble(down_payment) + Double.parseDouble(txtBookingCharge.getText().toString().trim()),
                    gold_rate, quantity);
        }

    }

    private void ShowPopupDialog(GoldBookingRequestModel.Data dataModel, String key, double payableAmt) {
        final Dialog dialog = new Dialog(BookingDetailActivity.this);
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
                    + payableAmt);
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
                    AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment,
                            monthly, gold_rate, quantity, tennure, pc, payment_option,
                            edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), "",
                            initBookingCharge, disc, booking_charge,"0");
                } else {
                    AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment,
                            monthly, gold_rate, quantity, tennure, pc, payment_option,
                            edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), "",
                            initBookingCharge, disc, booking_charge,"1");
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


    private void integrateGpay(double amt, String goldRate, String weight) {
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
                        .appendQueryParameter("pa", "9881136531@okbizaxis")
                        .appendQueryParameter("pn", "VGold Pvt. Ltd.")
                        .appendQueryParameter("mc", "")
                        .appendQueryParameter("tr", transNo)
                        .appendQueryParameter("tn", "GB_" + weight + "_" + goldRate + " " + name + "(" + VGoldApp.onGetUerId() + ")")
                        .appendQueryParameter("am", String.valueOf(amt))
                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
                        .build();

        /*Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9881136531@okbizaxis")
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")//"28-4092-313-2021-00-14")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", transNo)
                .appendQueryParameter("tn", "GB_" + weight + "_" + goldRate + " " + name + "(" + VGoldApp.onGetUerId() + ")")
                .appendQueryParameter("am", String.valueOf(amt))
//                .appendQueryParameter("am", "10.0")
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
            Toast.makeText(BookingDetailActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();

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
        if (isConnectionAvailable(BookingDetailActivity.this)) {
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
//                Toast.makeText(BookingDetailActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "payment successfull: "+approvalRefNo);

                AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment, monthly,
                        gold_rate, quantity, tennure, pc, payment_option,
                        edtRtgsBankDetail.getText().toString(), approvalRefNo, "",
                        initBookingCharge, disc, booking_charge,"0");

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(BookingDetailActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(BookingDetailActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(BookingDetailActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
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


    private void AttemptToGoldBookingRequest(String user_id, String booking_value,
                                             String down_payment, String monthly, String rate,
                                             String gold_weight, String tennure, String pc,
                                             String payment_option, String bank_details,
                                             String tr_id, String cheque_no, String initBookingCharge,
                                             String disc, String booking_charge, String confirmedVal) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        goldBookingRequestServiceProvider.getGoldBookingRequest(user_id,
                booking_value, down_payment, monthly, rate, gold_weight, tennure, pc,
                payment_option, bank_details, tr_id, cheque_no, initBookingCharge, disc, booking_charge,
                confirmedVal, new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        try {
                            String status = ((GoldBookingRequestModel) serviceResponse).getStatus();
                            String message = ((GoldBookingRequestModel) serviceResponse).getMessage();
                            dataModel = ((GoldBookingRequestModel) serviceResponse).getData();

                            if (status.equals("200")) {

                                if (payment_option.equalsIgnoreCase("Gold Wallet") &&
                                        confirmedVal.equalsIgnoreCase("0")) {

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
                                    Intent intent = new Intent(BookingDetailActivity.this, SuccessActivity.class);
                                    intent.putExtra("message", message);
                                    startActivity(intent);
                                }





                                //   mAlert.onShowToastNotification(BookingDetailActivity.this, message);

                            } else {

                                AlertDialogs.alertDialogOk(BookingDetailActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(BookingDetailActivity.this, message);
//                        Intent intent = new Intent(BookingDetailActivity.this, MainActivity.class);
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
                                PrintUtil.showToast(BookingDetailActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                            } else {
                                PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.showNetworkAvailableToast(BookingDetailActivity.this);
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
                Intent intent = new Intent(BookingDetailActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 11:
                Intent LogIntent = new Intent(BookingDetailActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }

    }
}
