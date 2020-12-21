package com.cognifygroup.vgold;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ContactListActivityForMoney extends AppCompatActivity {
    private static final String TAG = ContactListActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    @InjectView(R.id.imgContact1)
    ImageView imgContact1;
    @InjectView(R.id.edtMobileNumber)
    EditText edtMobileNumber;
    @InjectView(R.id.btnProceed)
    Button btnProceed;

    String contactNumber1="";
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_for_money);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick(R.id.imgContact1)
    public void onImgContactClick(){
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }


    @OnClick(R.id.btnProceed)
    public void onbtnProceedClick(){
        Intent intent=new Intent(ContactListActivityForMoney.this,PayActivityForMoney.class);
        intent.putExtra("mobileno",edtMobileNumber.getText().toString());
        intent.putExtra("whichactivity","3");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
//            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            String contactName= retrieveContactName();
            String contactNumber=retrieveContactNumber();

            Intent intent=new Intent(ContactListActivityForMoney.this,PayActivityForMoney.class);
            intent.putExtra("name",contactName);
            if (flag){
                intent.putExtra("number",contactNumber1);
            }else {
                intent.putExtra("number",contactNumber);
            }
            intent.putExtra("whichactivity","1");
            startActivity(intent);

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

//        Log.d(TAG, "Contact ID: " + contactID);

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

      //  String get_Mo = contactNumber.substring(contactNumber.lastIndexOf(' ')+1);

        contactNumber=contactNumber.replaceAll("\\s+","");
        if(contactNumber.startsWith("+"))
        {
            contactNumber1=contactNumber.substring(3);
            //contactNumber = contactNumber.replace("91", "");
            flag=true;
            return contactNumber1;
        }else {
            flag=false;
            return contactNumber;
        }


    }

    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        // Toast.makeText(ContactListActivity.this,contactName,Toast.LENGTH_LONG).show();
//        Log.d(TAG, "Contact Name: " + contactName);
       String no1= phoeNumberWithOutCountryCode(""+contactName);
        return no1;

    }

    public String phoeNumberWithOutCountryCode(String phoneNumberWithCountryCode) {
        Pattern complie = Pattern.compile(" ");
        String[] phonenUmber = complie.split(phoneNumberWithCountryCode);
//        Log.e("number is", phonenUmber[1]);
        return phonenUmber[1];
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
