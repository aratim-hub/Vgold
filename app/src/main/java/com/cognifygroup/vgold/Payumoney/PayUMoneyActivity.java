package com.cognifygroup.vgold.Payumoney;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.LoginActivity;
import com.cognifygroup.vgold.MainActivity;
import com.cognifygroup.vgold.PayActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.addGold.AddGoldModel;
import com.cognifygroup.vgold.addGold.AddGoldServiceProvider;
import com.cognifygroup.vgold.addMoney.AddMoneyModel;
import com.cognifygroup.vgold.addMoney.AddMoneyServiceProvider;
import com.cognifygroup.vgold.payInstallment.PayInstallmentModel;
import com.cognifygroup.vgold.payInstallment.PayInstallmentServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class PayUMoneyActivity extends AppCompatActivity implements AlertDialogOkListener {

    //    AddWalletMoneyServiceProvider addWalletMoneyServiceProvider;
    @InjectView(R.id.webview_payment)
    WebView webView;

    /*String merchant_key = "0dCMZ3lW";
    String salt = "VMeDRpi6aH";*/

    String merchant_key = "SzsTlQ";
    String salt = "Qc3n63C6";

    String action1 = "";
    String base_url = "https://secure.payu.in";
    // String base_url = "https://test.payu.in";
    //int error = 0;
    // String hashString = "";
    // Map<String, String> params;
    String txnid;
    String amount = "";
    String otherAmount = "";
    String productInfo = "";
    String firstName = "";
    String emailId = "";

    private String SUCCESS_URL = "https://www.payumoney.com/payments/guestcheckout/#/success";
    private String FAILED_URL = "https://www.PayUmoney.com/mobileapp/PayUmoney/failure.php";
    private String phone = "";
    private String serviceProvider = "payu_paisa";
    private String hash = "";

    private Handler mHandler = new Handler();
    private AlertDialogs mAlert;
    private String mBookingId;

    AddMoneyServiceProvider addMoneyServiceProvider;
    AddGoldServiceProvider addGoldServiceProvider;
    PayInstallmentServiceProvider payInstallmentServiceProvider;
    String whichActivity = "";
    String goldWeight = "";
    String bookingId = "";
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;


    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        // getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        progressDialog = new TransparentProgressDialog(PayUMoneyActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();
        progressDialog.show();

        addMoneyServiceProvider = new AddMoneyServiceProvider(this);
        addGoldServiceProvider = new AddGoldServiceProvider(this);
        payInstallmentServiceProvider = new PayInstallmentServiceProvider(this);
        Intent intent = getIntent();
        if (getIntent() != null && getIntent().hasExtra("AMOUNT")) {
            amount = intent.getStringExtra("AMOUNT");
            otherAmount = intent.getStringExtra("OTHERAMOUNT");
            whichActivity = intent.getStringExtra("whichActivity");
            // Toast.makeText(PayUMoneyActivity.this,whichActivity,Toast.LENGTH_LONG).show();
            goldWeight = intent.getStringExtra("goldweight");
            bookingId = intent.getStringExtra("bookingId");
        }

        emailId = "" + VGoldApp.onGetEmail();
        phone = "" + VGoldApp.onGetNo();
        //amount = MiniXeroxApp.onGetPdfCost();
        // mBookingId = MiniXeroxApp.onGetPdfId();
       /* Bundle lBundle = getIntent().getExtras();
        amount = lBundle.getString("AMOUNT");
        mBookingId = lBundle.getString("BOOKINGID");*/
        // phone = MedoApplication.onGetUser();
        //emailId=MedoApplication.onGetEmail();
        txnid = "TXN" + System.currentTimeMillis();
        JSONObject productInfoObj = new JSONObject();
        JSONArray productPartsArr = new JSONArray();
        JSONObject productPartsObj1 = new JSONObject();
        JSONObject paymentIdenfierParent = new JSONObject();
        JSONArray paymentIdentifiersArr = new JSONArray();
        JSONObject paymentPartsObj1 = new JSONObject();
        JSONObject paymentPartsObj2 = new JSONObject();
        try {
            // Payment Parts
            productPartsObj1.put("name", "abc");
            productPartsObj1.put("description", "abcd");
            productPartsObj1.put("value", "1000");
            productPartsObj1.put("isRequired", "true");
            productPartsObj1.put("settlementEvent", "EmailConfirmation");
            productPartsArr.put(productPartsObj1);
            productInfoObj.put("paymentParts", productPartsArr);

            // Payment Identifiers
            paymentPartsObj1.put("field", "CompletionDate");
            paymentPartsObj1.put("value", "31/10/2012");
            paymentIdentifiersArr.put(paymentPartsObj1);

            paymentPartsObj2.put("field", "TxnId");
            paymentPartsObj2.put("value", txnid);
            paymentIdentifiersArr.put(paymentPartsObj2);

            paymentIdenfierParent.put("paymentIdentifiers",
                    paymentIdentifiersArr);
            productInfoObj.put("", paymentIdenfierParent);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        productInfo = productInfoObj.toString();

//        Log.e("TAG", productInfoObj.toString());

        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt())
                + (System.currentTimeMillis() / 1000L);
        txnid = hashCal("SHA-256", rndm).substring(0, 20);

        hash = hashCal("SHA-512", merchant_key + "|" + txnid + "|" + amount
                + "|" + productInfo + "|" + firstName + "|" + emailId
                + "|||||||||||" + salt);

        action1 = base_url.concat("/_payment");

        webView.setWebViewClient(new WebViewClient() {
            private int webViewPreviousState;
            private final int PAGE_STARTED = 0x1;
            private final int PAGE_REDIRECTED = 0x2;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                webViewPreviousState = PAGE_REDIRECTED;
                webView.loadUrl(urlNewString);

              /*  if (urlNewString.startsWith(SUCCESS_URL)) {
                    Log.e("Url", "" + urlNewString);
                    AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"Success");
                   // AlertDialogs.getInstance().onShowToastNotification(PayUMoneyActivity.this, "Payment Successful!");
                    // Toast.makeText(PayUMoneyActivity.this, "Payment Successful " + urlNewString, Toast.LENGTH_LONG).show();
                  //  RequestModel lRequest = new RequestModel();
                   // lRequest.setAPImethod("payment_status_of_booking?booking_id=" + mBookingId + "&&payment_status=Success");
                   // new JsonRequestClass(PayUMoneyActivity.this).onJsonObjReq(PayUMoneyActivity.this, lRequest);
                    Intent lIntent=new Intent(PayUMoneyActivity.this,MainActivity.class);
                   // lIntent.putExtras(getIntent().getExtras());
                    startActivity(lIntent);
                    finish();
                } else if (urlNewString.startsWith(FAILED_URL)) {
                    AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"failure");
                    AlertDialogs.getInstance().onShowToastNotification(PayUMoneyActivity.this, "Payment failure!");

                }*/
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewPreviousState = PAGE_STARTED;


            }

            @Override
            public void onPageFinished(WebView view, String url) {

                int index = url.lastIndexOf('/');
                url = url.substring(0, index);
//                Log.e("URL", url);
                if (webViewPreviousState == PAGE_STARTED) {
                    progressDialog.hide();
                }
                if (url.equals(SUCCESS_URL)) {
                    // AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"Success");
                    if (whichActivity.equals("money")) {
                        AttemptToAddMoney(VGoldApp.onGetUerId(), amount, "Payumoney", "", txnid, "");
                    } else if (whichActivity.equals("gold")) {
                        AttemptToAddGold(VGoldApp.onGetUerId(), goldWeight, "" + amount, "Payumoney", "", txnid, "");
                    } else if (whichActivity.equals("installment")) {
                        AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                "" + amount, "Payumoney",
                                "", txnid, otherAmount, "");

                    } else {
                        AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", "Something went wrong",
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, "Something went wrong");
                    }

                } else if (url.equals(FAILED_URL)) {
                    //  AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"failure");
                    AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", "Payment failure!",
                            getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                    AlertDialogs.getInstance().onShowToastNotification(PayUMoneyActivity.this, "Payment failure!");
                   /* Intent lIntent=new Intent(PayUMoneyActivity.this,MainActivity.class);
                    startActivity(lIntent);
                    finish();*/
                }

            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.addJavascriptInterface(new PayUJavaScriptInterface(this), "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchant_key);
        mapParams.put("hash", hash);
        mapParams.put("txnid", txnid);
        mapParams.put("service_provider", "payu_paisa");
        mapParams.put("amount", amount);
        mapParams.put("firstname", firstName);
        mapParams.put("email", emailId);
        mapParams.put("phone", phone);

        mapParams.put("productinfo", productInfo);
        // mapParams.put("surl", SUCCESS_URL);
        mapParams.put("furl", FAILED_URL);

        mapParams.put("udf1", "");
        mapParams.put("udf2", "");

        mapParams.put("udf3", "");
        mapParams.put("udf4", "");
        mapParams.put("udf5", "");

        webview_ClientPost(webView, action1, mapParams.entrySet());


    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 1:
                Intent lIntent = new Intent(PayUMoneyActivity.this, MainActivity.class);
                startActivity(lIntent);
                finish();
                break;

        }

    }
