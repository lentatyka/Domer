package com.example.domer.screens.edit.fragments.start;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.EditProduct> {

    private List<String> data;
    private final Consumer<Integer> callback;

    ProductAdapter(Consumer<Integer> callback){
        this.data = new ArrayList<>();
        this.callback = callback;
    }


    @NonNull
    @Override
    public EditProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        row = inflater.inflate(R.layout.edit_product_item, parent, false);
        return new EditProduct(row);
    }

    @Override
    public void onBindViewHolder(@NonNull EditProduct holder, int position) {
        //Check holder instance to populate data  according to it
        //set data
        holder.address.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<String> data){
        this.data = data;
        notifyDataSetChanged();
    }


    class EditProduct extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView address;

        public EditProduct(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.item_edit_address);
            Button deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(EditProduct.this);
        }


        @Override
        public void onClick(View v) {
            int position = EditProduct.this.getAdapterPosition();
            if (callback != null && position != RecyclerView.NO_POSITION) {
                callback.accept(position);
            }
        }
    }
}
