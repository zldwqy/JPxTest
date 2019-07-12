package com.example.jpxtest;


import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    public static final String TAG = "ProductViewModel";
    MediatorLiveData<List<Product>> mProducts;

    public ProductViewModel() {
        mProducts = new MediatorLiveData<>();
        Log.d(TAG, "ProductViewModel: ");
    }


    public static final String CONTENT = "ABCDEFGHIJKLMNOPQ";


    public LiveData<List<Product>> generateProducts() {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Product product = generateProduct(i);
            products.add(product);
        }
        mProducts.setValue(products);
        return mProducts;
    }

    public MediatorLiveData<List<Product>> getProducts() {
        if (mProducts.getValue() == null) {
            generateProducts();
        }
        return mProducts;
    }

    private Product generateProduct(int i) {
        Product product = new Product();
        product.setId(i);
        product.setName(String.valueOf(CONTENT.charAt(i)));
        product.setPrice(i * 1.11f);
        return product;
    }

    public static class ProductViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        public ProductViewModelFactory() {
        }
    }


    public boolean addProduct() {
        List<Product> value = mProducts.getValue();
        if (value != null && value.size() == CONTENT.length()) {
            return false;
        }
        value.add(generateProduct(value != null ? value.size() : 0));
        mProducts.setValue(value);
        return true;
    }

    public void deleteProduct() {
        List<Product> value = mProducts.getValue();
        if (value != null && value.size() > 0) {
            value.remove(value.size() - 1);
            mProducts.setValue(value);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (mProducts.getValue() != null) {
            mProducts.getValue().clear();
            mProducts = null;
        }
        Log.d(TAG, "onCleared: ");
    }
}
