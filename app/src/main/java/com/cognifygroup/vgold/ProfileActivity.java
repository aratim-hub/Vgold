package com.cognifygroup.vgold;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
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
        txtAddress.setText(VGoldApp.onGetAddress());

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
    }

    @OnClick(R.id.btnUpdateProfile)
    public void onClickOfBtnUpdateProfile(){

        startActivity(new Intent(ProfileActivity.this,UpdateProfileActivity.class));
    }
}
