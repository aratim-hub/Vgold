package com.cognifygroup.vgold;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class AddPaymentToGoldWalletActivity extends AppCompatActivity {

    @InjectView(R.id.btnPay)
    Button btnPay;


    String goldWeight;
    double amount;

    String mPaymentFlag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_to_gold_wallet);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
        Intent intent=getIntent();
          goldWeight=intent.getStringExtra("AMOUNT");
          amount= Double.parseDouble(goldWeight)*3250;

          btnPay.setText("Pay "+"\u20B9 "+amount);
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
}
