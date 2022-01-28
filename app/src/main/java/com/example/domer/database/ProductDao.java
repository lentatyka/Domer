package com.example.domer.database;



import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insertAll(Product...products);

    @Insert
    void insert(Product product);

    @Query("SELECT * FROM exam_table WHERE article LIKE :article")
    List<Product> searchArticle(String article);

    @Update(entity = Product.class)
    int update(ProductRepository.ProductAddress productAddress);

}
