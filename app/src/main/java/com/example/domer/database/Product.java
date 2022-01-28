package com.example.domer.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "exam_table")
public class Product implements Serializable {

    public Product(){}
    @PrimaryKey
    private int id;
    private String article;
    private String color;
    private String size;
    private String address;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(article, product.article) &&
                Objects.equals(color, product.color) &&
                Objects.equals(size, product.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(article, color, size, address);
    }

    public Product(String article, String color, String size, String address) {
        this.article = article;
        this.color = color;
        this.size = size;
        this.address = address;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getArticle() {
        return article;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setArticle(String article){
        this.article = article;
    }

}
