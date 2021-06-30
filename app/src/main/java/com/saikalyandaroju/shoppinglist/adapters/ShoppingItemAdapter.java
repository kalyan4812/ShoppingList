package com.saikalyandaroju.shoppinglist.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ItemViewHolder> {
    private RequestManager requestManager;
    public List<ShoppingItem> shoppingItems = new ArrayList<>();

    @Inject
    public ShoppingItemAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    private DiffUtil.ItemCallback<ShoppingItem> diffUtil = new DiffUtil.ItemCallback<ShoppingItem>() {

        @Override
        public boolean areItemsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };
    private AsyncListDiffer<ShoppingItem> differ = new AsyncListDiffer(this, diffUtil);

    public List get() {
        return differ.getCurrentList();
    }

    public void set(List<ShoppingItem> shoppingItems) {
        differ.submitList(shoppingItems);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.i("check",shoppingItems.size()+"");
        ShoppingItem shoppingItem = shoppingItems.get(position);
        requestManager.load(shoppingItem.getImageUrl()).into(holder.imageView);
        holder.name.setText(shoppingItem.getName());
        holder.amount.setText(shoppingItem.getAmount() + "");
        holder.price.setText(shoppingItem.getPrice() + "");
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, amount, price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivShoppingImage);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvShoppingItemPrice);
            amount = itemView.findViewById(R.id.tvShoppingItemAmount);
        }
    }
}
