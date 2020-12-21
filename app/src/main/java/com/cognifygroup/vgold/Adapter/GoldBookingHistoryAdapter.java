package com.cognifygroup.vgold.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.GoldBookingHistoryActivity;
import com.cognifygroup.vgold.GoldTransactionHistoryActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoldBookingHistoryAdapter extends RecyclerView.Adapter<GoldBookingHistoryAdapter.MyViewHolder> {

    private GoldBookingHistoryActivity mContext;
    String str = null;
    ArrayList<GetGoldBookingHistoryModel.Data> goldBookingHistoryArrayList;

    public GoldBookingHistoryAdapter(GoldBookingHistoryActivity Context, ArrayList<GetGoldBookingHistoryModel.Data> goldBookingHistoryArrayList) {
        this.mContext = Context;
        this.goldBookingHistoryArrayList = goldBookingHistoryArrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gold_booking_history_item_adapter, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        /*GetGoldBookingHistoryModel.Data addSaleArrayList = goldBookingHistoryArrayList.get(position);



        holder.txtGoldBookingId.setText("Gold booking Id " + addSaleArrayList.getGold_booking_id());
        holder.txtGoldQty.setText(addSaleArrayList.getGold()+" gm");
        holder.txtGoldRate.setText(addSaleArrayList.getRate());
        holder.txtGoldBookingValue.setText(addSaleArrayList.getBooking_amount());
        holder.txtDownPayment.setText(addSaleArrayList.getDown_payment());
        holder.txtBookingCharge.setText(addSaleArrayList.getBooking_charge());
        holder.txtTenure.setText(addSaleArrayList.getTennure());
        holder.txtInstallment.setText(addSaleArrayList.getMonthly_installment());
*/
       /* String date = "" +goldBookingHistoryArrayList.get(position).getAdded_date();
        SimpleDateFormat spf = new SimpleDateFormat("mm/dd/yyyy");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd MMM yyyy");
        String newDateString = spf.format(newDate);*/

        double gold = Double.parseDouble(goldBookingHistoryArrayList.get(position).getGold());
        DecimalFormat numberFormat = new DecimalFormat("#.000");
        gold = Double.parseDouble(numberFormat.format(gold));

        holder.txtDate1.setText(goldBookingHistoryArrayList.get(position).getAdded_date());
        holder.txtGoldBookingId1.setText(goldBookingHistoryArrayList.get(position).getGold_booking_id());
        holder.txtGoldQty1.setText(gold + " GM");
        holder.txtRate1.setText(goldBookingHistoryArrayList.get(position).getRate());
        holder.txtGoldBookingValue1.setText(goldBookingHistoryArrayList.get(position).getBooking_amount());
        holder.txtBookingCharge1.setText(goldBookingHistoryArrayList.get(position).getBooking_charge());
        holder.txtDownPayment1.setText(goldBookingHistoryArrayList.get(position).getDown_payment());
        holder.txtInstallment1.setText(goldBookingHistoryArrayList.get(position).getMonthly_installment());
        holder.txtTenure1.setText(goldBookingHistoryArrayList.get(position).getTennure());

        if (goldBookingHistoryArrayList.get(position).getAccount_status().equals("1")) {
            holder.txtAccountStatus.setText("Active");
            holder.txtAccountStatus.setTextColor(Color.argb(255, 0, 128, 0));
            holder.closeLayout.setVisibility(View.GONE);
        } else if (goldBookingHistoryArrayList.get(position).getAccount_status().equals("2")) {
            holder.txtAccountStatus.setText("Matured");
            holder.txtAccountStatus.setTextColor(Color.BLUE);
            holder.closeLayout.setVisibility(View.GONE);
        } else {
            holder.txtAccountStatus.setText("Closed");
            holder.txtAccountStatus.setTextColor(Color.RED);
            holder.closeLayout.setVisibility(View.VISIBLE);
        }

        holder.progressBar.setSecondaryProgress(Integer.parseInt(goldBookingHistoryArrayList.get(position).getPaidInstallmentCount()));
        holder.progressBar.setProgress(Integer.parseInt(goldBookingHistoryArrayList.get(position).getPaidInstallmentCount()));
        holder.progressBar.setMax(Integer.parseInt(goldBookingHistoryArrayList.get(position).getTennure()));

        holder.cardRating1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, GoldTransactionHistoryActivity.class).putExtra("GOLD_BOOKING_ID", goldBookingHistoryArrayList.get(position).getGold_booking_id()));
            }
        });

        holder.imgGoldBookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vgold.co.in/dashboard/user/module/goldbooking/booking_receipt.php?bid=" + goldBookingHistoryArrayList.get(position).getGold_booking_id() + "&&user_id=" + VGoldApp.onGetUerId()));
                mContext.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return goldBookingHistoryArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.cardRating1)
        CardView cardRating1;

        @InjectView(R.id.txtDate1)
        TextView txtDate1;

        @InjectView(R.id.txtGoldBookingId1)
        TextView txtGoldBookingId1;
        @InjectView(R.id.txtGoldQty1)
        TextView txtGoldQty1;
        @InjectView(R.id.txtRate1)
        TextView txtRate1;
        @InjectView(R.id.txtGoldBookingValue1)
        TextView txtGoldBookingValue1;
        @InjectView(R.id.txtBookingCharge1)
        TextView txtBookingCharge1;
        @InjectView(R.id.txtDownPayment1)
        TextView txtDownPayment1;
        @InjectView(R.id.txtInstallment1)
        TextView txtInstallment1;
        @InjectView(R.id.txtTenure1)
        TextView txtTenure1;
        @InjectView(R.id.txtAccountStatus)
        TextView txtAccountStatus;
        @InjectView(R.id.imgGoldBookingHistory)
        ImageView imgGoldBookingHistory;
        @InjectView(R.id.progressBar)
        ProgressBar progressBar;
        @InjectView(R.id.closeLayout)
        LinearLayout closeLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}