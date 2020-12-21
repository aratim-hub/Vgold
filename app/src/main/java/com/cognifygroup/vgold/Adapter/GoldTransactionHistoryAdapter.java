package com.cognifygroup.vgold.Adapter;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cognifygroup.vgold.GetGoldTransactionHistory.GetGoldTransactionHistoryModel;
import com.cognifygroup.vgold.GoldBookingHistoryActivity;
import com.cognifygroup.vgold.GoldTransactionHistoryActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.TransactionDetailsActivity;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shivraj on 6/17/18.
 */

public class GoldTransactionHistoryAdapter extends RecyclerView.Adapter<GoldTransactionHistoryAdapter.MyViewHolder>{

    private GoldTransactionHistoryActivity mContext;
    String str = null;
    String booking_id;
    ArrayList<GetGoldTransactionHistoryModel.Data> goldTransactionHistoryArrayList;

    public GoldTransactionHistoryAdapter(GoldTransactionHistoryActivity Context, ArrayList<GetGoldTransactionHistoryModel.Data> goldTransactionHistoryArrayList,String booking_id) {
        this.mContext = Context;
        this.goldTransactionHistoryArrayList = goldTransactionHistoryArrayList;
        this.booking_id=booking_id;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.gold_transaction_history_item_adapter,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtTransactionId.setText(booking_id);

        if (goldTransactionHistoryArrayList.get(position).getEntry_status().equals("Credited")){
            holder.txtStatus.setText("Credit");
            holder.txtStatus.setTextColor(Color.argb(255,0,128,0));
        }else {
            holder.txtStatus.setText("Debit");
            holder.txtStatus.setTextColor(Color.RED);
        }

        holder.txtInstallmentAmount.setText("Amount :  \u20B9 "+goldTransactionHistoryArrayList.get(position).getInstallment());

        holder.txtTransactionId.setText(""+goldTransactionHistoryArrayList.get(position).getId());


        holder.txtTransactionDate.setText("Date  :  "+goldTransactionHistoryArrayList.get(position).getTransaction_date());

        holder.cardTransactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, TransactionDetailsActivity.class)
                .putExtra("installment",goldTransactionHistoryArrayList.get(position).getInstallment())
                .putExtra("recipt_no",goldTransactionHistoryArrayList.get(position).getId())
                .putExtra("date",goldTransactionHistoryArrayList.get(position).getTransaction_date())
                .putExtra("remainingamt",goldTransactionHistoryArrayList.get(position).getRemaining_amount())
                .putExtra("period",goldTransactionHistoryArrayList.get(position).getPeriod())
                .putExtra("txnid",goldTransactionHistoryArrayList.get(position).getTransaction_id())
                .putExtra("bankdetail",goldTransactionHistoryArrayList.get(position).getBank_details())
                .putExtra("paymentmethod",goldTransactionHistoryArrayList.get(position).getPayment_method())
                .putExtra("chequeno",goldTransactionHistoryArrayList.get(position).getCheque_no())
                .putExtra("adminstatus",goldTransactionHistoryArrayList.get(position).getAdmin_status())
                .putExtra("gold_id",booking_id)
                .putExtra("next_due_date",goldTransactionHistoryArrayList.get(position).getNext_due_date()));

            }
        });

    }

    @Override
    public int getItemCount() {
        return goldTransactionHistoryArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.cardTransactionHistory)
        CardView cardTransactionHistory;
       @InjectView(R.id.txtTransactionId)
       TextView txtTransactionId;
        @InjectView(R.id.txtInstallmentAmount)
        TextView txtInstallmentAmount;
        @InjectView(R.id.txtTransactionDate)
        TextView txtTransactionDate;
        @InjectView(R.id.txtStatus)
        TextView txtStatus;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }

}
