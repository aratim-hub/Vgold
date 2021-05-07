package com.cognifygroup.vgold.plan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.BuildConfig;
import com.cognifygroup.vgold.GoldWalletActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldModel;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseActivity;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.FontStyle;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlanActivity extends AppCompatActivity implements AlertDialogOkListener {

    private TransparentProgressDialog progressDialog;
    private PlanServiceProvider planServiceProvider;
    private AlertDialogOkListener alertDialogOkListener = this;

    @InjectView(R.id.edtgoldWeight)
    EditText edtgoldWeight;

    @InjectView(R.id.btnViewPlan)
    Button btnViewPlan;

    @InjectView(R.id.lblTotAmt)
    TextView lblTotAmt;
    @InjectView(R.id.txtTotAmt)
    TextView txtTotAmt;

    @InjectView(R.id.lblBookAmt)
    TextView lblBookAmt;
    @InjectView(R.id.txtBookAmt)
    TextView txtBookAmt;

    @InjectView(R.id.lblBookChr)
    TextView lblBookChr;
    @InjectView(R.id.txtBookChr)
    TextView txtBookChr;

    @InjectView(R.id.lblPaidAmt)
    TextView lblPaidAmt;
    @InjectView(R.id.lblEMI)
    TextView lblEMI;

    @InjectView(R.id.lbl12Emi)
    TextView lbl12Emi;
    @InjectView(R.id.txt12Emi)
    TextView txt12Emi;

    @InjectView(R.id.lbl24Emi)
    TextView lbl24Emi;
    @InjectView(R.id.txt24Emi)
    TextView txt24Emi;

    @InjectView(R.id.lbl36Emi)
    TextView lbl36Emi;
    @InjectView(R.id.txt36Emi)
    TextView txt36Emi;

    @InjectView(R.id.lbl48Emi)
    TextView lbl48Emi;
    @InjectView(R.id.txt48Emi)
    TextView txt48Emi;

    @InjectView(R.id.lbl60Emi)
    TextView lbl60Emi;
    @InjectView(R.id.txt60Emi)
    TextView txt60Emi;
    @InjectView(R.id.palnLayout)
    LinearLayout palnLayout;

    @InjectView(R.id.shareFab)
    FloatingActionButton shareFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new TransparentProgressDialog(PlanActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        FontStyle.FontStyle(PlanActivity.this);

        planServiceProvider = new PlanServiceProvider(this);

        lblTotAmt.setTypeface(FontStyle.getFonSteagislerRegular());
        txtTotAmt.setTypeface(FontStyle.getFonSteagislerRegular());
        lblBookAmt.setTypeface(FontStyle.getFonSteagislerRegular());
        txtBookAmt.setTypeface(FontStyle.getFonSteagislerRegular());
        lblBookChr.setTypeface(FontStyle.getFonSteagislerRegular());
        txtBookChr.setTypeface(FontStyle.getFonSteagislerRegular());
        lblPaidAmt.setTypeface(FontStyle.getFonSteagislerRegular());

        lblEMI.setTypeface(FontStyle.getFonSteagislerRegular());
        lbl12Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        txt12Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        lbl24Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        txt24Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        lbl36Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        txt36Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        lbl48Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        txt48Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        lbl60Emi.setTypeface(FontStyle.getFonSteagislerRegular());
        txt60Emi.setTypeface(FontStyle.getFonSteagislerRegular());


        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String weight = edtgoldWeight.getText().toString();
                File filePath = new File(Environment.getExternalStorageDirectory()
                        .getPath() + "/" + "VGOLD" + "/plans_" + weight + ".pdf");
//                        .getPath() + "/" + "VGOLD" + "/plans.pdf");

                if (filePath.exists()) {

                    Uri uri = FileProvider.getUriForFile(PlanActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider", filePath);

                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.setPackage("com.whatsapp");

                    startActivity(share);
                }
            }
        });
    }

    @OnClick(R.id.btnViewPlan)
    public void onClickOfbtnViewPlan() {
        String weight = edtgoldWeight.getText().toString();
        if (weight != null && !TextUtils.isEmpty(weight)) {
            getPlanDetails(weight);
        }
    }

    private void getPlanDetails(String quantity) {
        progressDialog.show();
        planServiceProvider.getPlanDetails(quantity, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    progressDialog.hide();
                    String status = ((PlanModel) serviceResponse).getStatus();
                    String message = ((PlanModel) serviceResponse).getMessage();

                    PlanModel.Data model = ((PlanModel) serviceResponse).getData();
//                    String data = ((PlanModel) serviceResponse).getData();

                    if (status.equals("200")) {

                        palnLayout.setVisibility(View.VISIBLE);

                        txtTotAmt.setText(getResources().getString(R.string.rs) + model.getTot_amt() + "/-");
                        txtBookAmt.setText(getResources().getString(R.string.rs) + model.getBooking_amt() + "/-");
                        txtBookChr.setText(getResources().getString(R.string.rs) + model.getBooking_chrg() + "/-");
                        lblPaidAmt.setText("You Pay " + getResources().getString(R.string.rs) + model.getPayable_amt() + "/-");

                        if (model.getRemain_amt() != null && !TextUtils.isEmpty(model.getRemain_amt())) {
                            double remainAmt = Double.valueOf(model.getRemain_amt());

                            txt12Emi.setText(getResources().getString(R.string.rs) + String.valueOf(Math.round(remainAmt / 12)) + "/-");
                            txt24Emi.setText(getResources().getString(R.string.rs) + String.valueOf(Math.round(remainAmt / 24)) + "/-");
                            txt36Emi.setText(getResources().getString(R.string.rs) + String.valueOf(Math.round(remainAmt / 36)) + "/-");
                            txt48Emi.setText(getResources().getString(R.string.rs) + String.valueOf(Math.round(remainAmt / 48)) + "/-");
                            txt60Emi.setText(getResources().getString(R.string.rs) + String.valueOf(Math.round(remainAmt / 60)) + "/-");

                        }

                        downloadPDFFile(model.getPdf_url());

                    } else {
                        palnLayout.setVisibility(View.GONE);
                        AlertDialogs.alertDialogOk(PlanActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.hide();
                } finally {
                    progressDialog.hide();
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {

                try {
                    progressDialog.hide();
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(PlanActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PlanActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PlanActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void downloadPDFFile(String pdf_url) {
        new DownloadFileFromURL(pdf_url).execute();
    }

    private class DownloadFileFromURL extends AsyncTask<Void, Void, Void> {

        String downloadUrl;
        Context context;
        String version;
        File apkStorage = null;
        File outputFile = null;

        public DownloadFileFromURL(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL NWUrl = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) NWUrl.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("TAG", "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());
                }

                apkStorage = new File(Environment.getExternalStorageDirectory()
                        .getPath() + "/" + "VGOLD");
                if (!apkStorage.exists()) {
                    apkStorage.mkdirs();
                }
//                outputFile = new File(apkStorage, "plans.pdf");

                String weight = edtgoldWeight.getText().toString();
                outputFile = new File(apkStorage, "/plans_" + weight + ".pdf");

                if (outputFile.exists()) {
                    outputFile.delete();
                    outputFile.createNewFile();
                    Log.e("TAG", "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }
                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {
                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e("TAG", "Download Error Exception " + e.getMessage());
            }

            return null;
        }

        @SuppressLint("RestrictedApi")
        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    shareFab.setVisibility(View.VISIBLE);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 3000);
                    Log.e("TAG", "Download Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 3000);
                Log.e("TAG", "Download Failed with Exception - " + e.getLocalizedMessage());

            }
            super.onPostExecute(result);
        }
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