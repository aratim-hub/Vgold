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
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.Payumoney.PayUMoneyActivity;
import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.addMoney.AddMoneyModel;
import com.cognifygroup.vgold.addMoney.AddMoneyServiceProvider;
import com.cognifygroup.vgold.getTodaysGoldRate.GetTodayGoldRateServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddMoneyActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edtAddMoney)
    EditText edtAddMoney;
    @InjectView(R.id.btnAddMoney)
    Button btnAddMoney;

    @InjectView(R.id.spinner_payment_option1)
    Spinner spinner_payment_option1;
    @InjectView(R.id.llCheque)
    LinearLayout llCheque;
    @InjectView(R.id.llRTGS)
    LinearLayout llRTGS;

    String payment_option;

    @InjectView(R.id.edtBankDetail1)
    EditText edtBankDetail1;
    @InjectView(R.id.edtChequeNo1)
    EditText edtChequeNo1;

    @InjectView(R.id.edtRtgsBankDetail1)
    EditText edtRtgsBankDetail1;
    @InjectView(R.id.edtTxnId1)
    EditText edtTxnId1;

    AlertDialogs mAlert;
    AddMoneyServiceProvider addMoneyServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          progressDialog = new TransparentProgressDialog(AddMoneyActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        addMoneyServiceProvider=new AddMoneyServiceProvider(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_option1, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner_payment_option1.setAdapter(adapter);
        spinner_payment_option1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String paymentoption= (String) parent.getItemAtPosition(pos);
                if (paymentoption.equals("cheque")){
                    payment_option="Cheque";
                    llCheque.setVisibility(View.VISIBLE);
                    llRTGS.setVisibility(View.GONE);
                }else if(paymentoption.equals("NEFT/RTGS/Online Transfer")){
                    payment_option="Online";
                    llRTGS.setVisibility(View.VISIBLE);
                    llCheque.setVisibility(View.GONE);
                }else if(paymentoption.equals("Credit/Debit/Net Banking(Payment Gateway)")){
                    payment_option="Payu";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                }else {
                    payment_option="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @OnClick(R.id.btnAddMoney)
    public void onClickOfBtnAddMoney(){

        if (payment_option.equals("Cheque")){

            AttemptToAddMoney(VGoldApp.onGetUerId(),""+edtAddMoney.getText().toString(),payment_option,edtBankDetail1.getText().toString(),"",edtChequeNo1.getText().toString());


        }else if (payment_option.equals("Online")){

            AttemptToAddMoney(VGoldApp.onGetUerId(),""+edtAddMoney.getText().toString(),payment_option,edtRtgsBankDetail1.getText().toString(),edtTxnId1.getText().toString(),"");

        }else if (payment_option.equals("Payu")){
            Intent intent=new Intent(AddMoneyActivity.this, PayUMoneyActivity.class);
            intent.putExtra("AMOUNT",edtAddMoney.getText().toString())
            .putExtra("whichActivity","money");
            startActivity(intent);
        }else {

            AlertDialogs.alertDialogOk(AddMoneyActivity.this, "Alert", "Please select payment option",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(AddMoneyActivity.this, "Please select payment option");

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void AttemptToAddMoney(String user_id, String amount, String payment_option, String bank_details, String tr_id, String cheque_no) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        addMoneyServiceProvider.getAddBankDetails(user_id,amount,payment_option,bank_details,tr_id,cheque_no, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((AddMoneyModel) serviceResponse).getStatus();
                    String message = ((AddMoneyModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                      //  mAlert.onShowToastNotification(AddMoneyActivity.this, message);
                        Intent intent=new Intent(AddMoneyActivity.this,SuccessActivity.class);
                        intent.putExtra("message",message);
                        startActivity(intent);
                        finish();
                    } else {
                        AlertDialogs.alertDialogOk(AddMoneyActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(AddMoneyActivity.this, message);
//                        Intent intent=new Intent(AddMoneyActivity.this,MainActivity.class);
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
                        PrintUtil.showToast(AddMoneyActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(AddMoneyActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(AddMoneyActivity.this);
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
