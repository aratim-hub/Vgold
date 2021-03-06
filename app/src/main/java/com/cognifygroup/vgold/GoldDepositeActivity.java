
package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.AddBank.AddBankServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.getBankDetails.GetBankModel;
import com.cognifygroup.vgold.getMaturityWeight.MaturityWeightModel;
import com.cognifygroup.vgold.getMaturityWeight.MaturityWeightServiceProvider;
import com.cognifygroup.vgold.goldDepositeRequest.DepositeRequestModel;
import com.cognifygroup.vgold.goldDepositeRequest.DepositeRequestServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.cognifygroup.vgold.vendorForDeeposite.VendorForDepositeModel;
import com.cognifygroup.vgold.vendorForDeeposite.VendorForDepositeServiceProvider;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GoldDepositeActivity extends AppCompatActivity implements AlertDialogOkListener {


    @InjectView(R.id.edtgoldWeight)
    EditText edtgoldWeight;
    @InjectView(R.id.spinner_tennure_deposite)
    Spinner spinner_tennure_deposite;
    @InjectView(R.id.txtMaturityWeight)
    TextView txtMaturityWeight;
    @InjectView(R.id.spinner_willingToDeposite)
    Spinner spinner_willingToDeposite;
    @InjectView(R.id.btnSendDepositeRequest)
    Button btnSendDepositeRequest;
    @InjectView(R.id.edtRemark)
    EditText edtRemark;
    @InjectView(R.id.edtPurity)
    EditText edtPurity;

    String tennure, bankGurantee, vendor_id, firmName;
    private String msg;

    AlertDialogs mAlert;
    MaturityWeightServiceProvider maturityWeightServiceProvider;
    VendorForDepositeServiceProvider vendorForDepositeServiceProvider;
    DepositeRequestServiceProvider depositeRequestServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    public Timer timer = new Timer();
    public final long DELAY = 1000; // milliseconds
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_deposite);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        edtgoldWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    // TODO: do what you need here (refresh list)
                                    // you will probably need to use runOnUiThread(Runnable action) for some specific actions (e.g. manipulating views)
                                    AttemptToGetMaturityWeight(s.toString(), tennure, "yes");

                                }
                            },
                            DELAY
                    );

                }
            }
        });

    }

    public void init() {

        progressDialog = new TransparentProgressDialog(GoldDepositeActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        maturityWeightServiceProvider = new MaturityWeightServiceProvider(this);
        vendorForDepositeServiceProvider = new VendorForDepositeServiceProvider(this);
        depositeRequestServiceProvider = new DepositeRequestServiceProvider(this);

        AttemptToGetVendorForDeposite();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Tennure_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
// Apply the adapter to the spinner
        spinner_tennure_deposite.setAdapter(adapter);
        spinner_tennure_deposite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String paymentoption = (String) parent.getItemAtPosition(pos);
                if (paymentoption.equals("12 month")) {
                    tennure = "12";
                } else if (paymentoption.equals("24 month")) {
                    tennure = "24";

                } else if (paymentoption.equals("36 month")) {
                    tennure = "36";

                } else if (paymentoption.equals("48 month")) {
                    tennure = "48";

                } else if (paymentoption.equals("60 month")) {
                    tennure = "60";

                } else {
                    tennure = "";
                }

                AttemptToGetMaturityWeight(edtgoldWeight.getText().toString(), tennure, "yes");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                            AlertDialogs.alertDialogOk(GoldDepositeActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(GoldDepositeActivity.this, "Alert", message,
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
                        PrintUtil.showToast(GoldDepositeActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
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

    @OnClick(R.id.btnSendDepositeRequest)
    public void onClickOfBtnSendRequest() {
        AttemptToGetDepositeRequest(VGoldApp.onGetUerId(),
                edtgoldWeight.getText().toString(), tennure,
                txtMaturityWeight.getText().toString(), vendor_id,
                edtPurity.getText().toString().trim(), edtRemark.getText().toString().trim(),
                "yes");
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_withBank:
                if (checked)

                    bankGurantee = "yes";
                // Pirates are the best
                AttemptToGetMaturityWeight(edtgoldWeight.getText().toString(), tennure, bankGurantee);
                break;
            case R.id.radio_withoutBank:
                if (checked)
                    bankGurantee = "no";
                AttemptToGetMaturityWeight(edtgoldWeight.getText().toString(), tennure, bankGurantee);
                // Ninjas rule
                break;
        }

    }


    private void AttemptToGetVendorForDeposite() {
        // mAlert.onShowProgressDialog(SignUpActivity.this, true);
        vendorForDepositeServiceProvider.getVendorForDeposite(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((VendorForDepositeModel) serviceResponse).getStatus();
                    String message = ((VendorForDepositeModel) serviceResponse).getMessage();
                    final ArrayList<VendorForDepositeModel.Data> mArrCity = ((VendorForDepositeModel) serviceResponse).getData();


                    if (status.equals("200")) {
                        ArrayAdapter<VendorForDepositeModel.Data> adapter =
                                new ArrayAdapter<VendorForDepositeModel.Data>(GoldDepositeActivity.this, R.layout.support_simple_spinner_dropdown_item, mArrCity);
//                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
                        spinner_willingToDeposite.setAdapter(adapter);
                        spinner_willingToDeposite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                vendor_id = String.valueOf(mArrCity.get(position).getVendor_id());
                                firmName = String.valueOf(mArrCity.get(position).getFirm_name());

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {
                        AlertDialogs.alertDialogOk(GoldDepositeActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
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
                        PrintUtil.showToast(GoldDepositeActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });

    }

    private void AttemptToGetMaturityWeight(String gold_weight, String tennure, String guarantee) {
        //  progressDialog.show();
        maturityWeightServiceProvider.getMaturityWeight(gold_weight, tennure, guarantee, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((MaturityWeightModel) serviceResponse).getStatus();
                    String message = ((MaturityWeightModel) serviceResponse).getMessage();
                    String maturityWeight = ((MaturityWeightModel) serviceResponse).getData();
                    txtMaturityWeight.setText(maturityWeight);

                    if (status.equals("200")) {

                     /*   mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                        Intent intent=new Intent(GoldDepositeActivity.this,MainActivity.class);
                        startActivity(intent);*/
                    } else {
                        AlertDialogs.alertDialogOk(GoldDepositeActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                       /* Intent intent=new Intent(GoldDepositeActivity.this,MainActivity.class);
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
                        PrintUtil.showToast(GoldDepositeActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
                } finally {
                    //  progressDialog.hide();
                }
            }
        });
    }

    private void AttemptToGetDepositeRequest(String user_id, String gw, String tennure,
                                             String cmw, String vendor_id,
                                             String add_purity, String remark,
                                             String guarantee) {
        progressDialog.show();
        depositeRequestServiceProvider.getDepositeRequest(user_id, gw, tennure, cmw, vendor_id,
                add_purity, remark, guarantee, new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        try {
                            String status = ((DepositeRequestModel) serviceResponse).getStatus();
                            String message = ((DepositeRequestModel) serviceResponse).getMessage();

                            msg = message;

                            if (status.equals("200")) {

                                AlertDialogs.alertDialogOk(GoldDepositeActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

                       /* mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                        Intent intent=new Intent(GoldDepositeActivity.this,MainActivity.class);
                        startActivity(intent);*/
                            } else {

                                AlertDialogs.alertDialogOk(GoldDepositeActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 2, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                        /*Intent intent=new Intent(GoldDepositeActivity.this,SuccessActivity.class);
                        intent.putExtra("message",message);
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
                                PrintUtil.showToast(GoldDepositeActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                            } else {
                                PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.showNetworkAvailableToast(GoldDepositeActivity.this);
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
                Intent intent = new Intent(GoldDepositeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case 2:
                Intent failIntent = new Intent(GoldDepositeActivity.this, SuccessActivity.class);
                failIntent.putExtra("message", msg);
                startActivity(failIntent);
                break;
            case 11:
                Intent LogIntent = new Intent(GoldDepositeActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
