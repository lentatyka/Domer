package com.example.domer.screens.main.searchscreen;

import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domer.R;
import com.example.domer.database.Product;

import java.util.List;
import java.util.function.Consumer;

public class SearchAdapter extends ListAdapter<Product, SearchAdapter.ProductHolder> {
    private final Consumer<Product> callback;
    private List<Product> list;

    enum Colors{
        ANTHRACITE(55, 62, 67), BEIGE(242, 231, 191), BLACK(0, 0, 0),
        BLUE(0, 0, 255), BROWN(152, 108, 71), BURGUNDY(142, 49, 85),
        DARK_BLUE(0, 36, 156), DARK_BROWN(70, 45, 20), FUCHSIA(255, 0, 255),
        GREEN(0, 255, 0), GREY(128, 128, 128), LIGHT_BLUE(195, 228, 232),
        LIGHT_BROWN(163, 111, 64), LIGHT_GREEN(165, 214, 16),
        LIGHT_GREY(136, 142, 140), LIGHT_LILAC(224, 199, 210),
        LIGHT_PINK(250, 180, 216), LILAC(166, 147, 188),
        MINT_GREEN(181, 222, 207), ORANGE(252, 147, 3), PINK(255, 170, 170),
        SALMON(242, 193, 159), SAX(62, 95, 151), SKY_BLUE(142, 193, 233),
        TURQUOISE(6, 184, 185), YELLOW(252, 233, 3);
        private final int val;

        Colors(int r, int g, int b){
            this.val = Color.rgb(r, g, b);
        }
        public int getVal() {
            return val;
        }
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };
    public SearchAdapter(Consumer<Product> callback, List<Product> list) {
        super(DIFF_CALLBACK);
        this.callback = callback;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    protected Product getItem(int position) {
        return list.get(position);
    }

    public void setList(List<Product> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductHolder(itemView, callback);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ProductHolder holder, int position) {
        Product product = getItem(position);
        holder.article.setText(product.getArticle());
        holder.color.setText(product.getColor());
        if(product.getColor() != null){
            try {
                holder.color.setTextColor(
                        Colors.valueOf(product.getColor()).getVal()
                );
            }catch (IllegalArgumentException e){
                holder.color.setTextColor(Color.BLACK);
            }
        }

        holder.size.setText(product.getSize());
        String[] tokens = product.getAddress().split(":");
        StringBuilder address = new StringBuilder();
        for(String str : tokens){
            address.append(str);
            address.append("\n");
        }
        holder.address.setText(address.toString().trim());

    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView article;
        private final TextView color;
        private final TextView size;
        private final TextView address;
        private final Consumer<Product> ownerCallback;

        public ProductHolder(@NonNull View itemView, Consumer<Product> ownerCallback) {
            super(itemView);

            this.ownerCallback = ownerCallback;
            article = itemView.findViewById(R.id.item_product_name);
            color = itemView.findViewById(R.id.item_product_color);
            size = itemView.findViewById(R.id.item_product_size);
            address = itemView.findViewById(R.id.item_product_address);
            ImageButton button = itemView.findViewById(R.id.edit_button);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(ownerCallback != null){
                ownerCallback.accept(getItem(getAdapterPosition()));
            }
        }
    }
}
