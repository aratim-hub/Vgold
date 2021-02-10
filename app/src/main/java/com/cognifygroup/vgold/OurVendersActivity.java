package com.cognifygroup.vgold;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cognifygroup.vgold.Adapter.VendorOfferAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel;
import com.cognifygroup.vgold.getVendorOffer.VendorOfferServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class OurVendersActivity extends AppCompatActivity implements AlertDialogOkListener {

    @InjectView(R.id.rc_vendorOffer)
    RecyclerView rc_vendorOffer;
    @InjectView(R.id.txtError)
    TextView txtError;


    AlertDialogs mAlert;
    private VendorOfferAdapter vendorOfferAdapter;
    private VendorOfferServiceProvider vendorOfferServiceProvider;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_venders);
        ButterKnife.inject(this);

        init();
    }

    private void init() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new TransparentProgressDialog(OurVendersActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        vendorOfferServiceProvider = new VendorOfferServiceProvider(this);


        AttemptToGetVendorOffer();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void AttemptToGetVendorOffer() {
        progressDialog.show();
        vendorOfferServiceProvider.getGoldBookingHistory(new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((VendorOfferModel) serviceResponse).getStatus();
                    String message = ((VendorOfferModel) serviceResponse).getMessage();
                    ArrayList<VendorOfferModel.Data> mArrSaloonServices = ((VendorOfferModel) serviceResponse).getData();

                    if (status.equals("200")) {

                        txtError.setVisibility(View.GONE);
                        rc_vendorOffer.setVisibility(View.VISIBLE);
                        vendorOfferAdapter = new VendorOfferAdapter(OurVendersActivity.this, mArrSaloonServices, "venders");
//                        rc_vendorOffer.setLayoutManager(new LinearLayoutManager(OurVendersActivity.this, LinearLayoutManager.VERTICAL, false));
                        rc_vendorOffer.setLayoutManager(new GridLayoutManager(OurVendersActivity.this, 5));
                        rc_vendorOffer.setAdapter(vendorOfferAdapter);

                    } else {

                        txtError.setVisibility(View.VISIBLE);
                        rc_vendorOffer.setVisibility(View.GONE);

                        AlertDialogs.alertDialogOk(OurVendersActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(OurVendersActivity.this, message);
                       /* Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
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
                        PrintUtil.showToast(OurVendersActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(OurVendersActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(OurVendersActivity.this);
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


    @Override
    public void onDialogOk(int resultCode) {

    }
}
