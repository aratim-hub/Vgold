package com.cognifygroup.vgold;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.cognifygroup.vgold.AddComplain.AddComplainModel;
import com.cognifygroup.vgold.AddComplain.AddComplainServiceProvider;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CheckLoginStatus.LoginSessionModel;
import com.cognifygroup.vgold.CheckLoginStatus.LoginStatusServiceProvider;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity implements AlertDialogOkListener {
    @InjectView(R.id.edt_comment)
    EditText edt_comment;
    @InjectView(R.id.btnSubmitFeedback)
    Button btnSubmitFeedback;
    @InjectView(R.id.ratingBar)
    AppCompatRatingBar ratingBar;

    AddComplainServiceProvider addComplainServiceProvider;
    AlertDialogs mAlert;
    private TransparentProgressDialog progressDialog;
    private AlertDialogOkListener alertDialogOkListener = this;
    private LoginStatusServiceProvider loginStatusServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(FeedbackActivity.this);

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

        progressDialog = new TransparentProgressDialog(FeedbackActivity.this);
        progressDialog.setCancelable(false);
        setFinishOnTouchOutside(false);

        mAlert = AlertDialogs.getInstance();

        addComplainServiceProvider = new AddComplainServiceProvider(this);


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
                            AlertDialogs.alertDialogOk(FeedbackActivity.this, "Alert", message + ",  Please relogin to app",
                                    getResources().getString(R.string.btn_ok), 11, false, alertDialogOkListener);
                        }

                    } else {
                        AlertDialogs.alertDialogOk(FeedbackActivity.this, "Alert", message,
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
                        PrintUtil.showToast(FeedbackActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(FeedbackActivity.this);
                    }
                } catch (Exception e) {
                    progressDialog.hide();
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(FeedbackActivity.this);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }


    @OnClick(R.id.btnSubmitFeedback)
    public void onClickOfbtnSubmitReview() {
        Log.d("value is ", String.valueOf(ratingBar.getRating()));

        AttemptToAddFeedback(VGoldApp.onGetUerId(), edt_comment.getText().toString(),
                String.valueOf(ratingBar.getRating()));
    }


    private void AttemptToAddFeedback(String user_id, String comment, String rating) {
        progressDialog.show();
        addComplainServiceProvider.addFeedback(user_id, comment, rating, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    String status = ((AddComplainModel) serviceResponse).getStatus();
                    String message = ((AddComplainModel) serviceResponse).getMessage();

                    if (status.equals("200")) {
                        AlertDialogs.alertDialogOk(FeedbackActivity.this, "Alert", "Thank You for the valuable feedback",
                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
                    } else {
                        AlertDialogs.alertDialogOk(FeedbackActivity.this, "Alert", message,
                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
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
                        PrintUtil.showToast(FeedbackActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(FeedbackActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(FeedbackActivity.this);
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
                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case 11:
                Intent LogIntent = new Intent(FeedbackActivity.this, LoginActivity.class);
                startActivity(LogIntent);
                finish();
                break;
        }
    }
}
