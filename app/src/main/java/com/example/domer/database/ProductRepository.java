package com.example.domer.database;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.domer.utilits.Preferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductRepository {
    private final ProductDao productDao;
    private final MutableLiveData<List<Product>> products;
    private List<Product> productList;

    public ProductRepository(Application application) {
        ProductDataBase dataBase = ProductDataBase.getInstance(application);
        productDao = dataBase.productDao();
        products = new MutableLiveData<>();
        productList = new ArrayList<>();
        checkData(application);
    }

    protected static class ProductAddress {
        int id;
        String address;

        protected ProductAddress(int id, String address) {
            this.id = id;
            this.address = address;
        }
    }

    private void checkData(Context context) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day != Preferences.SHARED_PREFERENCES.getData()) {
            Toast.makeText(context, "Проверка обновлений", Toast.LENGTH_SHORT).show();
            FirebasaDatabaseHelper.getInstance().readBook(products -> {

                CompletableFuture<List<Integer>> future = CompletableFuture.supplyAsync(() -> {
                    List<Integer> ins_upd = new ArrayList<>(); //0 update //1 insert
                    int value;
                    int update = 0;
                    int insert = 0;
                    for (Product val : products) {
                        ProductAddress productAddress = new ProductAddress(val.getId(),
                                val.getAddress());
                        value = productDao.update(productAddress);
                        //Insert if failed update.
                        if (value == 0) {
                            productDao.insert(val);
                            insert++;
                        } else
                            update++;
                    }
                    ins_upd.add(update);
                    ins_upd.add(insert);
                    return ins_upd;
                });
                try {
                    List<Integer> v = future.get();
                    Preferences.SHARED_PREFERENCES.setData(day);
                    Toast.makeText(context, "Обновлено: " + v.get(0)
                            + " Добавлено: " + v.get(1), Toast.LENGTH_SHORT).show();
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(context, "Ошибка. Перезапусти приложение" +
                            "или обратись к админу", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public CompletableFuture<List<Product>> searchArticle(String article) {
        return CompletableFuture.supplyAsync(()
                -> {
            productList = productDao.searchArticle("%" + article + "%");
            setProducts();
            return productList;
        });
    }

    public List<Product> searchSize(String value) {
        productList = Objects.requireNonNull(productList, "empty livedata")
                .stream()
                .filter(c -> c.getSize().equals(value)).collect(Collectors.toList());
        setProducts();
        return productList;
    }

    public List<Product> searchColorSize(List<String> value) {
        Stream<Product> color = Objects.requireNonNull(productList, "empty livedata")
                .stream()
                .filter(c -> c.getColor().equals(value.get(0)));
        if (value.size() > 1) {
            Stream<Product> color_size = color
                    .filter(c -> c.getSize().equals(value.get(1)));
            productList = color_size.collect(Collectors.toList());
        } else {
            productList = color.collect(Collectors.toList());
        }
        setProducts();
        return productList;
    }

    public void getProduct(String query) {
        CompletableFuture.runAsync(() -> products.postValue(productDao.searchArticle("%" + query + "%")));
    }

    public MutableLiveData<List<Product>> getProducts() {
        return products;
    }

    private void setProducts() {
        CompletableFuture.runAsync(() -> products.postValue(productList));
    }

    public void updateFirebaseProduct(Product product) {
        //Update firebase and sqlite manual
        FirebasaDatabaseHelper.getInstance().updateProduct(product, success -> {
            if (success) {
                CompletableFuture.runAsync(() -> productDao.update(new ProductAddress(product.getId(),
                        product.getAddress())))
                        .thenAccept((res) -> getProduct(product.getArticle()));
            }
        });
    }

}
