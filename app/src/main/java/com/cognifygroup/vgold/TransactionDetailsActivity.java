package com.cognifygroup.vgold;


import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TransactionDetailsActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.txtTxnInstallmentprice)
    TextView txtTxnInstallmentprice;
    @InjectView(R.id.txtTxnRemainingAmt)
    TextView txtTxnRemainingAmt;
   /* @InjectView(R.id.txtTxnPeriod)
    TextView txtTxnPeriod;*/
    @InjectView(R.id.txtTxnId)
    TextView txtTxnId;
    @InjectView(R.id.txtTxnBankDetails)
    TextView txtTxnBankDetails;
    @InjectView(R.id.txtTxnpaymentMethod)
    TextView txtTxnpaymentMethod;
    /*@InjectView(R.id.txtTxnchequeNo)
    TextView txtTxnchequeNo;*/
  /*  @InjectView(R.id.txtTxnAdminStatus)
    TextView txtTxnAdminStatus;*/
    @InjectView(R.id.imgGoldTransactionDetails)
    Button imgGoldTransactionDetails;
    @InjectView(R.id.txtReciptNo)
    TextView txtReciptNo;
    @InjectView(R.id.txtDate)
    TextView txtDate;
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.txtCrn)
    TextView txtCrn;
    @InjectView(R.id.txtGoldAccount)
    TextView txtGoldAccount;
    @InjectView(R.id.txtNextDueDate)
    TextView txtNextDueDate;

    String recipt_no;

    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    private TransparentProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
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
    public void init(){

        if(getIntent().hasExtra("installment")){
            Intent intent=getIntent();
            String installment=intent.getStringExtra("installment");
            String remainingamount=intent.getStringExtra("remainingamt");
            String peroid=intent.getStringExtra("period");
            String txnId=intent.getStringExtra("txnid");
            String bankdetail=intent.getStringExtra("bankdetail");
            String paymentMethod=intent.getStringExtra("paymentmethod");
            String chequeno=intent.getStringExtra("chequeno");
            String adminstatus=intent.getStringExtra("adminstatus");
            recipt_no=intent.getStringExtra("recipt_no");
            String date=intent.getStringExtra("date");
            String gold_id=intent.getStringExtra("gold_id");
            String next_due_date=intent.getStringExtra("next_due_date");

            txtReciptNo.setText("Reciept "+recipt_no);
            txtDate.setText(date);
            txtName.setText(VGoldApp.onGetFirst()+" "+VGoldApp.onGetLast());
            txtCrn.setText(VGoldApp.onGetUerId());
            txtTxnInstallmentprice.setText(installment);
            txtTxnRemainingAmt.setText(remainingamount);
           // txtTxnPeriod.setText(peroid);
            txtTxnId.setText(txnId);
            txtTxnBankDetails.setText(bankdetail);
            txtTxnpaymentMethod.setText(paymentMethod);
            txtGoldAccount.setText(gold_id);
            txtNextDueDate.setText(next_due_date);
           // txtTxnchequeNo.setText(chequeno);
           // txtTxnAdminStatus.setText(adminstatus);
        }
        progressDialog = new TransparentProgressDialog(TransactionDetailsActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        loginStatusServiceProvider = new LoginStatusServiceProvider(this);
        checkLoginSession();
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
                            AlertDialogs.alertDialogOk(TransactionDetailsActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(TransactionDetailsActivity.this, "Alert", message,
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
                        PrintUtil.showToast(TransactionDetailsActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(TransactionDetailsActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(TransactionDetailsActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @OnClick(R.id.imgGoldTransactionDetails)
    public void onClickOfImgTrDetails(){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vgold.co.in/dashboard/user/module/goldbooking/mmd_receipt.php?id="+recipt_no+"&&user_id="+ VGoldApp.onGetUerId()));
        startActivity(browserIntent);
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 11:
                Intent LogIntent = new Intent(TransactionDetailsActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
