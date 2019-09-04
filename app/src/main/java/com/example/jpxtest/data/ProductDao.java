package com.example.jpxtest.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.jpxtest.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Product ... products);

    @Query("SELECT * FROM product")
    LiveData<List<Product>> query();

    @Delete
    void delete(Product ... products);


}
