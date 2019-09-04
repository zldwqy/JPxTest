package com.example.jpxtest;

import androidx.lifecycle.LiveData;

import com.example.jpxtest.data.ProductDao;

import java.util.List;

public class ProductRepository {

    private static ProductRepository mInstance;

    private ProductDao productDao;

    private ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public static ProductRepository getInstance(ProductDao productDao) {
        if (mInstance == null){
            mInstance = new ProductRepository(productDao);
        }
        return mInstance;
    }

    public LiveData<List<Product>> getProducts(){
        if (productDao != null){
            return productDao.query();
        }
        return null;
    }

    public List<Long> insert(Product ... products){
        if (productDao != null){
            return productDao.insert(products);
        }
        return null;
    }

    public void delete(Product ... products){
        if (productDao != null){
            productDao.delete(products);
        }
    }
}
