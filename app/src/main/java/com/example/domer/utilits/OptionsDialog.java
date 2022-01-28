package com.example.domer.utilits;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.domer.R;
import com.example.domer.interfaces.DialogListener;

import org.jetbrains.annotations.NotNull;

public class OptionsDialog extends DialogFragment {

    DialogListener listener;
    boolean[] checkedList;


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the OptionDialogListener so we can send events to the host
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement OptionsDialogListener");
        }
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        checkedList = new boolean[2];
        checkedList[0] = Preferences.SHARED_PREFERENCES.getSENSOR();
        checkedList[1] = Preferences.SHARED_PREFERENCES.getSOUND();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.options)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.toppings, checkedList,
                        (dialog, which, isChecked) -> checkedList[which] = isChecked)
                // Set the action buttons
                .setPositiveButton(R.string.ok, (dialog, id) -> listener.onDialogPositiveClick(OptionsDialog.this))
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    //ignore
                });

        return builder.create();
    }

    public boolean[] getSelectedItems(){
        return checkedList;
    }

}
