package com.cognifygroup.vgold;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalModel;
import com.cognifygroup.vgold.TransferMoneyFinal.TransferMoneyFinalServiceProvider;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyServiceProvider;
import com.cognifygroup.vgold.login.LoginModel;
import com.cognifygroup.vgold.login.LoginServiceProvider;
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpModel;
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpServiceProvider;
import com.cognifygroup.vgold.transferMoney.TransferMoneyModel;
import com.cognifygroup.vgold.transferMoney.TransferMoneyServiceProvider;
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

public class PayActivityForMoney extends AppCompatActivity implements AlertDialogOkListener {
    String name;
    String mobile;
    String mobileno;
    String otp;
    double enterbalence=0;
    String whichactivity="0";
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.txtMobile)
    TextView txtMobile;
    @InjectView(R.id.edtAmount)
    EditText edtAmount;
    @InjectView(R.id.btnPay)
    Button btnPay;
    @InjectView(R.id.txtWalletBalence)
    TextView txtWalletBalence;

    @InjectView(R.id.txtError)
    TextView txtError;
    @InjectView(R.id.btnRefer)
    Button btnRefer;

    @InjectView(R.id.btnAddGoldWallet)
    Button btnAddGoldWallet;

    private TransferMoneyServiceProvider transferMoneyServiceProvider;
    private PayMoneyOtpServiceProvider payMoneyOtpServiceProvider;
    GetAllTransactionMoneyServiceProvider getAllTransactionMoneyServiceProvider;
    private TransferMoneyFinalServiceProvider transferMoneyFinalServiceProvider;
    private AlertDialogs mAlert;

    String balance;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_money);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
          progressDialog = new TransparentProgressDialog(PayActivityForMoney.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        transferMoneyServiceProvider=new TransferMoneyServiceProvider(this);
        payMoneyOtpServiceProvider=new PayMoneyOtpServiceProvider(this);
        getAllTransactionMoneyServiceProvider=new GetAllTransactionMoneyServiceProvider(this);
        transferMoneyFinalServiceProvider=new TransferMoneyFinalServiceProvider(this);

        /*btnPay.setVisibility(View.INVISIBLE);*/
        AttemptToGetMoneyTransactionHistory(VGoldApp.onGetUerId());


        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        mobile=intent.getStringExtra("number");
        mobileno=intent.getStringExtra("mobileno");
        String barcode = intent.getStringExtra("code");
        whichactivity= intent.getStringExtra("whichactivity");

        if (whichactivity.equals("1")){
            txtName.setText(name);
            txtMobile.setText(mobile);
        }

        if (whichactivity.equals("3")){
            txtMobile.setText(mobileno);
        }


        if (whichactivity.equals("2")){
            // close the activity in case of empty barcode
            if (TextUtils.isEmpty(barcode)) {

                AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", "Barcode is empty!",
                        getResources().getString(R.string.btn_ok), 2, false, alertDialogOkListener);
//                mAlert.onShowToastNotification(PayActivityForMoney.this, "Barcode is empty!");
//                finish();
            }

            if (!barcode.equals(null) || !barcode.equals(""))
            {
                searchBarcode(barcode);
            }
        }

    }

    private void searchBarcode(String barcode) {

        if (!barcode.equals("") || !barcode.equals(null)){
            txtMobile.setText(barcode.toString());
        }


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


    @OnClick(R.id.btnRefer)
    public void onClickOfBtnRefer(){
        startActivity(new Intent(PayActivityForMoney.this,ReferActivity.class)
                .putExtra("no",txtMobile.getText().toString()));
    }

    @OnClick(R.id.btnAddGoldWallet)
    public void onClickOfBtnAddGoldTowallet(){
        startActivity(new Intent(PayActivityForMoney.this,AddMoneyActivity.class));
    }

    @OnClick(R.id.btnPay)
    public void onClickOfbtnPay(){

        String amount=edtAmount.getText().toString();
        String name=txtName.getText().toString();

        double walletbalence= Double.parseDouble(balance);
        if (!amount.equals("") && amount!=null) {
             enterbalence = Double.parseDouble(edtAmount.getText().toString());
        }
        if (name.equals("") || name==null){

            AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", "This number is not register with Vgold",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//            mAlert.onShowToastNotification(PayActivityForMoney.this, "This number is not register with Vgold");
        }else {

            if (!amount.equals("") && amount!=null){

                if (enterbalence <= walletbalence){
                    TransferMoney(VGoldApp.onGetUerId(),txtMobile.getText().toString(),amount);
                }else {
                    btnAddGoldWallet.setVisibility(View.VISIBLE);
                    AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", "Insufficient wallet balence",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                    mAlert.onShowToastNotification(PayActivityForMoney.this, "Insufficient wallet balence");
                }
            }else {
                AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", "Enter amount",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                mAlert.onShowToastNotification(PayActivityForMoney.this, "Enter amount");
            }
        }
    }


    private void getOtp(String user_id) {

        progressDialog.show();
        payMoneyOtpServiceProvider.getAddBankDetails(user_id,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((PayMoneyOtpModel) serviceResponse).getStatus();
                String message = ((PayMoneyOtpModel) serviceResponse).getMessage();
                 otp=((PayMoneyOtpModel) serviceResponse).getOtp();
                try {
                    if (Status.equals("200")) {

                    //    mAlert.onShowToastNotification(PayActivityForMoney.this, "Otp sent to your register mobile no and mail");

                       startActivity(new Intent(PayActivityForMoney.this,OtpActivity.class).putExtra("OTP",otp)
                       .putExtra("AMOUNT",edtAmount.getText().toString())
                       .putExtra("NO",txtMobile.getText().toString()));

                    } else {
                        AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
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
                        PrintUtil.showToast(PayActivityForMoney.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void getTransferMoney(String no) {

       // mAlert.onShowProgressDialog(PayActivityForMoney.this, true);
        transferMoneyServiceProvider.getAddBankDetails(no,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((TransferMoneyModel) serviceResponse).getStatus();
                String message = ((TransferMoneyModel) serviceResponse).getMessage();
                String Name=((TransferMoneyModel) serviceResponse).getName();
                txtName.setText(Name);
                try {
                    if (Status.equals("200")) {

                        AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
                       // btnPay.setVisibility(View.VISIBLE);

                    } else {
//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
                        txtError.setVisibility(View.VISIBLE);
                        btnRefer.setVisibility(View.VISIBLE);
                        btnPay.setVisibility(View.GONE);
                        edtAmount.setVisibility(View.GONE);

                        AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", message,
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
                        PrintUtil.showToast(PayActivityForMoney.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void AttemptToGetMoneyTransactionHistory(String user_id) {
      //  mAlert.onShowProgressDialog(PayActivityForMoney.this, true);
        getAllTransactionMoneyServiceProvider.getAllTransactionMoneyHistory(user_id,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetAllTransactionMoneyModel) serviceResponse).getStatus();
                    String message = ((GetAllTransactionMoneyModel) serviceResponse).getMessage();
                    balance=((GetAllTransactionMoneyModel) serviceResponse).getWallet_Balance();
                    txtWalletBalence.setText("\u20B9 "+balance);
                    ArrayList<GetAllTransactionMoneyModel.Data> mArrMoneyTransactonHistory=((GetAllTransactionMoneyModel) serviceResponse).getData();

                    if (status.equals("200")) {

                        //       mAlert.onShowToastNotification(PayActivityForMoney.this, message);

                    } else {

                        AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
                        // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(PayActivityForMoney.this));
                        //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(PayActivityForMoney.this,mArrGoldBookingHistory));

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
                        PrintUtil.showToast(PayActivityForMoney.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void TransferMoney(String user_id,String no,String amount) {

        progressDialog.show();
        transferMoneyFinalServiceProvider.transferMoney(user_id,no,amount,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                String Status = ((TransferMoneyFinalModel) serviceResponse).getStatus();
                String message = ((TransferMoneyFinalModel) serviceResponse).getMessage();
                try {
                    if (Status.equals("200")) {

                        AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
//                        startActivity(new Intent(PayActivityForMoney.this,SuccessActivity.class));

                    } else {
                        AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
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
                        PrintUtil.showToast(PayActivityForMoney.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayActivityForMoney.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {

        switch (resultCode){
            case 1:
                startActivity(new Intent(PayActivityForMoney.this,SuccessActivity.class));
                break;
            case 2:
                finish();
                break;
        }

    }
}