/*
    @Override
    public void onGetResponse(String res) {
        ResponseModel lResponse = new ResponseModel();
        lResponse = new Gson().fromJson(res, lResponse.getClass());

        if (lResponse.status == 200) {
          //  MedoApplication.isGetAPI = true;
           // RequestModel lRequest = new RequestModel();
            //lRequest.setAPImethod("sendhttp.php?authkey=126333Ad7Wu8GR9jt57e76845&mobiles="+phone+"&sender=CONFRM&route=4&country=0&message=Your Booking Successfully");
            ///new JsonRequestClass((ResponseListner) PayUMoneyActivity.this).onJsonObjReq(PayUMoneyActivity.this, lRequest);


        } else {

            AlertDialogs.getInstance().onShowToastNotification(this, lResponse.message);
        }
    }*/

    public class PayUJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {

                public void run() {
                    mHandler = null;
                    AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", "Payment Successfull.",
                            getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//                    mAlert.onShowToastNotification(PayUMoneyActivity.this, "Payment Successfully.");
                }
            });
        }
    }

    public void webview_ClientPost(WebView webView, String url,
                                   Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>",
                url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format(
                    "<input name='%s' type='hidden' value='%s' />",
                    item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
//        Log.d("Test", "webview_ClientPost called");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public boolean empty(String s) {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }


    private void AttemptToAddMoney(String user_id, String amount, String payment_option, String bank_details, String tr_id, String cheque_no) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        addMoneyServiceProvider.getAddBankDetails(user_id, amount, payment_option, bank_details, tr_id, cheque_no, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((AddMoneyModel) serviceResponse).getStatus();
                    String message = ((AddMoneyModel) serviceResponse).getMessage();

                    if (status.equals("200")) {

                        AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", "Payment Successfull.",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                          mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent=new Intent(PayUMoneyActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();
                    } else {

                        AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", "Payment Successfull.",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent=new Intent(PayUMoneyActivity.this,MainActivity.class);
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
                        PrintUtil.showToast(PayUMoneyActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    private void AttemptToAddGold(String user_id, String gold, String amount, String payment_option, String bank_details, String tr_id, String cheque_no) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        addGoldServiceProvider.getAddBankDetails(user_id, gold, amount, payment_option,
                bank_details, tr_id, cheque_no, new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        try {
                            String status = ((AddGoldModel) serviceResponse).getStatus();
                            String message = ((AddGoldModel) serviceResponse).getMessage();

                            if (status.equals("200")) {

                                AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                            } else {

                                AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
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
                                PrintUtil.showToast(PayUMoneyActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                            } else {
                                PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                        } finally {
                            progressDialog.hide();
                        }
                    }
                });
    }


    private void AttemptToPayInstallment(String user_id, String gbid, String amountr,
                                         String payment_option, String bank_details,
                                         String tr_id, String otherAmount, String cheque_no) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        payInstallmentServiceProvider.payInstallment(user_id, gbid, amountr, payment_option,
                bank_details, tr_id, otherAmount, cheque_no, "0",new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        try {
                            String status = ((PayInstallmentModel) serviceResponse).getStatus();
                            String message = ((PayInstallmentModel) serviceResponse).getMessage();

                            if (status.equals("200")) {

                                AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                            } else {
                                AlertDialogs.alertDialogOk(PayUMoneyActivity.this, "Alert", message,
                                        getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
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
                                PrintUtil.showToast(PayUMoneyActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                            } else {
                                PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                        } finally {
                            progressDialog.hide();
                        }
                    }
                });
    }

  /*  private void addMoneyToWalletApi(String pay_bye,String rupees,String transaction_id,String user_id) {

        mAlert.onShowProgressDialog(PayUMoneyActivity.this, true);
        addWalletMoneyServiceProvider.addWalletMoney(pay_bye,rupees,transaction_id,user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    int Status = ((AddWalletMoneyModel) serviceResponse).getStatus();

                    String message = ((AddWalletMoneyModel) serviceResponse).getMessage();
                    String balance = String.valueOf(((AddWalletMoneyModel) serviceResponse).getBalance());
                    if (Status == 200) {
                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
                        startActivity(new Intent(PayUMoneyActivity.this,MainActivity.class));
                        finish();

                    } else {
                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mAlert.onShowProgressDialog(PayUMoneyActivity.this, false);
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {
                try {

                    if (apiErrorModel != null) {
                        PrintUtil.showToast(PayUMoneyActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                } finally {
                    mAlert.onShowProgressDialog(PayUMoneyActivity.this, false);
                }
            }
        });
    }*/

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(PayUMoneyActivity.this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}