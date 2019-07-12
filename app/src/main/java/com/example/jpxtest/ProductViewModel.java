package com.example.jpxtest;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    public static final String TAG = "ProductViewModel";

    public ProductViewModel() {
        Log.d(TAG, "ProductViewModel: ");
    }

    MediatorLiveData<List<Product>> mProducts = new MediatorLiveData<>();

    public static final String CONTENT="ABCDEFGHIJKLMNOPQ";


    public LiveData<List<Product>> getProducts() {
//        initProducts();
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Product product = generateProduct(i);
            products.add(product);
        }
        mProducts.setValue(products);
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


    public void addProduct(){
        List<Product> value = mProducts.getValue();
        value.add(generateProduct(mProducts.getValue().size()));
        mProducts.setValue(value);
    }

    public void deleteProduct(){
        List<Product> value = mProducts.getValue();
        value.remove(mProducts.getValue().size() -1);
        mProducts.setValue(value);
    }



}
