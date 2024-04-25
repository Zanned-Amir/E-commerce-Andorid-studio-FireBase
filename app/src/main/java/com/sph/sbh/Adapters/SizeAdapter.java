package com.sph.sbh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sph.sbh.R;
import com.sph.sbh.databinding.ViewholderSizeBinding;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Viewholder> {
   ArrayList<String> items;
   Context context;
   int selectedPosition = -1 ;
   int lastSelectedPosition = -1;

    public SizeAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SizeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        ViewholderSizeBinding binding =ViewholderSizeBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.Viewholder holder, int position) {
    holder.binding.sizeTxt.setText(items.get(position));

    holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        lastSelectedPosition = selectedPosition;
        selectedPosition = holder.getAdapterPosition();
        notifyItemChanged(lastSelectedPosition);
        notifyItemChanged(selectedPosition);
    }
    });
    if( selectedPosition == holder.getAdapterPosition()){
        holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
        holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.green));

    } else{
        holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
        holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.black));
    }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderSizeBinding binding;

        public Viewholder(ViewholderSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
