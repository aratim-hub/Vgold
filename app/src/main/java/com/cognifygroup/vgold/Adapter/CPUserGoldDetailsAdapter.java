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

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.CPUserDetailsActivity;
import com.cognifygroup.vgold.ChannelPartner.UserDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserGoldDetailsModel;
import com.cognifygroup.vgold.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CPUserGoldDetailsAdapter extends RecyclerView.Adapter<CPUserGoldDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    String str = null;
    String uid;
    ArrayList<UserGoldDetailsModel.Data> userGoldDetailsArrayList;
    ArrayList<UserGoldDetailsModel.Data> orgUserGoldDetailsArrayList;
    private Filter customerFilter;

    public CPUserGoldDetailsAdapter(Context Context,
                                    ArrayList<UserGoldDetailsModel.Data> userGoldDetailsArrayList,
                                    String uid) {
        this.mContext = Context;
        this.activity = (Activity) Context;
        this.userGoldDetailsArrayList = userGoldDetailsArrayList;
        this.orgUserGoldDetailsArrayList = userGoldDetailsArrayList;
        this.uid = uid;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_user_gold_details_adapter, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        UserGoldDetailsModel.Data dataList = userGoldDetailsArrayList.get(position);

        holder.txtDate.setText(dataList.getAdded_date());
        holder.txtGoldBookingId.setText(dataList.getGold_booking_id());
        holder.txtGoldQty.setText(dataList.getGold() + " gm");
        holder.txtRate.setText(mContext.getResources().getString(R.string.rs) + dataList.getRate());
        holder.txtGoldBookingValue.setText(mContext.getResources().getString(R.string.rs) + dataList.getBooking_amount());
        holder.txtBookingCharge.setText(mContext.getResources().getString(R.string.rs) + dataList.getBooking_charge());
        holder.txtDownPayment.setText(mContext.getResources().getString(R.string.rs) + dataList.getDown_payment());
        holder.txtInstallment.setText(mContext.getResources().getString(R.string.rs) + dataList.getMonthly_installment());
        holder.txtTenure.setText(dataList.getTennure());
        holder.txtPromoCode.setText(dataList.getPromo_code());

        if (dataList.getAccount_status().equals("1")) {
            holder.txtAccountStatus.setText("Active");
            holder.txtAccountStatus.setTextColor(Color.argb(255, 0, 128, 0));
            holder.closeLayout.setVisibility(View.GONE);
        } else if (dataList.getAccount_status().equals("2")) {
            holder.txtAccountStatus.setText("Matured");
            holder.txtAccountStatus.setTextColor(Color.BLUE);
            holder.closeLayout.setVisibility(View.GONE);
        } else {
            holder.txtAccountStatus.setText("Closed");
            holder.txtAccountStatus.setTextColor(Color.RED);
            holder.closeLayout.setVisibility(View.VISIBLE);
        }

        holder.progressBar.setSecondaryProgress(Integer.parseInt(dataList.getTotalPaidInstallment()));
        holder.progressBar.setProgress(Integer.parseInt(dataList.getTotalPaidInstallment()));
        holder.progressBar.setMax(Integer.parseInt(dataList.getTennure()));


        holder.imgGoldBookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vgold.co.in/dashboard/user/module/goldbooking/booking_receipt.php?bid=" + dataList.getGold_booking_id() + "&&user_id=" + uid));
                mContext.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userGoldDetailsArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.txtDate)
        TextView txtDate;

        @InjectView(R.id.txtGoldBookingId)
        TextView txtGoldBookingId;

        @InjectView(R.id.txtGoldQty)
        TextView txtGoldQty;

        @InjectView(R.id.txtRate)
        TextView txtRate;

        @InjectView(R.id.txtGoldBookingValue)
        TextView txtGoldBookingValue;

        @InjectView(R.id.txtBookingCharge)
        TextView txtBookingCharge;

        @InjectView(R.id.txtDownPayment)
        TextView txtDownPayment;

        @InjectView(R.id.txtInstallment)
        TextView txtInstallment;

        @InjectView(R.id.txtTenure)
        TextView txtTenure;
        @InjectView(R.id.txtPromoCode)
        TextView txtPromoCode;
        @InjectView(R.id.txtAccountStatus)
        TextView txtAccountStatus;

        @InjectView(R.id.imgGoldBookingHistory)
        ImageView imgGoldBookingHistory;
        @InjectView(R.id.progressBar)
        ProgressBar progressBar;
        @InjectView(R.id.closeLayout)
        LinearLayout closeLayout;

        @InjectView(R.id.detailLayout)
        LinearLayout detailLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


    public void resetData() {
        userGoldDetailsArrayList = orgUserGoldDetailsArrayList;
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
                    results.values = orgUserGoldDetailsArrayList;
                    results.count = orgUserGoldDetailsArrayList.size();
                } else {
                    // We perform filtering operation
                    List<UserGoldDetailsModel.Data> customerArrayList = new ArrayList<>();
                    for (UserGoldDetailsModel.Data dataModel : userGoldDetailsArrayList) {

                        if (dataModel.getGold_booking_id().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getGold().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))
                                || dataModel.getAdded_date().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
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
            userGoldDetailsArrayList = (ArrayList<UserGoldDetailsModel.Data>) results.values;
            notifyDataSetChanged();

        }
    }
}