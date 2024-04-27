package com.sph.sbh.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.sph.sbh.Activities.DetailActivity;
import com.sph.sbh.Helper.ManagmentCart;
import com.sph.sbh.Model.ItemsDomain;
import com.sph.sbh.databinding.ViewholderPopListBinding;

import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    ArrayList<ItemsDomain> items;

    Context context;
    boolean showX = false ;
    boolean isSaled = false;
    ManagmentCart managmentCart;

    public PopularAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }
    public PopularAdapter(ArrayList<ItemsDomain> items ,boolean showX) {
        this.items = items;
        this.showX = showX;
    }
    public PopularAdapter(ArrayList<ItemsDomain> items ,boolean showX, boolean isSaled ) {
        this.items = items;
        this.showX = showX;
        this.isSaled = isSaled;
    }


    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
         managmentCart = new ManagmentCart(context);
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(LayoutInflater.from(context),parent,
            false);
        return new Viewholder(binding);
    }
    public void setFiltredList(ArrayList<ItemsDomain> filterList){
        this.items = filterList;
        notifyDataSetChanged();
    }
    public void setFiltredListFav(ArrayList<ItemsDomain> filterListFav){
        this.items = filterListFav;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
            holder.binding.title2.setText(items.get(position).getTitle());
            holder.binding.reviewTxt.setText("" + items.get(position).getReview());
            holder.binding.priceTxt1.setText(items.get(position).getPrice() + "dt");
            holder.binding.ratingTxt.setText("(" + items.get(position).getRating() + ")");
            holder.binding.oldPriceTxt.setText(items.get(position).getOldPrice() + "dt");
            if(isSaled == true) {
                holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
            else {
                holder.binding.priceTxt1.setVisibility(View.GONE);
                holder.binding.oldPriceTxt.setTextSize(10);
                holder.binding.oldPriceTxt.setTypeface(holder.binding.oldPriceTxt.getTypeface(), Typeface.BOLD);

            }
            holder.binding.ratingBar3.setRating((float) items.get(position).getRating());

            if (showX == true){
                holder.binding.close.setVisibility(View.VISIBLE);
            }
            else {
                holder.binding.close.setVisibility(View.GONE);
            }
        holder.binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.removeFromFavorites(items.get(position));
                holder.binding.info.setVisibility(View.GONE);


            }
        });



            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop());
            Glide.with(context).load(items.get(position).getPicUrl().get(0))
                    .apply(requestOptions)
                    .into(holder.binding.pic);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("object",items.get(position));
                    context.startActivity(intent);

                }
            });


    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopListBinding binding;

        public Viewholder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
