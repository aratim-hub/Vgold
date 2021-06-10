package com.cognifygroup.vgold.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
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

public class CPUserEMIDetailsAdapter extends RecyclerView.Adapter<CPUserEMIDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    String str = null;
    ArrayList<UserEMIDetailsModel.Data> userEmiDetailsArrayList;
    ArrayList<UserEMIDetailsModel.Data> orgUserEmiDetailsArrayList;
    private EMIDetailsListener listener;
    private Filter customerFilter;

    public CPUserEMIDetailsAdapter(Context Context,
                                   ArrayList<UserEMIDetailsModel.Data> userEmiDetailsArrayList,
                                   EMIDetailsListener listener) {
        this.mContext = Context;
        this.activity = (Activity) Context;
        this.userEmiDetailsArrayList = userEmiDetailsArrayList;
        this.orgUserEmiDetailsArrayList = userEmiDetailsArrayList;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_user_emi_details_adapter, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        UserEMIDetailsModel.Data dataList = userEmiDetailsArrayList.get(position);


        if (dataList.getLast_paid_date() != null && !TextUtils.isEmpty(dataList.getLast_paid_date())) {
            holder.txtDate.setText(dataList.getLast_paid_date());
            holder.lastPaidLayout.setVisibility(View.VISIBLE);
        } else {
            holder.lastPaidLayout.setVisibility(View.GONE);
        }

        holder.txtGoldBookingId.setText(dataList.getGold_booking_id());
        holder.txtGoldQty.setText(dataList.getGold() + " gm");
        holder.txtDownPayment.setText(mContext.getResources().getString(R.string.rs) + dataList.getDown_payment());
        holder.txtGoldBookingValue.setText(mContext.getResources().getString(R.string.rs) + dataList.getBooking_amount());
        holder.txtInstallment.setText(mContext.getResources().getString(R.string.rs) + dataList.getMonthly_installment());
        holder.txtTenure.setText(dataList.getTennure());
        holder.txtBookingCharge.setText(mContext.getResources().getString(R.string.rs) + dataList.getBooking_charge());

        holder.detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(dataList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userEmiDetailsArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.txtDate)
        TextView txtDate;

        @InjectView(R.id.txtGoldBookingId)
        TextView txtGoldBookingId;

        @InjectView(R.id.txtGoldQty)
        TextView txtGoldQty;

        @InjectView(R.id.txtDownPayment)
        TextView txtDownPayment;

        @InjectView(R.id.txtGoldBookingValue)
        TextView txtGoldBookingValue;

        @InjectView(R.id.txtInstallment)
        TextView txtInstallment;

        @InjectView(R.id.txtTenure)
        TextView txtTenure;

        @InjectView(R.id.txtBookingCharge)
        TextView txtBookingCharge;

        @InjectView(R.id.detailLayout)
        LinearLayout detailLayout;

        @InjectView(R.id.lastPaidLayout)
        LinearLayout lastPaidLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface EMIDetailsListener {
        void onItemClick(UserEMIDetailsModel.Data model);
    }


    public void resetData() {
        userEmiDetailsArrayList = orgUserEmiDetailsArrayList;
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
                    results.values = orgUserEmiDetailsArrayList;
                    results.count = orgUserEmiDetailsArrayList.size();
                } else {
                    // We perform filtering operation
                    List<UserEMIDetailsModel.Data> customerArrayList = new ArrayList<>();
                    for (UserEMIDetailsModel.Data dataModel : userEmiDetailsArrayList) {

                        if (dataModel.getGold_booking_id().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getGold().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getMonthly_installment().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
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
            userEmiDetailsArrayList = (ArrayList<UserEMIDetailsModel.Data>) results.values;
            notifyDataSetChanged();

        }
    }
}