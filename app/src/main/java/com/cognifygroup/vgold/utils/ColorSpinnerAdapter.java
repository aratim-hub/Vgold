package com.cognifygroup.vgold.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cognifygroup.vgold.getBookingId.GetBookingIdModel;

import java.util.List;

import androidx.annotation.Nullable;


public class ColorSpinnerAdapter extends ArrayAdapter<GetBookingIdModel.Data> {
    private Context context;
    private List<GetBookingIdModel.Data> values;

    public ColorSpinnerAdapter(Context context, int textViewResourceId, List<GetBookingIdModel.Data> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public GetBookingIdModel.Data getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getId());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getId());

        if (values.get(position).getId() == null) {
            label.setBackgroundColor(Color.WHITE);
        } /*else if (values.get(position).getIs_paid().equals(0)) {
            label.setBackgroundColor(Color.WHITE);
        }*/ else {
            label.setBackgroundColor(Color.GREEN);
        }

        return label;
    }


}
