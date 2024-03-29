package com.example.jpxtest;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpxtest.data.AppDataBase;
import com.example.jpxtest.data.ProductDao;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    public static final String TAG = "ProductViewModel";
    private LiveData<List<Product>> mProducts;
    private ProductRepository productRepository;

    public ProductViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
        mProducts = productRepository.getProducts();
    }

    public static final String CONTENT = "ABCDEFGHIJKLMNOPQ";

    public LiveData<List<Product>> getProducts() {

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
        private ProductRepository repository;
        public ProductViewModelFactory(ProductRepository repository) {
            this.repository = repository;
            Log.d(TAG, "ProductViewModelFactory 构造方法: ");
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            Log.d(TAG, "ProductViewModelFactory create: ");
            return (T) new ProductViewModel(repository);
        }
    }


    public boolean addProduct() {
        List<Product> value = mProducts.getValue();
        if (value.size() >= CONTENT.length()){
            return false;
        }
        productRepository.insert(generateProduct(value != null ? value.size() : 0));
        return true;
    }

    public void deleteProduct() {
        List<Product> value = mProducts.getValue();
        if (value != null && value.size() > 0){
            productRepository.delete(value.get(value.size() -1));
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
