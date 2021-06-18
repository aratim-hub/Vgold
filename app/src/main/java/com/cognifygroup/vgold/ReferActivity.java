package com.cognifygroup.vgold;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cognifygroup.vgold.AddBank.AddBankModel;
import com.cognifygroup.vgold.AddBank.AddBankServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.getReferCode.ReferModel;
import com.cognifygroup.vgold.getReferCode.ReferServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ReferActivity extends AppCompatActivity implements AlertDialogOkListener {

    AlertDialogs mAlert;
    ReferServiceProvider referServiceProvider;

    @InjectView(R.id.edtNameR)
    EditText edtNameR;
    @InjectView(R.id.edtEmailR)
    EditText edtEmailR;
    @InjectView(R.id.edtMobileR)
    EditText edtMobileR;
    @InjectView(R.id.btnSubmitR)
    Button btnSubmitR;
    @InjectView(R.id.imgContact)
    ImageView imgContact;

    private String contactID;

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    String contactNumber1 = "";
    boolean flag;

    String no;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
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

    private void init() {

        mAlert = AlertDialogs.getInstance();

        progressDialog = new TransparentProgressDialog(ReferActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);
        referServiceProvider = new ReferServiceProvider(this);

        if (getIntent().hasExtra("no")) {
            no = getIntent().getStringExtra("no");
        }

        edtMobileR.setText(no);

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
                            AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message,
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
                        PrintUtil.showToast(ReferActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @OnClick(R.id.btnSubmitR)
    public void onClickOfBtnRefer() {
        String email = edtEmailR.getText().toString();
        String name = edtNameR.getText().toString();
        String mobile = edtMobileR.getText().toString();
        if (!email.equals("") && email != null && !name.equals("") && name != null && !mobile.equals("") && mobile != null) {

            getReferCode(VGoldApp.onGetUerId());

        } else {
            AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", "All Data required",
                    getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
//            Toast.makeText(ReferActivity.this, "All Data required", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.imgContact)
    public void onClickContactBook() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
//            Log.d(TAG, "Response: " + data.toString());
//            uriContact = data.getData();

            Cursor cursor = null;
            Cursor cursorMobile = null;
            String contactNumber = "";
            String email = "", name = "", mobile = "";
            try {
                uriContact = data.getData();

                contactNumber = retrieveContactNumber();
                edtMobileR.setText(contactNumber);

                // get the contact id from the Uri
                String id = uriContact.getLastPathSegment();

                // query for everything email
                cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{id}, null);

                int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                // let's just get the first email
                if (cursor.moveToFirst()) {
                    email = cursor.getString(emailIdx);
                    name = cursor.getString(nameId);

                    edtEmailR.setText(email);
                    edtNameR.setText(name);
                    Log.e("TAG", "Got email: " + email);
                } else {
                    String contactName = retrieveContactName();
                    edtNameR.setText(contactName);
                    Log.e("TAG", "No results");
                }
            } catch (Exception e) {
                Log.e("TAG", "Failed to get email data", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }


//            String contactName = retrieveContactName();
//                 contactNumber = retrieveContactNumber(cursor);
//            String contactEmail = retrieveContactEmail();
//
//            edtMobileR.setText(contactNumber);
//            edtNameR.setText(contactName);
//            if (contactEmail != null) {
//                edtEmailR.setText(contactEmail);
//            }
        }
    }

    private String retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        // Toast.makeText(ContactListActivity.this,contactNumber,Toast.LENGTH_LONG).show();

//        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        contactNumber = contactNumber.replaceAll("\\s+", "");
        if (contactNumber.startsWith("+")) {
            contactNumber1 = contactNumber.substring(3);
            flag = true;
            //contactNumber = contactNumber.replace("91", "");
            return contactNumber1;

        } else {
            flag = false;
            return contactNumber;
        }
    }

    private String retrieveContactName() {
        String contactName = null;
        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return contactName;
    }

    private void getReferCode(String user_id) {
        progressDialog.show();
        referServiceProvider.getReferenceCode(user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    progressDialog.hide();
                    String status = ((ReferModel) serviceResponse).getStatus();
                    String message = ((ReferModel) serviceResponse).getMessage();
                    String data = ((ReferModel) serviceResponse).getData();

                    if (status.equals("200")) {
                        if (data != null && !TextUtils.isEmpty(data)) {
                            createDynamicLink(data);
                        }
                    } else {
//                        mAlert.onShowToastNotification(ReferActivity.this, message);
                        AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message,
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
                        PrintUtil.showToast(ReferActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void createDynamicLink(String data) {

        String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "&referrer=" + data;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://vgold.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.cognifygroup.vgold")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.cognifygroup.vgold")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        String mInvitationUrl = String.valueOf(shortDynamicLink.getShortLink());

                        if (mInvitationUrl != null && !TextUtils.isEmpty(mInvitationUrl)) {

//                            Log.d("TAG", mInvitationUrl);

                            AttemptToRefer(VGoldApp.onGetUerId(), edtNameR.getText().toString(), edtEmailR.getText().toString(),
                                    edtMobileR.getText().toString(), mInvitationUrl);

                        }

                    }
                });
    }

    private void AttemptToRefer(String user_id, String name, String email, String mobile_no, String refLink) {
        progressDialog.show();
        referServiceProvider.getAddBankDetails(user_id, name, email, mobile_no, refLink, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((ReferModel) serviceResponse).getStatus();
                    String message = ((ReferModel) serviceResponse).getMessage();

                    if (status.equals("200")) {


                        AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ReferActivity.this, message);
                       /* Intent intent = new Intent(ReferActivity.this, MainActivity.class);
                        startActivity(intent);*/
                    } else {
                        AlertDialogs.alertDialogOk(ReferActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ReferActivity.this, message);
//                        Intent intent = new Intent(ReferActivity.this, MainActivity.class);
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
                        PrintUtil.showToast(ReferActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ReferActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {
        switch (resultCode) {
            case 1:
                Intent intent = new Intent(ReferActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case 11:
                Intent LogIntent = new Intent(ReferActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
