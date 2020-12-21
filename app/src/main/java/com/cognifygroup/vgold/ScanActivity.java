package com.cognifygroup.vgold;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.androidhive.barcode.BarcodeReader;

import static android.provider.Telephony.Carriers.NAME;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener, AlertDialogOkListener {

    BarcodeReader barcodeReader;
    @InjectView(R.id.txtMobile)
    TextView txtMobile;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @OnClick(R.id.txtMobile)
    public void onClickOfTxtMobile(){
        Intent intent=new Intent(ScanActivity.this,ContactListActivity.class);
        startActivity(intent);

        /*Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(intent, 1);*/

        //startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

    }




/*    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String mobile = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        Toast.makeText(ScanActivity.this,name+""+mobile,Toast.LENGTH_LONG).show();
                        // TODO Whatever you want to do with the selected contact name.
                    }
                }
                break;
        }
    }*/

    @Override
    public void onScanned(Barcode barcode) {

        // playing barcode reader beep sound
        barcodeReader.playBeep();

        // ticket details activity by passing barcode
        Intent intent = new Intent(ScanActivity.this, PayActivity.class);
        intent.putExtra("code", barcode.displayValue);
        intent.putExtra("whichactivity","2");
        startActivity(intent);
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
        AlertDialogs.alertDialogOk(ScanActivity.this, "Alert", "Error occurred while scanning " + s,
                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        finish();
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
