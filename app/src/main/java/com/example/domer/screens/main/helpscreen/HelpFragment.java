package com.example.domer.screens.main.helpscreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.domer.R;
import com.example.domer.databinding.FragmentHelpBinding;
import com.example.domer.screens.main.SharedViewModel;
import com.example.domer.utilits.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class HelpFragment extends Fragment implements Observer<String> {

    @Nullable
    private FragmentHelpBinding binding;
    private SharedViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentHelpBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onDestroyView() {
        viewModel.getScreen().removeObserver(this);
        super.onDestroyView();
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
        menu.findItem(R.id.btn_map).setVisible(true);
        menu.findItem(R.id.btn_search).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.btn_map)
            viewModel.setScreen(Constants.KW_MAP);
        else if (itemId == R.id.btn_search)
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.helpFrag_to_searchFrag);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(String value) {
        if (value == null) {
            return;
        }

        switch (value) {
            case Constants.KW_HELP: {
                viewModel.setScreen(null);
                break;
            }
            case Constants.KW_MAP:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.helpFrag_to_mapFrag);
                break;
            default: {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.helpFrag_to_searchFrag);
            }
        }
    }

    private void init() {
        List<String> collection = Arrays.asList(getResources().getStringArray(R.array.helper));
        HelpAdapter helpAdapter = new HelpAdapter(collection);
        assert binding != null;
        RecyclerView recyclerView = binding.helpItemRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(helpAdapter);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getScreen().observe(getViewLifecycleOwner(), this);
    }
}