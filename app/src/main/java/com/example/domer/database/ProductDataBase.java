package com.example.domer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class ProductDataBase extends RoomDatabase {

    private static ProductDataBase instance;
    public abstract ProductDao productDao();

    public static synchronized ProductDataBase getInstance(Context context){
        if(instance == null){
            instance = buildDataBase(context);
        }
        return instance;
    }

    private static ProductDataBase buildDataBase(final Context context) {
        return Room.databaseBuilder(context, ProductDataBase.class, "example.db").build();
    }

    private static class Task {
        private final ProductDao productDao;
        private final FirebasaDatabaseHelper fbdb;
        private Task(ProductDataBase pdb){
            this.productDao = pdb.productDao();
            this.fbdb = FirebasaDatabaseHelper.getInstance();
        }
    }
}

