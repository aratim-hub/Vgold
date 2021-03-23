package com.cognifygroup.vgold;

import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cognifygroup.vgold.Adapter.GoldBookingHistoryAdapter;
import com.cognifygroup.vgold.Adapter.MoneyTransactionAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyServiceProvider;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
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

public class MoneyWalletActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.txtRupees)
    TextView txtRupees;
    @InjectView(R.id.btnAddMoneyToWallet)
    Button btnAddMoneyToWallet;
    @InjectView(R.id.recyclerViewMoneyWallet)
    RecyclerView recyclerViewMoneyWallet;
    Dialog dialog;
    AlertDialogs mAlert;
    GetAllTransactionMoneyServiceProvider getAllTransactionMoneyServiceProvider;

    private String GetAmount;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_wallet);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
          progressDialog = new TransparentProgressDialog(MoneyWalletActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        getAllTransactionMoneyServiceProvider=new GetAllTransactionMoneyServiceProvider(this);
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
                            AlertDialogs.alertDialogOk(MoneyWalletActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(MoneyWalletActivity.this, "Alert", message,
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
                        PrintUtil.showToast(MoneyWalletActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(MoneyWalletActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(MoneyWalletActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btnAddMoneyToWallet)
    public void AddMoney() {


        startActivity(new Intent(MoneyWalletActivity.this,AddMoneyActivity.class));


        /*dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_for_wallet_money);
        dialog.show();

        final EditText edtAmount = (EditText) dialog.findViewById(R.id.edtAmount);
        final Button textViewCancel = (Button) dialog.findViewById(R.id.textViewCancel);
        final Button textViewOk = (Button) dialog.findViewById(R.id.textViewOk);

        dialog.setCancelable(false);

        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAmount.getText().length() < 1) {
                    edtAmount.setError("Enter amount");
                } else {
                    GetAmount = edtAmount.getText().toString();
                    Intent intent=new Intent(MoneyWalletActivity.this,AddPaymentToMoneyWalletActivity.class);
                    intent.putExtra("AMOUNT",GetAmount);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        AttemptToGetMoneyTransactionHistory(VGoldApp.onGetUerId());
    }

    private void AttemptToGetMoneyTransactionHistory(String user_id) {
        progressDialog.show();
        getAllTransactionMoneyServiceProvider.getAllTransactionMoneyHistory(user_id,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GetAllTransactionMoneyModel) serviceResponse).getStatus();
                    String message = ((GetAllTransactionMoneyModel) serviceResponse).getMessage();
                    String balance=((GetAllTransactionMoneyModel) serviceResponse).getWallet_Balance();
                    txtRupees.setText(balance);
                    ArrayList<GetAllTransactionMoneyModel.Data> mArrMoneyTransactonHistory=((GetAllTransactionMoneyModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        recyclerViewMoneyWallet.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
                        recyclerViewMoneyWallet.setAdapter(new MoneyTransactionAdapter(MoneyWalletActivity.this,mArrMoneyTransactonHistory));

                     //   mAlert.onShowToastNotification(MoneyWalletActivity.this, message);

                    } else {
                        AlertDialogs.alertDialogOk(MoneyWalletActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(MoneyWalletActivity.this, message);
                       // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
                        //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(MoneyWalletActivity.this,mArrGoldBookingHistory));

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
                        PrintUtil.showToast(MoneyWalletActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(MoneyWalletActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(MoneyWalletActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 11:
                Intent LogIntent = new Intent(MoneyWalletActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
