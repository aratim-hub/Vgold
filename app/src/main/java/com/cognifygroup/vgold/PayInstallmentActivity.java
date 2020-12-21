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
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.Payumoney.PayUMoneyActivity;
import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.fetchDownPayment.FetchDownPaymentModel;
import com.cognifygroup.vgold.fetchDownPayment.FetchDownPaymentServiceProvider;
import com.cognifygroup.vgold.getBankDetails.GetBankModel;
import com.cognifygroup.vgold.getBankDetails.GetBankServiceProvider;
import com.cognifygroup.vgold.getBookingId.GetBookingIdModel;
import com.cognifygroup.vgold.getBookingId.GetGoldBookingIdServiceProvider;
import com.cognifygroup.vgold.payInstallment.PayInstallmentModel;
import com.cognifygroup.vgold.payInstallment.PayInstallmentServiceProvider;
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

public class PayInstallmentActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.spinner_goldBookingId)
    Spinner spinner_goldBookingId;
    @InjectView(R.id.txtAmount)
    TextView txtAmount;

    @InjectView(R.id.spinner_payment_option)
    Spinner spinner_payment_option;
    @InjectView(R.id.llCheque)
    LinearLayout llCheque;
    @InjectView(R.id.llRTGS)
    LinearLayout llRTGS;
    @InjectView(R.id.btnProceedToPayment1)
    Button btnProceedToPayment1;

    @InjectView(R.id.edtBankDetail)
    EditText edtBankDetail;
    @InjectView(R.id.edtChequeNo)
    EditText edtChequeNo;

    @InjectView(R.id.edtRtgsBankDetail)
    EditText edtRtgsBankDetail;
    @InjectView(R.id.edtTxnId)
    EditText edtTxnId;

    double result = 0;
    double amount = 0;
    String goldWeight = "0.0";
    String payment_option = "";
    String todayGoldRate = "0";
    private String msg;

    AlertDialogs mAlert;
    GetGoldBookingIdServiceProvider getGoldBookingIdServiceProvider;
    FetchDownPaymentServiceProvider fetchDownPaymentServiceProvider;
    PayInstallmentServiceProvider payInstallmentServiceProvider;

    String bookingId;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

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

        AttemptTogetBookingId(VGoldApp.onGetUerId());

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
                    payment_option = "RTGS";
                    llRTGS.setVisibility(View.VISIBLE);
                    llCheque.setVisibility(View.GONE);
                } else if (paymentoption.equals("Wallet")) {
                    payment_option = "Wallet";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                } else if (paymentoption.equals("Credit/Debit/Net Banking(Payment Gateway)")) {
                    payment_option = "Credit/Debit/Net Banking(Payment Gateway)";
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

    @OnClick(R.id.btnProceedToPayment1)
    public void OnClickOfProceedToPayment1() {

        if (!txtAmount.getText().toString().equals("") && txtAmount.getText().toString() != null) {
            if (payment_option.equals("Cheque")) {

                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId, "" + txtAmount.getText().toString(), payment_option, edtBankDetail.getText().toString(), "", edtChequeNo.getText().toString());


            } else if (payment_option.equals("RTGS")) {

                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId, "" + txtAmount.getText().toString(), payment_option, edtRtgsBankDetail.getText().toString(), edtTxnId.getText().toString(), "");

            } else if (payment_option.equals("Wallet")) {

                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId, "" + txtAmount.getText().toString(), payment_option, "", "", "");

            } else if (payment_option.equals("Credit/Debit/Net Banking(Payment Gateway)")) {
                startActivity(new Intent(PayInstallmentActivity.this, PayUMoneyActivity.class)
                        .putExtra("AMOUNT", "" + txtAmount.getText().toString())
                        .putExtra("bookingId", bookingId));

            } else {

                AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please select payment option",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                mAlert.onShowToastNotification(PayInstallmentActivity.this, "Please select payment option");

            }
        } else {
            AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", "Please enter vaild Amount",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(PayInstallmentActivity.this, "Please enter vaild Amount");
        }


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
        getGoldBookingIdServiceProvider.getGoldBookingId(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetBookingIdModel) serviceResponse).getStatus();
                    String message = ((GetBookingIdModel) serviceResponse).getMessage();
                    final ArrayList<GetBookingIdModel.Data> mArrCity = ((GetBookingIdModel) serviceResponse).getData();


                    if (status.equals("200")) {
                        ArrayAdapter<GetBookingIdModel.Data> adapter =
                                new ArrayAdapter<GetBookingIdModel.Data>(PayInstallmentActivity.this, R.layout.support_simple_spinner_dropdown_item, mArrCity);
                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spinner_goldBookingId.setAdapter(adapter);
                        spinner_goldBookingId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                bookingId = String.valueOf(mArrCity.get(position).getId());

                                AttemptTogetDownPayment(bookingId);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {

                        AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
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


    private void AttemptToPayInstallment(String user_id, String gbid, String amountr, String payment_option, String bank_details, String tr_id, String cheque_no) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        payInstallmentServiceProvider.payInstallment(user_id, gbid, amountr, payment_option, bank_details, tr_id, cheque_no, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((PayInstallmentModel) serviceResponse).getStatus();
                    String message = ((PayInstallmentModel) serviceResponse).getMessage();

                    if (status.equals("200")) {
                        msg = message;

                        AlertDialogs.alertDialogOk(PayInstallmentActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayInstallmentActivity.this, message);
//                        Intent intent=new Intent(PayInstallmentActivity.this,SuccessActivity.class);
//                        intent.putExtra("message",message);
//                        startActivity(intent);
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
        }

    }
}
