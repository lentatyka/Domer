package com.example.domer.screens.main.searchscreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.domer.database.Product;
import com.example.domer.database.ProductRepository;
import com.example.domer.utilits.Preferences;
import com.example.domer.utilits.engine.Engine;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SearchViewModel extends AndroidViewModel {
    MutableLiveData<List<Product>> products;
    MutableLiveData<String[]> textSpeech;
    ProductRepository repository;
    Engine engine;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    //from repository
    public void setProducts(String article) {
        repository.getProduct(article);
    }

    public void updateFirebaseProduct(Product product){
        repository.updateFirebaseProduct(product);
    }

    private List<Product> searchArticle(List<String> article) {
        try {
             return repository.searchArticle(article.get(0)).get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private List<Product> searchSize(String value) {
        return repository.searchSize(value);
    }

    private List<Product> searchColorSize(List<String> value) {
        return repository.searchColorSize(value);
    }

    private void setTextSpeech(String[] apply) {
        CompletableFuture.runAsync(() ->
                textSpeech.postValue(apply));
    }

    public LiveData<String[]> getTextSpeech() {
        return textSpeech;
    }


    public void acceptResult(String value) {
        engine.accept(value);
    }

    //init data at 1st run
    private void init() {
        if (products == null) {
            repository = new ProductRepository(getApplication());
            products = repository.getProducts();
        }
        if (textSpeech == null)
            textSpeech = new MutableLiveData<>();
        //Engine///
        engine = new Engine();
        Engine.MyBinaryOperator<List<String>, String> caller = (result, state) -> {

            List<Product> product = null;
            if (result != null) {
                switch (state) {
                    case Engine.ARTICLE_REQ:
                        product = searchArticle(result);
                        break;
                    case Engine.COLOR_SIZE_REQ:
                        product = searchColorSize(result);
                        break;
                    case Engine.SIZE_REQ:
                        product = searchSize(result.get(0));
                }
            }
            if (Preferences.SHARED_PREFERENCES.getSOUND())
                setTextSpeech(engine.apply(product));

        };

        engine.setCaller(caller);
    }

    @Override
    protected void onCleared() {
        engine.destroy();
        engine = null;
    }
}
