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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Adapter.GoldBookingHistoryAdapter;
import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.Application.VGoldApp;
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
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        AttemptToGetTodayGoldRate();
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

                        AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
                       /* Intent intent = new Intent(AddGoldActivity.this, SuccessActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                        finish();*/
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
          /*  case 2:
                Intent failIntent = new Intent(AddGoldActivity.this, MainActivity.class);
                startActivity(failIntent);
                break;*/
        }

    }
}
