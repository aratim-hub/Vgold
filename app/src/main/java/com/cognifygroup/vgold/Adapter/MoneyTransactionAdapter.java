package com.cognifygroup.vgold.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cognifygroup.vgold.MoneyWalletActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.getAllTransactionForMoney.GetAllTransactionMoneyModel;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shivraj on 6/21/18.
 */

public class MoneyTransactionAdapter extends RecyclerView.Adapter<MoneyTransactionAdapter.MyViewHolder> {

    MoneyWalletActivity mContext;
    ArrayList<GetAllTransactionMoneyModel.Data> moneyTransactionArraylist;

    public MoneyTransactionAdapter(MoneyWalletActivity mContext, ArrayList<GetAllTransactionMoneyModel.Data> moneyTransactionArraylist) {
        this.mContext = mContext;
        this.moneyTransactionArraylist = moneyTransactionArraylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_adapter,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!moneyTransactionArraylist.get(position).getTransaction_id().equals("")){
            holder.txtTxnIdMoney.setText(moneyTransactionArraylist.get(position).getTransaction_id());
        }else if (!moneyTransactionArraylist.get(position).getOnline_transaction_id().equals("")){
            holder.txtTxnIdMoney.setText(moneyTransactionArraylist.get(position).getOnline_transaction_id());
        }else {
            holder.txtTxnIdMoney.setText(moneyTransactionArraylist.get(position).getCheque_no());
        }
        holder.txtRupeeMoney.setText("Rs."+moneyTransactionArraylist.get(position).getAmount());
        holder.txtTimeDateMoney.setText(moneyTransactionArraylist.get(position).getTransaction_date());
        holder.txtPaymentThrough.setText(moneyTransactionArraylist.get(position).getPayment_method());

        if (!moneyTransactionArraylist.get(position).getReceived_from().equals("")){
            holder.txtPaymentFromTo.setText(moneyTransactionArraylist.get(position).getReceived_from());
        }else {
            holder.txtPaymentFromTo.setText(moneyTransactionArraylist.get(position).getTransafer_to());
        }
        holder.txtStatus1.setText(moneyTransactionArraylist.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return moneyTransactionArraylist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.txtTxnIdMoney)
        TextView txtTxnIdMoney;
        @InjectView(R.id.txtRupeeMoney)
        TextView txtRupeeMoney;
        @InjectView(R.id.txtTimeDateMoney)
        TextView txtTimeDateMoney;
        @InjectView(R.id.txtPaymentThrough)
        TextView txtPaymentThrough;
        @InjectView(R.id.txtPaymentFromTo)
        TextView txtPaymentFromTo;
        @InjectView(R.id.txtStatus1)
        TextView txtStatus1;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
