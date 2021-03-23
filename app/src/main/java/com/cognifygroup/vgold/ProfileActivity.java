package com.cognifygroup.vgold;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.txtPanNumber)
    TextView txtPanNumber;
    @InjectView(R.id.txtCRN)
    TextView txtCRN;
    @InjectView(R.id.txtMail)
    TextView txtMail;
    @InjectView(R.id.txtPhone)
    TextView txtPhone;
    @InjectView(R.id.txtAddress)
    TextView txtAddress;
    @InjectView(R.id.imgBarcode)
    ImageView imgBarcode;
    @InjectView(R.id.btnUpdateProfile)
    Button btnUpdateProfile;
    private LoginStatusServiceProvider loginStatusServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        progressDialog = new TransparentProgressDialog(ProfileActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        txtName.setText(VGoldApp.onGetFirst()+" "+VGoldApp.onGetLast());
        txtCRN.setText(VGoldApp.onGetUerId());
        String pan=VGoldApp.onGetPanNo();
        if (pan.equals("") || pan.equals(null)){
            pan="0000000000";
        }
        String screte_pan=pan.substring(pan.length()-4,pan.length());

        txtPanNumber.setText("XXXXXX"+screte_pan);
        txtMail.setText(VGoldApp.onGetEmail());
        txtPhone.setText(VGoldApp.onGetNo());
        txtAddress.setText(VGoldApp.onGetAddress()+", " + VGoldApp.onGetCity()
        + ", " + VGoldApp.onGetState());

        Picasso.with(this)
                .load(VGoldApp.onGetQrCode())
                //.placeholder(R.drawable.images)
                .resize(400, 400)
                .into(imgBarcode, new Callback() {
                    @Override
                    public void onSuccess() {
                        /*if (holder.progressbar_category !=null){
                            holder.progressbar_category.setVisibility(View.GONE);
                        }*/
                    }

                    @Override
                    public void onError() {

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
                            AlertDialogs.alertDialogOk(ProfileActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(ProfileActivity.this, "Alert", message,
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
                        PrintUtil.showToast(ProfileActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ProfileActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ProfileActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @OnClick(R.id.btnUpdateProfile)
    public void onClickOfBtnUpdateProfile(){

        startActivity(new Intent(ProfileActivity.this,UpdateProfileActivity.class));
    }
    @Override
    public void onDialogOk(int resultCode) {

        switch (resultCode){
            case 11:
                Intent LogIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }

    }
}
