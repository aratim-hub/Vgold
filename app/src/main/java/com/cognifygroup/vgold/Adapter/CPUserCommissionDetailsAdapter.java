package com.cognifygroup.vgold.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cognifygroup.vgold.ChannelPartner.UserCommissionDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserGoldDetailsModel;
import com.cognifygroup.vgold.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CPUserCommissionDetailsAdapter extends RecyclerView.Adapter<CPUserCommissionDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    String str = null;
    String uid;
    ArrayList<UserCommissionDetailsModel.Data> userCommissionDetailsArrayList;
    ArrayList<UserCommissionDetailsModel.Data> orgUserCommissionDetailsArrayList;
    private Filter customerFilter;

    public CPUserCommissionDetailsAdapter(Context Context,
                                          ArrayList<UserCommissionDetailsModel.Data> userCommissionDetailsArrayList,
                                          String uid) {
        this.mContext = Context;
        this.activity = (Activity) Context;
        this.userCommissionDetailsArrayList = userCommissionDetailsArrayList;
        this.orgUserCommissionDetailsArrayList = userCommissionDetailsArrayList;
        this.uid = uid;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_user_commission_details_adapter, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        UserCommissionDetailsModel.Data dataList = userCommissionDetailsArrayList.get(position);

        holder.txtDate.setText(dataList.getCreated_date());
        holder.txtGoldBookingId.setText(dataList.getBooking_id() + " - " + dataList.getGold() + " gm");
        holder.txtCommssion.setText(mContext.getResources().getString(R.string.rs) + dataList.getCommission_amount());

    }

    @Override
    public int getItemCount() {
        return userCommissionDetailsArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.txtDate)
        TextView txtDate;

        @InjectView(R.id.txtGoldBookingId)
        TextView txtGoldBookingId;

        @InjectView(R.id.txtCommssion)
        TextView txtCommssion;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public void resetData() {
        userCommissionDetailsArrayList = orgUserCommissionDetailsArrayList;
    }

    public Filter getFilter() {
        if (customerFilter == null) customerFilter = new CustomFilter();
        return customerFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            try {
                // We implement here the filter logic
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = orgUserCommissionDetailsArrayList;
                    results.count = orgUserCommissionDetailsArrayList.size();
                } else {
                    // We perform filtering operation
                    List<UserCommissionDetailsModel.Data> customerArrayList = new ArrayList<>();
                    for (UserCommissionDetailsModel.Data dataModel : userCommissionDetailsArrayList) {

                        if (dataModel.getBooking_id().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getGold().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getCreated_date().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                            customerArrayList.add(dataModel);
                        }
                    }
                    results.values = customerArrayList;
                    results.count = customerArrayList.size();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userCommissionDetailsArrayList = (ArrayList<UserCommissionDetailsModel.Data>) results.values;
            notifyDataSetChanged();

        }
    }
}