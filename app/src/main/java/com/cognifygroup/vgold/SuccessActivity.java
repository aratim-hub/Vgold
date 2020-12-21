package com.cognifygroup.vgold;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SuccessActivity extends AppCompatActivity {

    @InjectView(R.id.ImageView)
    ImageView ImageView;
    @InjectView(R.id.txtb)
    TextView txtb;
    @InjectView(R.id.btnOk)
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.inject(this);


           Glide.with(this).load("https://thumbs.gfycat.com/CourteousPhonyHorsemouse-size_restricted.gif").into(ImageView);

           if (getIntent().hasExtra("message")){
               String message=getIntent().getStringExtra("message");
               txtb.setText(message);
               txtb.setTextSize(12);

           }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);


    }

    @OnClick(R.id.btnOk)
    public void onClickOfBtnok(){

        Intent intent=new Intent(SuccessActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
