package com.cognifygroup.vgold;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.AddBank.AddBankService;
import com.cognifygroup.vgold.AddBank.AddBankServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.register.RegModel;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddBankActivity extends AppCompatActivity implements AlertDialogOkListener {

    AlertDialogs mAlert;
    AddBankServiceProvider addBankServiceProvider;

    @InjectView(R.id.edtBankName)
    EditText edtBankName;
    @InjectView(R.id.edtBranch)
    EditText edtBranch;
    @InjectView(R.id.edtAcc_no)
    EditText edtAcc_no;
    @InjectView(R.id.edtAcc_holder_name)
    EditText edtAcc_holder_name;
    @InjectView(R.id.edtAcc_type)
    EditText edtAcc_type;
    @InjectView(R.id.edtIfsc)
    EditText edtIfsc;
    @InjectView(R.id.btnSubmit)
    Button btnSubmit;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
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

    private void init(){
        mAlert = AlertDialogs.getInstance();

        progressDialog = new TransparentProgressDialog(AddBankActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        addBankServiceProvider=new AddBankServiceProvider(this);
    }

    @OnClick(R.id.btnSubmit)
    public void onClickOfBtnSubmit(){
        String acc_holder_name=edtAcc_holder_name.getText().toString();
        String acc_no=edtAcc_no.getText().toString();
        String acc_type=edtAcc_type.getText().toString();
        String bank_name=edtBankName.getText().toString();
        String branch=edtBranch.getText().toString();
        String ifsc=edtIfsc.getText().toString();

        if (acc_holder_name.length() == 0 && acc_no.length() == 0 && acc_type.length() == 0 && bank_name.length() == 0 && branch.length() == 0 && ifsc.length() == 0) {

            AlertDialogs.alertDialogOk(AddBankActivity.this, "Alert", "All data required",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//            mAlert.onShowToastNotification(AddBankActivity.this, "All data required");




        } else if (acc_holder_name.length() == 0) {
            edtAcc_holder_name.setError("Enter Account Holder Name");
        } else if (acc_no.length() == 0) {
            edtAcc_no.setError("Enter Account Number");
        } else if (acc_type.length() == 0) {
            edtAcc_type.setError("Enter Account Type");
        } else if (bank_name.length() < 3) {
            edtBankName.setError("Enter Bank Name");
        } else if (branch.length() == 0) {
            edtBranch.setError("Enter Branch Name");
        } else if (ifsc.length() == 0) {
            edtIfsc.setError("Enter Ifsc Number");
        } else {
            AttemptToAddBankDetails(VGoldApp.onGetUerId(),acc_holder_name,bank_name,acc_no,ifsc,acc_type,branch);
        }
    }


    private void AttemptToAddBankDetails(String user_id, String name, String bank_name, String acc_no, String ifsc, String acc_type, String branch) {
        progressDialog.show();
        addBankServiceProvider.getAddBankDetails(user_id,name,bank_name,acc_no,ifsc,acc_type,branch, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((AddBankModel) serviceResponse).getStatus();
                    String message = ((AddBankModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                      //  mAlert.onShowToastNotification(AddBankActivity.this, message);
                        Intent intent=new Intent(AddBankActivity.this,SuccessActivity.class);
                        intent.putExtra("message",message);
                        startActivity(intent);
                    } else {

                        AlertDialogs.alertDialogOk(AddBankActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(AddBankActivity.this, message);

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
                        PrintUtil.showToast(AddBankActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(AddBankActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(AddBankActivity.this);
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
                Intent intent=new Intent(AddBankActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

    }
}
