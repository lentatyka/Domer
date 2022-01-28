package com.example.domer.screens.main.searchscreen;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.domer.R;
import com.example.domer.database.Product;
import com.example.domer.databinding.FragmentSearchBinding;
import com.example.domer.interfaces.DialogListener;
import com.example.domer.screens.edit.EditActivity;
import com.example.domer.screens.main.SharedViewModel;
import com.example.domer.utilits.AcceptDialog;
import com.example.domer.utilits.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SearchFragment extends Fragment implements Consumer<Product> {

    private FragmentSearchBinding binding;
    private Observer<List<Product>> productObserver;
    private Observer<String> screenObserver;
    private SharedViewModel sharedViewModel;
    private SearchViewModel searchViewModel;
    private RecyclerView recyclerView;
    private TextView clearText;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        clearText = binding.clearText;
        recyclerView = binding.searchRecycleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchViewModel.getProducts().removeObserver(productObserver);
        sharedViewModel.getScreen().removeObserver(screenObserver);
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.btn_help).setVisible(true);
        menu.findItem(R.id.btn_map).setVisible(true);
        menu.findItem(R.id.search).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.btn_map) {
            sharedViewModel.setScreen(Constants.KW_MAP);
        } else if (itemId == R.id.btn_help) {
            sharedViewModel.setScreen(Constants.KW_HELP);
        }
        return super.onOptionsItemSelected(item);
    }


    private void init() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        SearchAdapter searchAdapter = new SearchAdapter(this, Collections.emptyList());
        recyclerView.setAdapter(searchAdapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        productObserver = list -> {
            if(list.size() == 0)
                clearText.setVisibility(View.VISIBLE);
            else
                clearText.setVisibility(View.GONE);
            searchAdapter.setList(list);
        };
        screenObserver = value -> {
            if(value == null){
                return;
            }
                switch (value){
                    case Constants.KW_HELP:{
                        if(navController.getCurrentDestination().getId() == R.id.searchFragment){
                            navController.navigate(R.id.searchFrag_to_helpFrag);
                        }
                        break;
                    }
                    case  Constants.KW_MAP:
                        if(navController.getCurrentDestination().getId() == R.id.searchFragment)
                            navController.navigate(R.id.searchFrag_to_mapFrag);
                        break;
                    default:{
                        searchViewModel.acceptResult(value);
                    }
                }
        };
        searchViewModel.getProducts().observe(getViewLifecycleOwner(), productObserver);
        sharedViewModel.getScreen().observe(getViewLifecycleOwner(), screenObserver);
    }

    @Override
    public void accept(Product product) {
        sharedViewModel.setScreen(null);
        Intent intent = new Intent(requireActivity(), EditActivity.class);
        intent.putExtra(EditActivity.PRODUCT, product);
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result)->{
            if(result.getResultCode() ==Activity.RESULT_OK){
                Intent data = result.getData();
                assert data != null;
                Product answer = (Product)data.getSerializableExtra(EditActivity.PRODUCT);
                searchViewModel.updateFirebaseProduct(answer);
            }
        }).launch(intent);
    }
}