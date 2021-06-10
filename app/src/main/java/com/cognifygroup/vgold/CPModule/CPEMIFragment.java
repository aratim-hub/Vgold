package com.cognifygroup.vgold.CPModule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cognifygroup.vgold.Adapter.CPEMIStatusDetailsAdapter;
import com.cognifygroup.vgold.Adapter.CPUserCommissionDetailsAdapter;
import com.cognifygroup.vgold.Adapter.CPUserEMIDetailsAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CPUserDetailsActivity;
import com.cognifygroup.vgold.ChannelPartner.UserCommissionDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIStatusDetailsModel;
import com.cognifygroup.vgold.PayInstallmentActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.payInstallment.PayInstallmentModel;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CPEMIFragment extends Fragment implements AlertDialogOkListener, CPUserEMIDetailsAdapter.EMIDetailsListener {

    private Activity activity;

    private TransparentProgressDialog progressDialog;
    private CPServiceProvider mCPUserServiceProvider;
    private AlertDialogOkListener alertDialogOkListener = this;
    private String uid;
    private RecyclerView recyclerEMIDetails;
    private TextView noData;
    private CPUserEMIDetailsAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public CPEMIFragment(String uid) {
        this.uid = uid;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cp_emi_details, container, false);
        ButterKnife.inject(activity);

        initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new TransparentProgressDialog(activity);
        progressDialog.setCancelable(false);
        activity.setFinishOnTouchOutside(false);

        mCPUserServiceProvider = new CPServiceProvider(activity);

        AppCompatEditText etEMISearch = view.findViewById(R.id.etEMISearch);
        noData = (TextView) view.findViewById(R.id.noData);
        recyclerEMIDetails = (RecyclerView) view.findViewById(R.id.recyclerEMIDetails);
        recyclerEMIDetails.setLayoutManager(new LinearLayoutManager(activity));

        etEMISearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    if (mAdapter != null) {
                        mAdapter.resetData();
                    }
                }
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        getUserEMIDetails();
    }

    private void getUserEMIDetails() {
        progressDialog.show();
        mCPUserServiceProvider.getUserEMIDetails(uid, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {

                try {
                    String Status = ((UserEMIDetailsModel) serviceResponse).getStatus();
                    String message = ((UserEMIDetailsModel) serviceResponse).getMessage();
                    ArrayList<UserEMIDetailsModel.Data> userGoldCommssionDetailsArrayList = ((UserEMIDetailsModel) serviceResponse).getData();

                    if (Status.equals("200")) {
                        if (userGoldCommssionDetailsArrayList != null && userGoldCommssionDetailsArrayList.size() > 0) {
                            noData.setVisibility(View.GONE);
                            setListData(userGoldCommssionDetailsArrayList);
                        } else {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } else {
                        AlertDialogs.alertDialogOk(activity, "Alert", message,
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
                        PrintUtil.showToast(activity, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(activity);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    private void setListData(ArrayList<UserEMIDetailsModel.Data> userGoldCommssionDetailsArrayList) {
         mAdapter = new CPUserEMIDetailsAdapter(activity, userGoldCommssionDetailsArrayList, this);
        recyclerEMIDetails.setAdapter(mAdapter);
    }

    private void EMIDetailsDialog(ArrayList<UserEMIStatusDetailsModel.Data> statusList) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cp_emi_details_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayoutCompat closeLayout = dialog.findViewById(R.id.closeLayout);
        RecyclerView rvEMIStatusDetails = dialog.findViewById(R.id.rvEMIStatusDetails);
        rvEMIStatusDetails.setLayoutManager(new LinearLayoutManager(activity));

        if (statusList != null && statusList.size() > 0) {
            CPEMIStatusDetailsAdapter adapter = new CPEMIStatusDetailsAdapter(activity, statusList);
            rvEMIStatusDetails.setAdapter(adapter);
        }

        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onItemClick(UserEMIDetailsModel.Data model) {
        if (model != null) {
            getEMIStatusDetails(model.getGold_booking_id());
        }
    }

    private void getEMIStatusDetails(String BookingId) {
        progressDialog.show();
        mCPUserServiceProvider.getUserEMIStatusDetails(BookingId, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {

                try {
                    String Status = ((UserEMIStatusDetailsModel) serviceResponse).getStatus();
                    String message = ((UserEMIStatusDetailsModel) serviceResponse).getMessage();
                    ArrayList<UserEMIStatusDetailsModel.Data> userEMIStatusDetailsArrayList = ((UserEMIStatusDetailsModel) serviceResponse).getData();

                    if (Status.equals("200")) {
                        if (userEMIStatusDetailsArrayList != null && userEMIStatusDetailsArrayList.size() > 0) {
                            EMIDetailsDialog(userEMIStatusDetailsArrayList);
                        }
                    } else {
                        AlertDialogs.alertDialogOk(activity, "Alert", message,
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
                        PrintUtil.showToast(activity, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(activity);
                } finally {
                    progressDialog.hide();
                }
            }
        });
    }

    @Override
    public void onDialogOk(int resultCode) {

    }
}