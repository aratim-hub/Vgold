package com.cognifygroup.vgold.CPModule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cognifygroup.vgold.Adapter.CPUserCommissionDetailsAdapter;
import com.cognifygroup.vgold.Adapter.CPUserGoldDetailsAdapter;
import com.cognifygroup.vgold.ChannelPartner.UserCommissionDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserGoldDetailsModel;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.utils.APICallback;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;
import com.cognifygroup.vgold.utils.BaseServiceResponseModel;
import com.cognifygroup.vgold.utils.PrintUtil;
import com.cognifygroup.vgold.utils.TransparentProgressDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class CPCommissionFragment extends Fragment implements AlertDialogOkListener {

    private Activity activity;
    private TransparentProgressDialog progressDialog;
    private CPServiceProvider mCPUserServiceProvider;
    private AlertDialogOkListener alertDialogOkListener = this;
    private String uid;
    private RecyclerView recyclerCommissionDetails;
    private TextView noData;
    private CPUserCommissionDetailsAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public CPCommissionFragment(String uid) {
        this.uid = uid;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cp_commission_details, container, false);
        ButterKnife.inject(activity);

        initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new TransparentProgressDialog(activity);
        progressDialog.setCancelable(false);
        activity.setFinishOnTouchOutside(false);

        mCPUserServiceProvider = new CPServiceProvider(activity);

        AppCompatEditText etCommissionSearch = view.findViewById(R.id.etCommissionSearch);
        noData = (TextView) view.findViewById(R.id.noData);
        recyclerCommissionDetails = (RecyclerView) view.findViewById(R.id.recyclerCommissionDetails);

        recyclerCommissionDetails.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerCommissionDetails.setLayoutManager(mLayoutManager);
        recyclerCommissionDetails.setItemAnimator(new DefaultItemAnimator());


        etCommissionSearch.addTextChangedListener(new TextWatcher() {
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

        getUserCommissionDetails();
    }

    private void getUserCommissionDetails() {
        progressDialog.show();
        mCPUserServiceProvider.getUserCommissionDetails(uid, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {

                try {
                    String Status = ((UserCommissionDetailsModel) serviceResponse).getStatus();
                    String message = ((UserCommissionDetailsModel) serviceResponse).getMessage();
                    ArrayList<UserCommissionDetailsModel.Data> userGoldCommssionDetailsArrayList = ((UserCommissionDetailsModel) serviceResponse).getData();

                    if (Status.equals("200")) {
                        if (userGoldCommssionDetailsArrayList != null && userGoldCommssionDetailsArrayList.size() > 0) {
                            noData.setVisibility(View.GONE);
                            mAdapter = new CPUserCommissionDetailsAdapter(activity, userGoldCommssionDetailsArrayList, uid);
                            recyclerCommissionDetails.setAdapter(mAdapter);
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