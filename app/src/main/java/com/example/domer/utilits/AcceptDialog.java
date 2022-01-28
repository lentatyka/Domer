package com.example.domer.utilits;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.domer.R;
import com.example.domer.database.Product;
import com.example.domer.interfaces.DialogListener;

import org.jetbrains.annotations.NotNull;

public class AcceptDialog extends DialogFragment {
    private final Product product;
    private final DialogListener listener;

    public AcceptDialog(@NonNull Product product, DialogListener listener) {
        this.product = product;
        this.listener = listener;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        StringBuilder title = new StringBuilder();
        title.append(product.getArticle());
        if(product.getColor()!=null){
            title.append("->");
            title.append(product.getColor());
        }
        builder.setTitle(title.toString())
                .setPositiveButton(R.string.ok, (dialog, id) -> {

                    listener.onDialogPositiveClick(AcceptDialog.this);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    //ignore
                });

        return builder.create();
    }

    public Product getProduct(){
        return product;
    }

}

