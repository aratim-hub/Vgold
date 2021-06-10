package com.cognifygroup.vgold.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CPUserDetailsActivity;
import com.cognifygroup.vgold.ChannelPartner.UserDetailsModel;
import com.cognifygroup.vgold.GoldBookingHistoryActivity;
import com.cognifygroup.vgold.GoldTransactionHistoryActivity;
import com.cognifygroup.vgold.LoginActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CPUserDetailsAdapter extends RecyclerView.Adapter<CPUserDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    String str = null;
    ArrayList<UserDetailsModel.Data> userDetailsArrayList;
    ArrayList<UserDetailsModel.Data> orgUserDetailsArrayList;
    private Filter customerFilter;

    public CPUserDetailsAdapter(Context Context,
                                ArrayList<UserDetailsModel.Data> userDetailsArrayList) {
        this.mContext = Context;
        this.activity = (Activity) Context;
        this.userDetailsArrayList = userDetailsArrayList;
        this.orgUserDetailsArrayList = userDetailsArrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_user_details_item_adapter, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        UserDetailsModel.Data dataList = userDetailsArrayList.get(position);

        holder.txtName.setText(dataList.getUname() + " " +dataList.getTotal_gold_in_account() +" gm");

        holder.detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LogIntent = new Intent(activity, CPUserDetailsActivity.class);
                LogIntent.putExtra("userDetailsModel", (Serializable) dataList);
                mContext.startActivity(LogIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userDetailsArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.txtName)
        TextView txtName;

        @InjectView(R.id.detailLayout)
        LinearLayout detailLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public void resetData() {
        userDetailsArrayList = orgUserDetailsArrayList;
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
                    results.values = orgUserDetailsArrayList;
                    results.count = orgUserDetailsArrayList.size();
                } else {
                    // We perform filtering operation
                    List<UserDetailsModel.Data> customerArrayList = new ArrayList<>();
                    for (UserDetailsModel.Data dataModel : userDetailsArrayList) {

                        if (dataModel.getUname().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getUmobile().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getUid().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getUemail().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
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
            userDetailsArrayList = (ArrayList<UserDetailsModel.Data>) results.values;
            notifyDataSetChanged();

//            no_tv.setText("(" + String.valueOf(results.count) + ")");
        }
    }
}