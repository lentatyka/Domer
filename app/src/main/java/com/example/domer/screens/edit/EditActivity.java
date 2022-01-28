package com.example.domer.screens.edit;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.domer.database.Product;
import com.example.domer.databinding.ActivityEditBinding;
import com.example.domer.databinding.ToolbarBinding;
import com.example.domer.screens.edit.fragments.start.ProductFragment;

import java.util.Arrays;
import java.util.List;

public class EditActivity extends AppCompatActivity implements ProductFragment.ProductListener {

    public static final String PRODUCT = "PRODUCT";
    ActivityEditBinding binding;
    ToolbarBinding toolbar;
    EditProductViewModel viewModel;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        toolbar = binding.editToolbar;

        product = (Product)getIntent().getSerializableExtra(PRODUCT);
        setTitle(product.getArticle());
        viewModel = new ViewModelProvider(this).get(EditProductViewModel.class);
        //Split addresses by ":"
        String[] tokens = product.getAddress().split(":");
        List<String> list = Arrays.asList(tokens);
        viewModel.setAddress(list);
        setSupportActionBar(toolbar.getRoot());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void accept() {
        //Save
        List<String> value = viewModel.getAddress().getValue();
        assert value != null;
        String address = String.join(":", value);
        product.setAddress(address);
        Intent intent = new Intent();
        intent.putExtra(PRODUCT, product);
        setResult(RESULT_OK, intent);
        finish();
    }

}