package com.cognifygroup.vgold;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.Loan.LoanModel;
import com.cognifygroup.vgold.Loan.LoanServiceProvider;
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

public class LoanActivity extends AppCompatActivity implements AlertDialogOkListener {


    @InjectView(R.id.loanEligibleAmt)
    TextView loanEligibleAmt;

    @InjectView(R.id.txtError)
    TextView txtError;

    @InjectView(R.id.btnApply)
    Button btnApply;
    @InjectView(R.id.edtPayableAmount)
    EditText edtPayableAmount;
    @InjectView(R.id.spinnerReason)
    Spinner spinnerReason;
    @InjectView(R.id.loanLayout)
    LinearLayoutCompat loanLayout;

    private String loanAmt;

    AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    private LoginStatusServiceProvider loginStatusServiceProvider;
    LoanServiceProvider getLoanServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        ButterKnife.inject(LoanActivity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new TransparentProgressDialog(LoanActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        getLoanServiceProvider = new LoanServiceProvider(this);
        loginStatusServiceProvider = new LoginStatusServiceProvider(this);

        checkLoginSession();
        AttemptToLoanDetails(VGoldApp.onGetUerId());

        /*intrestedId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llAmt.setVisibility(View.VISIBLE);
                    llRemark.setVisibility(View.VISIBLE);
                    btnApply.setVisibility(View.VISIBLE);
                } else {
                    llAmt.setVisibility(View.GONE);
                    llRemark.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                }
            }
        });*/
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
                            AlertDialogs.alertDialogOk(LoanActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(LoanActivity.this, "Alert", message,
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
                        PrintUtil.showToast(LoanActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoanActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(LoanActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToLoanDetails(String user_id) {
        progressDialog.show();
        getLoanServiceProvider.getLoanEligibility(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((LoanModel) serviceResponse).getStatus();
                    String message = ((LoanModel) serviceResponse).getMessage();
                    LoanModel.Data loanModel = ((LoanModel) serviceResponse).getData();

                    if (status.equalsIgnoreCase("200")) {
                        if (loanModel != null) {
                            loanAmt = loanModel.getLoan_amount();
                            loanEligibleAmt.setText("Congratulations " + VGoldApp.onGetFirst() + "!" +
                                    " You can apply for a loan of up to, "
                                    + getResources().getString(R.string.rs) + loanModel.getLoan_amount() + "/-");
                            txtError.setVisibility(View.GONE);
                            loanLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txtError.setVisibility(View.VISIBLE);
                        loanLayout.setVisibility(View.GONE);
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
                        PrintUtil.showToast(LoanActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoanActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(LoanActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void ApplyForLoan(String user_id, String amt, String comment) {
        progressDialog.show();
        getLoanServiceProvider.applyForLoan(user_id, amt, comment, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((LoanModel) serviceResponse).getStatus();
                    String message = ((LoanModel) serviceResponse).getMessage();

                    if (status.equalsIgnoreCase("200")) {
                        AlertDialogs.alertDialogOk(LoanActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
                    } else {
                        AlertDialogs.alertDialogOk(LoanActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(LoanActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoanActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(LoanActivity.this);
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

    @OnClick(R.id.btnApply)
    public void btnApply() {
        String amt = edtPayableAmount.getText().toString().trim();
        String remark = String.valueOf(spinnerReason.getSelectedItem());
        if (!TextUtils.isEmpty(amt) && Double.parseDouble(amt) > 0) {
            if (loanAmt != null && !TextUtils.isEmpty(loanAmt)) {
                if (Double.parseDouble(loanAmt) >= Double.parseDouble(amt)) {
                    ApplyForLoan(VGoldApp.onGetUerId(), amt, remark);
                } else {
                    AlertDialogs.alertDialogOk(LoanActivity.this, "Alert", "Amount should not more than eligible loan amount.",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
                }
            } else {
                AlertDialogs.alertDialogOk(LoanActivity.this, "Alert", "Something went wrong!! Try later.",
                        getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
            }
        } else {
            edtPayableAmount.setError("Required");
        }
    }


    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 11:
                Intent LogIntent = new Intent(LoanActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;

            case 1:
                Intent mainIntent = new Intent(LoanActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                break;
        }

    }
}
