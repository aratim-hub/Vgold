package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Adapter.MoneyTransactionAdapter;
import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.WithdrawMoney.WithdrawMoneyModel;
import com.cognifygroup.vgold.WithdrawMoney.WithdrawMoneyServiceProvider;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyServiceProvider;
import com.cognifygroup.vgold.getBankDetails.GetBankModel;
import com.cognifygroup.vgold.getBankDetails.GetBankServiceProvider;
import com.cognifygroup.vgold.sellGold.SellGoldServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.gms.common.api.internal.TaskUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WithdrawActivity extends AppCompatActivity implements AlertDialogOkListener {

    AlertDialogs mAlert;
    GetBankServiceProvider getBankServiceProvider;
    WithdrawMoneyServiceProvider withdrawMoneyServiceProvider;
    GetAllTransactionMoneyServiceProvider getAllTransactionMoneyServiceProvider;

    @InjectView(R.id.spinnerBank)
    Spinner spinnerBank;
    @InjectView(R.id.edtWithdrawAmount)
    EditText edtWithdrawAmount;
    @InjectView(R.id.edtComment)
    EditText edtComment;
    @InjectView(R.id.btnWithdrawMoney)
    Button btnWithdrawMoney;
    @InjectView(R.id.txtWalletBalence)
    TextView txtWalletBalence;

    String BankId, BankName;
    String balance;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new TransparentProgressDialog(WithdrawActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        getBankServiceProvider = new GetBankServiceProvider(this);
        withdrawMoneyServiceProvider = new WithdrawMoneyServiceProvider(this);
        getAllTransactionMoneyServiceProvider = new GetAllTransactionMoneyServiceProvider(this);

        AttemptToGetMoneyTransactionHistory(VGoldApp.onGetUerId());
        AttemptToBank(VGoldApp.onGetUerId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnWithdrawMoney)
    public void onClickOfBtnWithdrawMoney() {

        double walletbalence = 0.0;
        if (balance != null && !TextUtils.isEmpty(balance)) {
            walletbalence = Double.parseDouble(balance);
        }

        double withdrawbalence = 0.0;
        if (edtWithdrawAmount.getText().toString() != null && !TextUtils.isEmpty(edtWithdrawAmount.getText().toString())) {
            withdrawbalence = Double.parseDouble(edtWithdrawAmount.getText().toString());
        }


//        double withdrawbalence = Double.parseDouble(edtWithdrawAmount.getText().toString());

        if (withdrawbalence <= walletbalence && withdrawbalence >= 500) {
            AttemptToWithdrawMoney(VGoldApp.onGetUerId(), BankId, edtWithdrawAmount.getText().toString(), edtComment.getText().toString());
        } else {
            AlertDialogs.alertDialogOk(WithdrawActivity.this, "Alert", "Amount should be greater than 500 or equal to or less than wallet balance",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            mAlert.onShowToastNotification(WithdrawActivity.this, "Amount should be greater than 500 or equal to or less than wallet balance");
        }
    }


    private void AttemptToBank(String user_id) {
        // mAlert.onShowProgressDialog(SignUpActivity.this, true);
        getBankServiceProvider.getAddBankDetails(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetBankModel) serviceResponse).getStatus();
                    String message = ((GetBankModel) serviceResponse).getMessage();
                    final ArrayList<GetBankModel.Data> mArrCity = ((GetBankModel) serviceResponse).getData();


                    if (status.equals("200")) {
                        ArrayAdapter<GetBankModel.Data> adapter =
                                new ArrayAdapter<GetBankModel.Data>(WithdrawActivity.this, R.layout.support_simple_spinner_dropdown_item, mArrCity);
                        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
                        spinnerBank.setAdapter(adapter);
                        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                BankId = String.valueOf(mArrCity.get(position).getBank_id());
                                BankName = String.valueOf(mArrCity.get(position).getBank_name());

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {
                        AlertDialogs.alertDialogOk(WithdrawActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(WithdrawActivity.this, message);
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
                        PrintUtil.showToast(WithdrawActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(WithdrawActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(WithdrawActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }

    private void AttemptToWithdrawMoney(String user_id, String bank_id, String amount, String comment) {
        progressDialog.show();
        withdrawMoneyServiceProvider.getAddBankDetails(user_id, bank_id, amount, comment, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((WithdrawMoneyModel) serviceResponse).getStatus();
                    String message = ((WithdrawMoneyModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        // mAlert.onShowToastNotification(WithdrawActivity.this, message);
                        Intent intent = new Intent(WithdrawActivity.this, SuccessActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                        finish();
                    } else {
                        AlertDialogs.alertDialogOk(WithdrawActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(WithdrawActivity.this, message);
//                        Intent intent=new Intent(WithdrawActivity.this,MainActivity.class);
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
                        PrintUtil.showToast(WithdrawActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(WithdrawActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(WithdrawActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToGetMoneyTransactionHistory(String user_id) {
        progressDialog.show();
        getAllTransactionMoneyServiceProvider.getAllTransactionMoneyHistory(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetAllTransactionMoneyModel) serviceResponse).getStatus();
                    String message = ((GetAllTransactionMoneyModel) serviceResponse).getMessage();
                    balance = ((GetAllTransactionMoneyModel) serviceResponse).getWallet_Balance();
                    txtWalletBalence.setText("\u20B9 " + balance);
                    ArrayList<GetAllTransactionMoneyModel.Data> mArrMoneyTransactonHistory = ((GetAllTransactionMoneyModel) serviceResponse).getData();

                    if (status.equals("200")) {

                        //       mAlert.onShowToastNotification(WithdrawActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(WithdrawActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(WithdrawActivity.this, message);
                        // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(WithdrawActivity.this));
                        //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(WithdrawActivity.this,mArrGoldBookingHistory));

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
                        PrintUtil.showToast(WithdrawActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(WithdrawActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(WithdrawActivity.this);
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
