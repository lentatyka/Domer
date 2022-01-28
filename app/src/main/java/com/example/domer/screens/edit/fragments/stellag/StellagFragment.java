package com.example.domer.screens.edit.fragments.stellag;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.domer.R;
import com.example.domer.databinding.FragmentStellagBinding;
import com.example.domer.screens.edit.EditProductViewModel;


public class StellagFragment extends Fragment {

    FragmentStellagBinding binding;
    RadioGroup sklad, stellag, vert, hor;
    Button back, save;
    EditProductViewModel viewModel;
    ConstraintLayout root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentStellagBinding.inflate(getLayoutInflater(), container, false);
        sklad = binding.skladRg;
        stellag = binding.stellagRg;
        vert = binding.verticalRg;
        hor = binding.horizontalRg;
        back = binding.btnBackward;
        save = binding.btnSave;
        root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        for(int i=0; i<10; i++){
            stellag.addView(createRadioButton(i+1));
            vert.addView(createRadioButton(i+1));
            hor.addView(createRadioButton(i+1));
        }

        back.setOnClickListener(v -> Navigation
                .findNavController(requireActivity(), R.id.edit_host_fragment)
                .navigate(R.id.action_stellagFragment_to_productFragment));

        save.setOnClickListener(v -> {
            int sklad_id = sklad.getCheckedRadioButtonId();
            int stellag_id = stellag.getCheckedRadioButtonId();
            int vert_id = vert.getCheckedRadioButtonId();
            int hort_id = hor.getCheckedRadioButtonId();
            if(sklad_id == -1 || stellag_id == -1 || vert_id == -1 || hort_id == -1)
                Toast.makeText(getContext(), "Неверный адрес. Есть неотмеченные поля",
                        Toast.LENGTH_SHORT).show();
            else{
                RadioButton sklad_rb = root.findViewById(sklad_id);
                RadioButton stellag_rb = root.findViewById(stellag_id);
                RadioButton vert_rb = root.findViewById(vert_id);
                RadioButton hor_rb = root.findViewById(hort_id);

                String builder = sklad_rb.getText() +
                        "-" +
                        stellag_rb.getText() +
                        "-" +
                        vert_rb.getText() +
                        "-" +
                        hor_rb.getText();
                viewModel.addPosition(builder);
                Navigation.findNavController(requireActivity(), R.id.edit_host_fragment)
                        .navigate(R.id.action_stellagFragment_to_productFragment);
            }
        });

        viewModel = new ViewModelProvider(requireActivity()).get(EditProductViewModel.class);
    }

    private RadioButton createRadioButton(int val) {
        RadioButton rb = new RadioButton(getContext());
        String s = Integer.toString(val);
        rb.setText(s);
        rb.setTextSize(20);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 4);
        rb.setLayoutParams(params);
        return rb;
    }
}