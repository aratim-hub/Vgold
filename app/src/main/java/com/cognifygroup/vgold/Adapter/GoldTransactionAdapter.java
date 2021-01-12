package com.cognifygroup.vgold.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cognifygroup.vgold.GoldWalletActivity;
import com.cognifygroup.vgold.MoneyWalletActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.getAllTransactionForGold.GetAllTransactionGoldModel;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shivraj on 6/21/18.
 */

public class GoldTransactionAdapter extends RecyclerView.Adapter<GoldTransactionAdapter.MyViewHolder> {

    GoldWalletActivity mContext;
    ArrayList<GetAllTransactionGoldModel.Data> goldTransactionArraylist;

    public GoldTransactionAdapter(GoldWalletActivity mContext, ArrayList<GetAllTransactionGoldModel.Data> goldTransactionArraylist) {
        this.mContext = mContext;
        this.goldTransactionArraylist = goldTransactionArraylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gold_booking_history_adapter, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (!goldTransactionArraylist.get(position).getTransaction_id().equals("")) {
            holder.txtTxnIdMoney1.setText(goldTransactionArraylist.get(position).getTransaction_id());
        } else if (!goldTransactionArraylist.get(position).getOnline_transaction_id().equals("")) {
            holder.txtTxnIdMoney1.setText(goldTransactionArraylist.get(position).getOnline_transaction_id());
        } else {
            holder.txtTxnIdMoney1.setText(goldTransactionArraylist.get(position).getCheque_no());
        }

        double gold = Double.parseDouble(goldTransactionArraylist.get(position).getGold());
        DecimalFormat numberFormat = new DecimalFormat("#.000");
        gold = Double.parseDouble(numberFormat.format(gold));
        holder.txtgoldBalance.setText(gold + " GM");
        holder.txtTimeDateMoney1.setText(goldTransactionArraylist.get(position).getTransaction_date());
        holder.txtPaymentThrough.setText(goldTransactionArraylist.get(position).getPayment_method());
        if (!goldTransactionArraylist.get(position).getReceived_from().equals("")) {
            holder.txtPaymentFromTo.setText(goldTransactionArraylist.get(position).getReceived_from());
        } else {
            holder.txtPaymentFromTo.setText(goldTransactionArraylist.get(position).getTransafer_to());
        }
//        holder.txtStatus1.setText(goldTransactionArraylist.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return goldTransactionArraylist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.txtTxnIdMoney1)
        TextView txtTxnIdMoney1;
        @InjectView(R.id.txtgoldBalance)
        TextView txtgoldBalance;
        @InjectView(R.id.txtTimeDateMoney1)
        TextView txtTimeDateMoney1;
        @InjectView(R.id.txtPaymentThrough)
        TextView txtPaymentThrough;
        @InjectView(R.id.txtPaymentFromTo)
        TextView txtPaymentFromTo;
        @InjectView(R.id.txtStatus1)
        TextView txtStatus1;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
