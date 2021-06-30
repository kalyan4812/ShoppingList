package com.saikalyandaroju.shoppinglist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.saikalyandaroju.shoppinglist.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private RequestManager manager;
    public List<String> images = new ArrayList<>();
    private ClickListener clickListenr;

    @Inject
    public ImageAdapter(RequestManager manager) {
        this.manager = manager;
    }

    private DiffUtil.ItemCallback<String> diffUtil = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem == newItem;
        }
    };
    private AsyncListDiffer differ = new AsyncListDiffer(this, diffUtil);

    public List get() {
        return differ.getCurrentList();
    }

    public void set(List<String> images) {
        differ.submitList(images);
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String url = images.get(position);
        manager.load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivShoppingImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAbsoluteAdapterPosition();
                    if(clickListenr!=null && pos!=-1){
                        clickListenr.onclick(images.get(pos));
                    }
                }
            });
        }
    }
    public interface ClickListener{
        void onclick(String url);
    }
    public void  setOnClickListenr(ClickListener clickListenr){
        this.clickListenr=clickListenr;
    }


}
