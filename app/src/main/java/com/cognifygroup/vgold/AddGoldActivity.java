package com.cognifygroup.vgold;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Adapter.GoldBookingHistoryAdapter;
import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.Payumoney.PayUMoneyActivity;
import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.addGold.AddGoldServiceProvider;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateModel;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
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

public class AddGoldActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edtAmount)
    EditText edtAmount;
    @InjectView(R.id.txtGoldWeight)
    TextView txtGoldWeight;
    @InjectView(R.id.txtGoldRate)
    TextView txtGoldRate;
    @InjectView(R.id.spinner_payment_option)
    Spinner spinner_payment_option;
    @InjectView(R.id.llCheque)
    LinearLayout llCheque;
    @InjectView(R.id.llRTGS)
    LinearLayout llRTGS;
    @InjectView(R.id.btnProceedToPayment)
    Button btnProceedToPayment;

    @InjectView(R.id.edtBankDetail)
    EditText edtBankDetail;
    @InjectView(R.id.edtChequeNo)
    EditText edtChequeNo;

    @InjectView(R.id.edtRtgsBankDetail)
    EditText edtRtgsBankDetail;
    @InjectView(R.id.edtTxnId)
    EditText edtTxnId;

    final int UPI_PAYMENT = 0;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    private String succesMsg;

    AlertDialogs mAlert;
    GetTodayGoldRateServiceProvider getTodayGoldRateServiceProvider;
    AddGoldServiceProvider addGoldServiceProvider;
    private AlertDialogOkListener alertDialogOkListener = this;

    double result = 0;
    double amount = 0;
    String goldWeight = "0.0";
    String payment_option = "";
    String todayGoldRate = "0";
    String todayGoldRateWithGst = "0";
    private TransparentProgressDialog progressDialog;
    private LoginStatusServiceProvider loginStatusServiceProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gold);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //edtGoldWeight.addTextChangedListener(null);

                if (s.length() != 0 && !s.toString().equals(".")) {
                    amount = Double.parseDouble(edtAmount.getText().toString());
//                    result = amount / Double.parseDouble(todayGoldRate);
                    result = amount / Double.parseDouble(todayGoldRateWithGst);

                    if (result != 0.00) {
                        goldWeight = "" + new DecimalFormat("##.###").format(result);
                        txtGoldWeight.setText("" + new DecimalFormat("##.###").format(result) + " gm");
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_option, android.R.layout.simple_spinner_item);
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
                    llCheque.setVisibility(View.VISIBLE);
                    llRTGS.setVisibility(View.GONE);
                } else if (paymentoption.equals("NEFT/RTGS/Online Transfer")) {
                    payment_option = "Online";
                    llRTGS.setVisibility(View.VISIBLE);
                    llCheque.setVisibility(View.GONE);
                } else if (paymentoption.equals("Wallet")) {
                    payment_option = "Wallet";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                } else if (paymentoption.equals("Credit/Debit/Net Banking(Payment Gateway)")) {
                    payment_option = "Payumoney";
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        progressDialog = new TransparentProgressDialog(AddGoldActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        getTodayGoldRateServiceProvider = new GetTodayGoldRateServiceProvider(this);
        addGoldServiceProvider = new AddGoldServiceProvider(this);
        loginStatusServiceProvider = new LoginStatusServiceProvider(this);
        checkLoginSession();
        AttemptToGetTodayGoldRate();
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
                        if(!data){
                            AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message,
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
                        PrintUtil.showToast(AddGoldActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(AddGoldActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(AddGoldActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @OnClick(R.id.btnProceedToPayment)
    public void OnClickOfProceedToPayment() {

        if (!goldWeight.equals("0.0") && goldWeight != null) {
            if (payment_option.equals("Cheque")) {

                AttemptToAddGold(VGoldApp.onGetUerId(), goldWeight, "" + amount, payment_option, edtBankDetail.getText().toString(), "", edtChequeNo.getText().toString());


            } else if (payment_option.equals("Online")) {

                AttemptToAddGold(VGoldApp.onGetUerId(), goldWeight, "" + amount, payment_option, edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), "");

            } else if (payment_option.equals("Wallet")) {

                AttemptToAddGold(VGoldApp.onGetUerId(), goldWeight, "" + amount, payment_option, "", "", "");

            } else if (payment_option.equals("UPI Payment")) {
                integrateGpay(amount, goldWeight);

            } else if (payment_option.equals("Payumoney")) {
                startActivity(new Intent(AddGoldActivity.this, PayUMoneyActivity.class)
                        .putExtra("AMOUNT", "" + amount)
                        .putExtra("whichActivity", "gold")
                        .putExtra("goldweight", goldWeight));

            } else {
                AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", "Please select payment option",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                mAlert.onShowToastNotification(AddGoldActivity.this, "Please select payment option");
            }
        } else {
            AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", "Please enter vaild Amount",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(AddGoldActivity.this, "Please enter vaild Amount");
        }
    }

    private void integrateGpay(double amount, String weight) {
        String no = "00000";
        if (VGoldApp.onGetNo() != null && !TextUtils.isEmpty(VGoldApp.onGetNo())) {
            no = VGoldApp.onGetNo().substring(0, 5);
        }
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


        String transNo = VGoldApp.onGetUerId() + "-" + BaseActivity.getDate();


        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "9881136531@okbizaxis")
                        .appendQueryParameter("pn", "VGold Pvt. Ltd.")
                        .appendQueryParameter("mc", "")
                        .appendQueryParameter("tr", transNo)
                        .appendQueryParameter("tn", "GP_ " + weight + "_" + todayGoldRateWithGst + " " + name + "(" + VGoldApp.onGetUerId() + ")")
                        .appendQueryParameter("am", String.valueOf(amount))
                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
                        .build();


        /*Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9881136531@okbizaxis")
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "101222")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", transNo)
                .appendQueryParameter("tn", "GP_ " + weight + "_" + todayGoldRateWithGst + " " + name + "(" + VGoldApp.onGetUerId() + ")")
                .appendQueryParameter("am", String.valueOf(amount))
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
            Toast.makeText(AddGoldActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();

        }


//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("main ", "response " + data);
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
        if (isConnectionAvailable(AddGoldActivity.this)) {
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
//                Log.e("UPI", "payment successfull: "+approvalRefNo);


                AttemptToAddGold(VGoldApp.onGetUerId(), goldWeight, "" + amount, payment_option, "", approvalRefNo, "");

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(AddGoldActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(AddGoldActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(AddGoldActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
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

    private void AttemptToGetTodayGoldRate() {
        progressDialog.show();
        getTodayGoldRateServiceProvider.getTodayGoldRate(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    progressDialog.hide();
                    String status = ((GetTodayGoldRateModel) serviceResponse).getStatus();
                    String message = ((GetTodayGoldRateModel) serviceResponse).getMessage();
                    todayGoldRate = ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate();
                    todayGoldRateWithGst = ((GetTodayGoldRateModel) serviceResponse).getGold_purchase_rate_with_gst();
//                    txtGoldRate.setText("₹ " + todayGoldRate + "/GM");
                    txtGoldRate.setText("₹ " + todayGoldRateWithGst + "/GM");

                    if (status.equals("200")) {
                        // mAlert.onShowToastNotification(AddGoldActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(AddGoldActivity.this, message);

                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                } finally {
                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    progressDialog.hide();
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(AddGoldActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(AddGoldActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(AddGoldActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToAddGold(String user_id, String gold, String amount, String payment_option, String bank_details, String tr_id, String cheque_no) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        addGoldServiceProvider.getAddBankDetails(user_id, gold, amount, payment_option, bank_details, tr_id, cheque_no, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((AddGoldModel) serviceResponse).getStatus();
                    String message = ((AddGoldModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        succesMsg = message;

                       /* AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);*/

//                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
                        Intent intent = new Intent(AddGoldActivity.this, SuccessActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                        finish();
                    } else {

                        AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                       /* mAlert.onShowToastNotification(AddGoldActivity.this, message);
                        Intent intent = new Intent(AddGoldActivity.this, MainActivity.class);
                        startActivity(intent);*/
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
                        PrintUtil.showToast(AddGoldActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(AddGoldActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(AddGoldActivity.this);
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
                Intent intent = new Intent(AddGoldActivity.this, SuccessActivity.class);
                intent.putExtra("message", succesMsg);
                startActivity(intent);
                finish();
                break;
            case 11:
                Intent LogIntent = new Intent(AddGoldActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }

    }
}
