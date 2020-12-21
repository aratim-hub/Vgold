package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.goldbookingrequest.GoldBookingRequestModel;
import com.cognifygroup.vgold.goldbookingrequest.GoldBookingRequestServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

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

    String payment_option;
    String monthly;
    String booking_value;
    String down_payment;
    String gold_rate;
    String quantity;
    String tennure;
    String pc;
    String booking_charge;

    AlertDialogs mAlert;
    GoldBookingRequestServiceProvider goldBookingRequestServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

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

        if (getIntent().hasExtra("monthly")) {

            monthly = getIntent().getStringExtra("monthly");
            booking_value = getIntent().getStringExtra("booking_value");
            down_payment = getIntent().getStringExtra("down_payment");
            gold_rate = getIntent().getStringExtra("gold_rate");
            booking_charge = getIntent().getStringExtra("booking_charge");
            quantity = getIntent().getStringExtra("quantity");
            tennure = getIntent().getStringExtra("tennure");
            pc = getIntent().getStringExtra("pc");


            txtMonthly.setText(monthly);
            txtBookingValue.setText(booking_value);
            txtDownPayment.setText(down_payment);
            txtBookingCharge.setText(booking_charge);
            txtGoldRate.setText(gold_rate);


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
                    } else {
                        llCheque.setVisibility(View.GONE);
                        llRTGS.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }
    }

    @OnClick(R.id.btnPayOnline)
    public void onClickOfBtnPayOnline() {

        if (payment_option.equals("Cheque")) {

            AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment, monthly, gold_rate, quantity, tennure, pc, payment_option, edtBankDetail.getText().toString(), "", edtChequeNo.getText().toString());


        } else if (payment_option.equals("Online")) {

            AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment, monthly, gold_rate, quantity, tennure, pc, payment_option, edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), "");

        } else if (payment_option.equals("Wallet")) {

            AttemptToGoldBookingRequest(VGoldApp.onGetUerId(), booking_value, down_payment, monthly, gold_rate, quantity, tennure, pc, payment_option, edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), "");

        }

    }


    private void AttemptToGoldBookingRequest(String user_id, String booking_value, String down_payment, String monthly, String rate, String gold_weight, String tennure, String pc, String payment_option, String bank_details, String tr_id, String cheque_no) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        goldBookingRequestServiceProvider.getGoldBookingRequest(user_id, booking_value, down_payment, monthly, rate, gold_weight, tennure, pc, payment_option, bank_details, tr_id, cheque_no, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GoldBookingRequestModel) serviceResponse).getStatus();
                    String message = ((GoldBookingRequestModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        //   mAlert.onShowToastNotification(BookingDetailActivity.this, message);
                        Intent intent = new Intent(BookingDetailActivity.this, SuccessActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
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
        }

    }
}
