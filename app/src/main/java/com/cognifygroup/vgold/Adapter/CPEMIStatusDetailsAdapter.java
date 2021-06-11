package com.cognifygroup.vgold.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cognifygroup.vgold.ChannelPartner.UserCommissionDetailsModel;
import com.cognifygroup.vgold.ChannelPartner.UserEMIStatusDetailsModel;
import com.cognifygroup.vgold.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CPEMIStatusDetailsAdapter extends RecyclerView.Adapter<CPEMIStatusDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    ArrayList<UserEMIStatusDetailsModel.Data> emiStatusDetailsArrayList;

    public CPEMIStatusDetailsAdapter(Context Context,
                                     ArrayList<UserEMIStatusDetailsModel.Data> emiStatusDetailsArrayList) {
        this.mContext = Context;
        this.activity = (Activity) Context;
        this.emiStatusDetailsArrayList = emiStatusDetailsArrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_user_emi_status_adapter, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        UserEMIStatusDetailsModel.Data dataList = emiStatusDetailsArrayList.get(position);

        holder.txtDate.setText(dataList.getTransaction_date());
        if(dataList.getIs_paid().equalsIgnoreCase("1")){
            holder.txtIsPaid.setText("Paid");
            holder.imgPaid.setVisibility(View.VISIBLE);
        }else{
            holder.txtIsPaid.setText("Pending");
        }

        holder.txtInstallment.setText(mContext.getResources().getString(R.string.rs) + dataList.getInstallment());
        holder.txtRemainAmt.setText(mContext.getResources().getString(R.string.rs) + dataList.getRemaining_amount());
        holder.txtNextDueDate.setText( dataList.getNext_due_date());

    }

    @Override
    public int getItemCount() {
        return emiStatusDetailsArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.txtDate)
        TextView txtDate;

        @InjectView(R.id.txtIsPaid)
        TextView txtIsPaid;

        @InjectView(R.id.txtInstallment)
        TextView txtInstallment;

        @InjectView(R.id.txtRemainAmt)
        TextView txtRemainAmt;

        @InjectView(R.id.txtNextDueDate)
        TextView txtNextDueDate;
        @InjectView(R.id.imgPaid)
        ImageView imgPaid;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}