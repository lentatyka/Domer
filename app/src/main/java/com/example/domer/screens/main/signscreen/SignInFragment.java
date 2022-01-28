package com.example.domer.screens.main.signscreen;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.domer.R;
import com.example.domer.databinding.FragmentSignInBinding;
import com.example.domer.databinding.ProgressBarButtonBinding;
import com.example.domer.utilits.Preferences;
import com.example.domer.utilits.ProgressBarButton;
import com.google.firebase.auth.FirebaseAuth;


public class SignInFragment extends Fragment {

    FragmentSignInBinding binding;
    EditText email, passwords;
    FirebaseAuth mAuth;
    Auth callback;

    public interface Auth {
        void accept();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        if(!Preferences.SHARED_PREFERENCES.getInitUser()) {
            init();
        }
        else
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_signInFragment_to_helpFragment);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (Auth) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }

    }

    private void init() {
        email = binding.loginEmail;
        passwords = binding.loginPassword;
        ProgressBarButtonBinding loginButton = binding.loginButton;
        ConstraintLayout root = loginButton.getRoot();
        root.setOnClickListener(v -> {
            final ProgressBarButton pbb = new ProgressBarButton(getContext(), v);
            String mail = email.getText().toString();
            String pass = passwords.getText().toString();
            if(mail.isEmpty())
                email.setError("введите логин");
            if(pass.isEmpty())
                passwords.setError("введите пароль");
            if(!mail.isEmpty() && !pass.isEmpty()){
                pbb.buttonActivated();
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(authResult -> {
                    pbb.buttonFinished(R.string.success);
                    Preferences.SHARED_PREFERENCES.setInitUser(true);
                    callback.accept();
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.action_signInFragment_to_helpFragment);
                }).addOnFailureListener(e ->{
                    pbb.buttonFinished(R.string.failed);
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });
    }
}