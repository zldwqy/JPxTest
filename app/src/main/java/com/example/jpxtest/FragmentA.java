package com.example.jpxtest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.jpxtest.databinding.FragmentABinding;

import java.util.List;


public class FragmentA extends Fragment {

    public static final String TAG = "FragmentA";
    private CartAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentABinding aBinding = FragmentABinding.inflate(inflater, container, false);
        cartAdapter = new CartAdapter();
        aBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        aBinding.recyclerview.setAdapter(cartAdapter);
        getLifecycle().addObserver(new LocationLifeCycle());
        ProductViewModel.ProductViewModelFactory factory = new ProductViewModel.ProductViewModelFactory();
        final ProductViewModel productViewModel = ViewModelProviders.of(this, factory).get(ProductViewModel.class);
//        final ProductViewModel productViewModel = factory.create(ProductViewModel.class);
        productViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products != null){
                    cartAdapter.setProducts(products);
                    Log.d(TAG, "onChanged: "+products.size());
                }

            }
        });
        aBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productViewModel.addProduct();
                Log.d(TAG, "addProduct: ");
            }
        });

        aBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productViewModel.deleteProduct();
                Log.d(TAG, "deleteProduct: ");

            }
        });

        return aBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
}
