package com.cognifygroup.vgold.CPModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cognifygroup.vgold.Adapter.CPUserDetailsAdapter;
import com.cognifygroup.vgold.Adapter.CPUserGoldDetailsAdapter;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.ChannelPartner.UserDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserGoldDetailsModel;
import com.cognifygroup.vgold.ChannelPartnerActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CPGoldBookingFragment extends Fragment implements AlertDialogOkListener {

    private Activity activity;

//    @InjectView(R.id.recyclerGoldDetails)
//    RecyclerView recyclerGoldDetails;

    private TransparentProgressDialog progressDialog;
    private CPServiceProvider mCPUserServiceProvider;
    private AlertDialogOkListener alertDialogOkListener = this;
    private String uid;
    private RecyclerView recyclerGoldDetails;
    private TextView noData;
    private CPUserGoldDetailsAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public CPGoldBookingFragment(String uid) {
        this.uid = uid;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cp_gold_details, container, false);
//        ButterKnife.inject(activity);

        initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new TransparentProgressDialog(activity);
        progressDialog.setCancelable(false);
        activity.setFinishOnTouchOutside(false);

        mCPUserServiceProvider = new CPServiceProvider(activity);

        AppCompatEditText etGoldSearch = view.findViewById(R.id.etGoldSearch);
        noData = (TextView) view.findViewById(R.id.noData);
        recyclerGoldDetails = (RecyclerView) view.findViewById(R.id.recyclerGoldDetails);
        recyclerGoldDetails.setLayoutManager(new LinearLayoutManager(activity));

        etGoldSearch.addTextChangedListener(new TextWatcher() {
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


        getUserGoldBookingDetails();
    }

    private void getUserGoldBookingDetails() {
        progressDialog.show();
        mCPUserServiceProvider.getUserGoldDetails(uid, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {

                try {
                    String Status = ((UserGoldDetailsModel) serviceResponse).getStatus();
                    String message = ((UserGoldDetailsModel) serviceResponse).getMessage();
                    ArrayList<UserGoldDetailsModel.Data> userGoldDetailsArrayList = ((UserGoldDetailsModel) serviceResponse).getData();


                    if (Status.equals("200")) {
                        if (userGoldDetailsArrayList != null && userGoldDetailsArrayList.size() > 0) {
                            noData.setVisibility(View.GONE);
                            recyclerGoldDetails.setLayoutManager(new LinearLayoutManager(activity));
                             mAdapter = new CPUserGoldDetailsAdapter(activity, userGoldDetailsArrayList, uid);
                            recyclerGoldDetails.setAdapter(mAdapter);
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

    @Override
    public void onDialogOk(int resultCode) {

    }
}