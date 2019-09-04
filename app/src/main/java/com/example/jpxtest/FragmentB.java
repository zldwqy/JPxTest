package com.example.jpxtest;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.jpxtest.databinding.FragmentBBinding;

import java.util.List;


public class FragmentB extends Fragment {

    public static final String TAG = "FragmentB";

    private ProductViewModel productViewModel;
    private FragmentBBinding bBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bBinding = FragmentBBinding.inflate(inflater, container, false);
        return bBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProductViewModel.ProductViewModelFactory factory = new ProductViewModel.ProductViewModelFactory(getContext());
        productViewModel = ViewModelProviders.of(getActivity(), factory).get(ProductViewModel.class);
        Log.d(TAG, "onViewCreated: "+productViewModel);
        Bundle arguments = getArguments();
        int position = arguments.getInt("position",-1);
        if (position != -1){
            List<Product> value = productViewModel.getProducts().getValue();
            if (value != null && value.size() > position) {
                Product product = value.get(position);
                String name = product.getName();
                int num = product.getNum();
                float price = product.getPrice();
                bBinding.txt.setText("name: "+name + " num: "+num + " price: "+price);

            }
        }


    }

    @Nullable
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimator(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }


}
