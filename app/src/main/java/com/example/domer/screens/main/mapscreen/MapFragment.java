package com.example.domer.screens.main.mapscreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.domer.R;
import com.example.domer.screens.main.SharedViewModel;
import com.example.domer.utilits.Constants;

public class MapFragment extends Fragment implements Observer<String> {

    SharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
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
        menu.findItem(R.id.btn_search).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.btn_help)
            viewModel.setScreen(Constants.KW_HELP);
        else if(itemId == R.id.btn_search)
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.mapFrag_to_searchFrag);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(String value) {
        if(value == null)
            return;
        switch (value){
            case Constants.KW_HELP:{
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.mapFrag_to_helpFrag);
                break;
            }
            case  Constants.KW_MAP:
                viewModel.setScreen(null);
                break;
            default: {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.mapFrag_to_searchFrag);
                break;
            }
        }
    }

    private void init() {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getScreen().observe(getViewLifecycleOwner(), this);
    }
}