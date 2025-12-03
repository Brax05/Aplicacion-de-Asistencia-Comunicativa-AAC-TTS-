package com.example.txttovoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrasesAdapter extends RecyclerView.Adapter<FrasesAdapter.ViewHolder> {

    private List<String> frases;
    private List<Boolean> showDeleteIcon;
    private FraseClickListener clickListener;
    private FraseLongPressListener longPressListener;
    private FraseEliminarListener eliminarListener;

    public FrasesAdapter(List<String> frases,
                         FraseClickListener clickListener,
                         FraseLongPressListener longPressListener,
                         FraseEliminarListener eliminarListener) {
        this.frases = frases;
        this.clickListener = clickListener;
        this.longPressListener = longPressListener;
        this.eliminarListener = eliminarListener;
        this.showDeleteIcon = new ArrayList<>();
        for (int i = 0; i < frases.size(); i++) showDeleteIcon.add(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frases_button, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String frase = frases.get(position);
        holder.button.setText(frase);

        holder.btnEliminar.setVisibility(showDeleteIcon.get(position) ? View.VISIBLE : View.GONE);

        holder.button.setOnClickListener(v -> clickListener.onFraseClick(frase));
        holder.button.setOnLongClickListener(v -> {
            for (int i = 0; i < showDeleteIcon.size(); i++)
                showDeleteIcon.set(i, i == position);
            notifyDataSetChanged();
            longPressListener.onFraseLongPressed(frase);

            eliminarListener.onEliminarClick(position);

            return true;
        });
        holder.btnEliminar.setOnClickListener(v -> eliminarListener.onEliminarClick(position));
    }

    @Override
    public int getItemCount() {
        return frases.size();
    }

    public void removeItem(int position) {
        frases.remove(position);
        showDeleteIcon.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void addItem(String frase) {
        frases.add(frase);
        showDeleteIcon.add(false);
        notifyItemInserted(frases.size() - 1);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void swapItems(int from, int to) {
        Collections.swap(frases, from, to);
        Collections.swap(showDeleteIcon, from, to);
        notifyItemMoved(from, to);
        notifyItemRangeChanged(0, getItemCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        ImageButton btnEliminar;

        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btnElemento);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
