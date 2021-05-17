package com.cognifygroup.vgold.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.cognifygroup.vgold.Application.VGoldApp;
import com.cognifygroup.vgold.MainActivity;
import com.cognifygroup.vgold.OfferLetterActivity;
import com.cognifygroup.vgold.R;
import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cognifygroup.vgold.utils.Constant.IMAGE_URL;

/**
 * Created by shivraj on 7/26/18.
 */

public class VendorOfferAdapter extends RecyclerView.Adapter<VendorOfferAdapter.UserViewHolder> {

    private Activity mContext;
    ArrayList<VendorOfferModel.Data> mCategoryArray;
    private String area;
    private DisplayImageOptions options;
    //   private CategoryInterface mCategoryInterface;
    private String BaseUrl;
    private String key;


    public VendorOfferAdapter(Context Context, ArrayList<VendorOfferModel.Data> mCategoryArray, String key) {
        this.mContext = (Activity) Context;
        this.mCategoryArray = mCategoryArray;
        this.key = key;
        //  mCategoryInterface=mContext;


    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_our_business, null);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        //set font

        holder.rlayout_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, OfferLetterActivity.class);
                intent.putExtra("offer", mCategoryArray.get(position).getLetter_path());
                intent.putExtra("offer1", mCategoryArray.get(position).getAdvertisement_path());
                intent.putExtra("venderId", mCategoryArray.get(position).getVendor_id());
                mContext.startActivity(intent);
                mContext.finish();
            }
        });


       /* if (key.equalsIgnoreCase("venders")) {
            BaseUrl = IMAGE_URL + mCategoryArray.get(position).getAdvertisement_path();
        } else if (key.equalsIgnoreCase("offers")) {
        }*/
        BaseUrl = IMAGE_URL + mCategoryArray.get(position).getLogo_path();

        if (BaseUrl != null && !TextUtils.isEmpty(BaseUrl)) {

           /* Glide.with(mContext).load(BaseUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imv_category);*/

            Picasso.with(mContext).load(BaseUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imv_category);


                /*Picasso.with(mContext)
                        .load(BaseUrl)
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.imv_category, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (holder.progressbar_category != null) {
                                    holder.progressbar_category.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onError() {
                                Toast.makeText(mContext, "No Image Found",Toast.LENGTH_SHORT).show();
                            }
                        });*/

            /*if (holder.progressbar_category != null) {
                holder.progressbar_category.setVisibility(View.GONE);
            }*/
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryArray.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        /*@InjectView(R.id.cardView)
        CardView cardView;*/
        @InjectView(R.id.rlayout_category)
        RelativeLayout rlayout_category;
        @InjectView(R.id.imv_category)
        ImageView imv_category;
//        @InjectView(R.id.progressbar_category)
//        ProgressBar progressbar_category;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }
    }

  /*  public interface CategoryInterface {

        public void onGetPositionCategory(int position,String categoryId,String categoryName);
    }*/


}
