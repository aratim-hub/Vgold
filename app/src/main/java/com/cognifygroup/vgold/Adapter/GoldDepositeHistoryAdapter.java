package com.cognifygroup.vgold.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.GetGoldDepositeHistory.GetGoldDepositeHistoryModel;
import com.cognifygroup.vgold.GoldBookingHistoryActivity;
import com.cognifygroup.vgold.GoldDepositeHistoryActivity;
import com.cognifygroup.vgold.GoldTransactionHistoryActivity;
import com.cognifygroup.vgold.OfferLetterActivity;
import com.cognifygroup.vgold.PayActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.getGoldBookingHistory.GetGoldBookingHistoryModel;
import com.cognifygroup.vgold.utils.AlertDialogOkListener;
import com.cognifygroup.vgold.utils.AlertDialogs;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoldDepositeHistoryAdapter extends RecyclerView.Adapter<GoldDepositeHistoryAdapter.MyViewHolder> implements AlertDialogOkListener {

    private AlertDialogs mAlert;
    private GoldDepositeHistoryActivity mContext;
    String str = null;
    ArrayList<GetGoldDepositeHistoryModel.Data> goldBookingHistoryArrayList;
    private AlertDialogOkListener alertDialogOkListener = this;

    public GoldDepositeHistoryAdapter(GoldDepositeHistoryActivity Context, ArrayList<GetGoldDepositeHistoryModel.Data> goldBookingHistoryArrayList) {
        this.mContext = Context;
        this.goldBookingHistoryArrayList = goldBookingHistoryArrayList;
        mAlert = AlertDialogs.getInstance();
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gold_deposite_history_item_adapter, null);
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

        holder.txtDate1.setText(goldBookingHistoryArrayList.get(position).getAdded_date());
        holder.txtGoldBookingId1.setText(goldBookingHistoryArrayList.get(position).getGold_deposite_id());
        holder.txtGoldQty1.setText(goldBookingHistoryArrayList.get(position).getGold() + "gm");
        holder.txtRate1.setText(goldBookingHistoryArrayList.get(position).getRate());
        holder.txtGoldMaturityweight.setText(goldBookingHistoryArrayList.get(position).getMaturity_weight());
        holder.txtBankGurantee.setText(goldBookingHistoryArrayList.get(position).getBank_guarantee());
        holder.txtAmount1.setText(goldBookingHistoryArrayList.get(position).getAmount());
        holder.txtgold_quality.setText(goldBookingHistoryArrayList.get(position).getGold_quality());
        holder.txtTenure1.setText(goldBookingHistoryArrayList.get(position).getTennure());

        if (goldBookingHistoryArrayList.get(position).getAccount_status().equals("1")) {
            holder.txtAccountStatus.setText("Active");
            holder.txtAccountStatus.setTextColor(Color.argb(255, 0, 128, 0));
        } else if (goldBookingHistoryArrayList.get(position).getAccount_status().equals("2")) {
            holder.txtAccountStatus.setText("Matured");
            holder.txtAccountStatus.setTextColor(Color.BLUE);
        } else {
            holder.txtAccountStatus.setText("Closed");
            holder.txtAccountStatus.setTextColor(Color.RED);
        }

        holder.cardRating1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity(new Intent(mContext,GoldTransactionHistoryActivity.class).putExtra("GOLD_BOOKING_ID",goldBookingHistoryArrayList.get(position).getGold_booking_id()));
            }
        });

        holder.imgGoldDepositeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bankGurantee = goldBookingHistoryArrayList.get(position).getBank_guarantee();
                if (bankGurantee.equals("yes")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vgold.co.in/dashboard/user/module/golddeposite/deposite_certificate.php?did=" + goldBookingHistoryArrayList.get(position).getGold_deposite_id() + "&&user_id=" + VGoldApp.onGetUerId()));
                    mContext.startActivity(browserIntent);
                } else if (bankGurantee.equals("no")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vgold.co.in/dashboard/user/module/golddeposite/deposite_certificate_without_bg.php?did=" + goldBookingHistoryArrayList.get(position).getGold_deposite_id() + "&&user_id=" + VGoldApp.onGetUerId()));
                    mContext.startActivity(browserIntent);
                } else {
                    AlertDialogs.alertDialogOk(mContext, "Alert", "Bank Guarantee not available",
                            mContext.getResources().getString(R.string.btn_ok), 4, false, alertDialogOkListener);

//                    mAlert.onShowToastNotification(mContext, "Bank Guarantee not available");
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return goldBookingHistoryArrayList.size();
    }

    @Override
    public void onDialogOk(int resultCode) {

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
        @InjectView(R.id.txtGoldMaturityweight)
        TextView txtGoldMaturityweight;
        @InjectView(R.id.txtBankGurantee)
        TextView txtBankGurantee;
        @InjectView(R.id.txtAmount1)
        TextView txtAmount1;
        @InjectView(R.id.txtgold_quality)
        TextView txtgold_quality;
        @InjectView(R.id.txtTenure1)
        TextView txtTenure1;
        @InjectView(R.id.txtAccountStatus)
        TextView txtAccountStatus;
        @InjectView(R.id.imgGoldDepositeHistory)
        ImageView imgGoldDepositeHistory;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}