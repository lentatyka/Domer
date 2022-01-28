package com.example.domer.screens.main.helpscreen;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domer.R;

import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelpHolder> {
    private final List<String> items;
    public HelpAdapter(List<String> items){
        this.items = items;
    }

    @NonNull
    @Override
    public HelpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_item, parent, false);
        return new HelpHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpHolder holder, int position) {
        String str = items.get(position);
        holder.textView.setText(str);
        if(str.charAt(0) == '*'){
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setTextColor(Color.rgb(233,79,55));
        }

        else{
            holder.textView.setGravity(Gravity.LEFT);
            if(position % 2 == 0)
                holder.textView.setTextColor(Color.rgb(119,159,161));
            else
                holder.textView.setTextColor(Color.rgb(65,67,97));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class HelpHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public HelpHolder(@NonNull View itemView) {
            super(itemView);
            textView =itemView.findViewById(R.id.help_item_text);
        }
    }
}
