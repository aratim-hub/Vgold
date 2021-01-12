package com.cognifygroup.vgold;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.cognifygroup.vgold.AddComplain.AddComplainModel;
import com.cognifygroup.vgold.AddComplain.AddComplainServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edt_comment)
    EditText edt_comment;
    @InjectView(R.id.btnSubmitReview)
    Button btnSubmitReview;
    @InjectView(R.id.ratingBar)
    AppCompatRatingBar ratingBar;

    AddComplainServiceProvider addComplainServiceProvider;
    AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
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

        progressDialog = new TransparentProgressDialog(ReviewActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        addComplainServiceProvider = new AddComplainServiceProvider(this);


    }

    @OnClick(R.id.btnSubmitReview)
    public void onClickOfbtnSubmitReview() {
        AttemptToAddReiew(VGoldApp.onGetUerId(), edt_comment.getText().toString());
    }


    private void AttemptToAddReiew(String user_id, String comment) {
        progressDialog.show();
        addComplainServiceProvider.addReview(user_id, comment, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((AddComplainModel) serviceResponse).getStatus();
                    String message = ((AddComplainModel) serviceResponse).getMessage();

                    if (status.equals("200")) {


                        AlertDialogs.alertDialogOk(ReviewActivity.this, "Alert", "Thank You for the valuable review",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ComplainActivity.this, message);
//                        Intent intent=new Intent(ComplainActivity.this,MainActivity.class);
//                        startActivity(intent);
                    } else {

                        AlertDialogs.alertDialogOk(ReviewActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

//                        mAlert.onShowToastNotification(ComplainActivity.this, message);
//                        Intent intent=new Intent(ComplainActivity.this,MainActivity.class);
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
                        PrintUtil.showToast(ReviewActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(ReviewActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(ReviewActivity.this);
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
                Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
