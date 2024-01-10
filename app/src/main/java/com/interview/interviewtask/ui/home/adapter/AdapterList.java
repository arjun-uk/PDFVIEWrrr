package com.interview.interviewtask.ui.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interview.interviewtask.R;
import com.interview.interviewtask.databinding.ItemListSingleBinding;
import com.interview.interviewtask.ui.home.response.ListResposne;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MyViewHolder> {
    public Context context;
    public ItemListSingleBinding binding;
    public List<ListResposne>listResposneList;
    public ItemCLickListner itemCLickListner;


    public interface ItemCLickListner{
        void ItemClick(String title,String id, ImageView download,String type,String pdfUrl);
    }

    public AdapterList(Context context, List<ListResposne> listResposneList,ItemCLickListner itemCLickListner) {
        this.context = context;
        this.listResposneList = listResposneList;
        this.itemCLickListner = itemCLickListner;

    }

    @NonNull
    @Override
    public AdapterList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemListSingleBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterList.MyViewHolder holder, int position) {

        binding.title.setText(listResposneList.get(holder.getAdapterPosition()).getTitle());
        binding.name.setText(listResposneList.get(holder.getAdapterPosition()).getAuthor());
        binding.codeNumber.setText("ISBN: "+listResposneList.get(holder.getAdapterPosition()).getIsbn());
        Glide.with(context).load(listResposneList.get(holder.getAdapterPosition()).getCover()).into(binding.image);

        binding.btnDownload.setOnClickListener(v -> {
            itemCLickListner.ItemClick(
                    listResposneList.get(holder.getAdapterPosition()).getTitle(),
                    listResposneList.get(holder.getAdapterPosition()).getId(),
                    binding.btnDownload,
                    "download",listResposneList.get(holder.getAdapterPosition()).getUrl());

            listResposneList.get(holder.getAdapterPosition()).setDownloaded(true);
            notifyItemChanged(holder.getAdapterPosition()); //
        });

        binding.getRoot().setOnClickListener(v -> {
            itemCLickListner.ItemClick(
                    listResposneList.get(holder.getAdapterPosition()).getTitle(),
                    listResposneList.get(holder.getAdapterPosition()).getId(),
                    binding.btnDownload,
                    "navigation",listResposneList.get(holder.getAdapterPosition()).getUrl());
        });
        if (listResposneList.get(holder.getAdapterPosition()).isDownloaded()) {

            binding.btnDownload.setImageResource(R.drawable.tick);
        } else {

            binding.btnDownload.setImageResource(R.drawable.download);
        }


    }


    @Override
    public int getItemCount() {
        return listResposneList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemListSingleBinding binding;
        public MyViewHolder(ItemListSingleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
