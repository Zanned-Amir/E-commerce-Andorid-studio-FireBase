package com.sph.sbh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.sph.sbh.Model.ReviewDomain;
import com.sph.sbh.databinding.ViewholderReviewBinding;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Viewholder> {
    ArrayList<ReviewDomain> items;
    Context context;

    public ReviewAdapter(ArrayList<ReviewDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ReviewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderReviewBinding binding = ViewholderReviewBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.Viewholder holder, int position) {
    holder.binding.name.setText(items.get(position).getName());
    holder.binding.descTxt.setText(items.get(position).getDescription());
        holder.binding.rattingTxt.setText(""+items.get(position).getRating());
        Glide.with(context).
                load(items.get(position).getPicUrl())
                .transform(new GranularRoundedCorners(100,100,100,100))
                .into(holder.binding.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderReviewBinding binding;
        public Viewholder( ViewholderReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
