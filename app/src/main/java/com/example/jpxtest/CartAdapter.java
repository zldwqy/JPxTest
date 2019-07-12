package com.example.jpxtest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jpxtest.databinding.ItemCartBinding;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private List<Product> mProducts;
    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Product product = mProducts.get(position);
        holder.mbinding.name.setText(product.getName());
        holder.mbinding.price.setText(Float.toString(product.getPrice()));

    }

    public void setProducts(final List<Product> products) {
        this.mProducts = products;
        if (mProducts == null){
            mProducts = products;
//            notifyItemRangeInserted(0,mProducts.size());
            notifyDataSetChanged();
        }else {
//            if (products == null){
//                mProducts = products;
//                notifyDataSetChanged();
//            }
            DiffUtil.Callback diffCallback = new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mProducts.size();
                }

                @Override
                public int getNewListSize() {
                    return products.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mProducts.get(oldItemPosition).equals(products.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Product oldProduct = mProducts.get(oldItemPosition);
                    Product newProduct = products.get(newItemPosition);
                    return oldProduct.getId() == newProduct.getId() &&
                            oldProduct.getName().equals(newProduct.getName())&&
                            oldProduct.getPrice() == newProduct.getPrice();
                }
            };
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
            mProducts = products;
            result.dispatchUpdatesTo(this);
            notifyDataSetChanged();

        }
    }

    @Override
    public int getItemCount() {
        return mProducts != null ?mProducts.size() : 0;
    }

    public static class CartHolder extends RecyclerView.ViewHolder{

        ItemCartBinding mbinding;
        public CartHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            mbinding = binding;
        }
    }




}
