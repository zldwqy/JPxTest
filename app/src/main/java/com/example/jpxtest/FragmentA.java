package com.example.jpxtest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.jpxtest.databinding.FragmentABinding;

import java.util.List;


public class FragmentA extends Fragment implements CartAdapter.IOnItemClickListener {

    public static final String TAG = "FragmentA";
    private CartAdapter cartAdapter;
    private ProductViewModel productViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentABinding aBinding = FragmentABinding.inflate(inflater, container, false);
        cartAdapter = new CartAdapter();
        cartAdapter.setOnItemClickListener(this);
        aBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        aBinding.recyclerview.setAdapter(cartAdapter);
        getLifecycle().addObserver(new LocationLifeCycle());
        ProductViewModel.ProductViewModelFactory factory = new ProductViewModel.ProductViewModelFactory(getContext());
        productViewModel = ViewModelProviders.of(getActivity(), factory).get(ProductViewModel.class);
        Log.d(TAG, "onCreateView--productViewModel: "+ productViewModel);
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
                boolean b = productViewModel.addProduct();
                Log.d(TAG, "addProduct: "+b);
                if (!b){
                    Toast.makeText(getContext(),"添加的数量已达上限",Toast.LENGTH_LONG).show();
                }
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

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        NavHostFragment.findNavController(this).navigate(R.id.action_to_fragmentB,bundle);
    }
}
