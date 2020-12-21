package com.cognifygroup.vgold;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cognifygroup.vgold.GoldBooking.GoldBookingModel;
import com.cognifygroup.vgold.GoldBooking.GoldBookingServiceProvider;
import com.cognifygroup.vgold.fragment.ItemOneFragment;
import com.cognifygroup.vgold.fragment.ItemThreeFragment;
import com.cognifygroup.vgold.fragment.ItemTwoFragment;
import com.cognifygroup.vgold.getMaturityWeight.MaturityWeightModel;
import com.cognifygroup.vgold.getMaturityWeight.MaturityWeightServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GoldBookingActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.spinner_goldWeight)
    Spinner spinner_goldWeight;
    @InjectView(R.id.spinner_tennure)
    Spinner spinner_tennure;
    @InjectView(R.id.edtPromoCode)
    EditText edtPromoCode;
    @InjectView(R.id.edtCustomGoldWeight)
    EditText edtCustomGoldWeight;
    @InjectView(R.id.llCustom)
    LinearLayout llCustom;
    @InjectView(R.id.btnNext)
    Button btnNext;
    int customweight = 0;
    String goldWeight1;
    String tennure;

    AlertDialogs mAlert;
    GoldBookingServiceProvider goldBookingServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_booking);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new TransparentProgressDialog(GoldBookingActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        goldBookingServiceProvider = new GoldBookingServiceProvider(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.GoldWeight_array1, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner_goldWeight.setAdapter(adapter);
        spinner_goldWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String goldWeight = (String) parent.getItemAtPosition(pos);
                if (goldWeight.equals("custom")) {
                    llCustom.setVisibility(View.VISIBLE);
                } else {
                    llCustom.setVisibility(View.GONE);
                    goldWeight1 = goldWeight;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.Tennure_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner_tennure.setAdapter(adapter1);
        spinner_tennure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                tennure = (String) parent.getItemAtPosition(pos);

                if (tennure.equals("12 month")) {
                    tennure = "12";
                } else if (tennure.equals("24 month")) {
                    tennure = "24";

                } else if (tennure.equals("36 month")) {
                    tennure = "36";

                } else if (tennure.equals("48 month")) {
                    tennure = "48";

                } else if (tennure.equals("60 month")) {
                    tennure = "60";

                } else {
                    tennure = "";
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


    @OnClick(R.id.btnNext)
    public void onClickOnBtnNext() {
        if (llCustom.getVisibility() == View.VISIBLE) {
            try {
                customweight = Integer.parseInt(edtCustomGoldWeight.getText().toString());
            } catch (Exception e) {
                customweight = 0;
            }

            if (customweight < 10) {
                edtCustomGoldWeight.setError("Gold Weight Greater than 10");
            } else {
                if (llCustom.getVisibility() == View.VISIBLE) {
                    goldWeight1 = edtCustomGoldWeight.getText().toString();
                }
                AttemptToGetGoldBooking(goldWeight1, tennure, edtPromoCode.getText().toString());
            }
        } else {
            AttemptToGetGoldBooking(goldWeight1, tennure, edtPromoCode.getText().toString());
        }


    }


    private void AttemptToGetGoldBooking(final String quantity, final String tennure, final String pc) {
        progressDialog.show();
        goldBookingServiceProvider.getAddBankDetails(quantity, tennure, pc, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((GoldBookingModel) serviceResponse).getStatus();
                    String message = ((GoldBookingModel) serviceResponse).getMessage();
                    String monthly = ((GoldBookingModel) serviceResponse).getMonthly();
                    String booking_value = ((GoldBookingModel) serviceResponse).getBooking_value();
                    String down_payment = ((GoldBookingModel) serviceResponse).getDown_payment();
                    String gold_rate = ((GoldBookingModel) serviceResponse).getGold_rate();
                    String booking_charge = ((GoldBookingModel) serviceResponse).getBookingCharge();

                    if (status.equals("200")) {

                        //  mAlert.onShowToastNotification(GoldBookingActivity.this, message);
                        Intent intent = new Intent(GoldBookingActivity.this, BookingDetailActivity.class);
                        intent.putExtra("monthly", monthly);
                        intent.putExtra("booking_value", booking_value);
                        intent.putExtra("down_payment", down_payment);
                        intent.putExtra("gold_rate", gold_rate);
                        intent.putExtra("booking_charge", booking_charge);
                        intent.putExtra("quantity", quantity);
                        intent.putExtra("tennure", tennure);
                        intent.putExtra("pc", pc);

                        startActivity(intent);
                    } else {
                        AlertDialogs.alertDialogOk(GoldBookingActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(GoldBookingActivity.this, message);
//                        Intent intent = new Intent(GoldBookingActivity.this, BookingDetailActivity.class);
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
                        PrintUtil.showToast(GoldBookingActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(GoldBookingActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(GoldBookingActivity.this);
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
                Intent intent = new Intent(GoldBookingActivity.this, BookingDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
