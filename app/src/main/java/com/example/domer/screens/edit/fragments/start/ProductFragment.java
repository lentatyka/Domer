package com.example.domer.screens.edit.fragments.start;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domer.R;
import com.example.domer.databinding.FragmentProductBinding;
import com.example.domer.screens.edit.EditProductViewModel;

import java.util.function.Consumer;

public class ProductFragment extends Fragment implements Consumer<Integer> {
    FragmentProductBinding binding;
    EditProductViewModel viewModel;
    ImageButton newAddress;

    ProductListener callback;

    public interface ProductListener{
        void accept();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(getLayoutInflater(), container, false);
        newAddress = binding.editAddButton;
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btn_save && callback != null)
            callback.accept();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_action_menu, menu);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (ProductListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        ProductAdapter mAdapter = new ProductAdapter(this);
        viewModel = new ViewModelProvider(requireActivity()).get(EditProductViewModel.class);
        viewModel.getAddress().observe(getViewLifecycleOwner(), mAdapter::setData);
        RecyclerView editRecycleView = binding.editRecycleView;
        editRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        editRecycleView.setHasFixedSize(true);
        editRecycleView.setAdapter(mAdapter);

        newAddress.setOnClickListener(v -> {
            if(viewModel.getSize() >= 5)
                Toast.makeText(getContext(), "Максимум 5 адресов", Toast.LENGTH_SHORT).show();
            else
                Navigation.findNavController(requireActivity(), R.id.edit_host_fragment)
                .navigate(R.id.action_productFragment_to_stellagFragment);
        });
    }

    @Override
    public void accept(Integer pos) {
        if(viewModel.getSize() > 1){
            viewModel.removePosition(pos);
        }
        else
            Toast.makeText(getContext(), "Минимум 1 адрес", Toast.LENGTH_SHORT).show();
    }
}