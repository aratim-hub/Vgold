package com.cognifygroup.vgold;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;

public class ScanResultActivity extends AppCompatActivity implements AlertDialogOkListener {
    AlertDialogs mAlert;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        init();
    }

    public void init() {
        mAlert = AlertDialogs.getInstance();
        String barcode = getIntent().getStringExtra("code");

        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
//            mAlert.onShowToastNotification(ScanResultActivity.this, "Barcode is empty!");
            AlertDialogs.alertDialogOk(ScanResultActivity.this, "Alert", "Barcode is empty!",
                    getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
        }

        searchBarcode(barcode);
    }

    private void searchBarcode(String barcode) {

       /* if (!barcode.equals("") || !barcode.equals(null)) {
            Log.e("tag", barcode.toString());
        }*/


    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 1:
                finish();
                break;
        }
    }
}
