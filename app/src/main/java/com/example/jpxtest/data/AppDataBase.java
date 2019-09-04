package com.example.jpxtest.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.jpxtest.Product;

import java.util.List;
@Database(entities = {Product.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {

   public abstract ProductDao productDao();


   private static AppDataBase mInstance;


   public static AppDataBase getInstance(Context context) {
      if (mInstance == null){
          mInstance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"product.db").allowMainThreadQueries().build();
      }
      return mInstance;
   }
}
