package com.example.txttovoice;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;

public class CarpetasAdapter extends RecyclerView.Adapter<CarpetasAdapter.ViewHolder> {

    private List<String> carpetas;
    private List<Boolean> showDeleteIcon;
    private CarpetaClickListener clickListener;
    private CarpetaLongPressListener longPressListener;
    private CarpetaEliminarListener eliminarListener;

    public CarpetasAdapter(List<String> carpetas,
                           CarpetaClickListener clickListener,
                           CarpetaLongPressListener longPressListener,
                           CarpetaEliminarListener eliminarListener) {
        this.carpetas = carpetas;
        this.clickListener = clickListener;
        this.longPressListener = longPressListener;
        this.eliminarListener = eliminarListener;
        this.showDeleteIcon = new ArrayList<>();
        for (int i = 0; i < carpetas.size(); i++) showDeleteIcon.add(false);
    }

    @NonNull
    @Override
    public CarpetasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carpetas_button, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarpetasAdapter.ViewHolder holder, int position) {
        String carpeta = carpetas.get(position);
        holder.button.setText(carpeta);

        // Fondo personalizado según color guardado
        SharedPreferences prefs = holder.button.getContext().getSharedPreferences("CarpetasColores", android.content.Context.MODE_PRIVATE);
        int defaultColor = holder.button.getContext().getResources().getColor(R.color.carpeta_color_azul);
        int color = prefs.getInt(carpeta, defaultColor);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(18);
        holder.button.setBackground(drawable);
        holder.button.setTextColor(0xFF222222);

        holder.btnEliminar.setVisibility(showDeleteIcon.get(position) ? View.VISIBLE : View.GONE);

        holder.button.setOnClickListener(v -> clickListener.onCarpetaClick(carpeta));

        // Mantener pulsado para eliminar carpeta
        holder.button.setOnLongClickListener(v -> {
            for (int i = 0; i < showDeleteIcon.size(); i++) showDeleteIcon.set(i, i == position);
            notifyDataSetChanged();
            longPressListener.onCarpetaLongPressed(carpeta);

            eliminarListener.onEliminarClick(position);

            return true;
        });

        holder.btnEliminar.setOnClickListener(v -> eliminarListener.onEliminarClick(position));
    }

    @Override
    public int getItemCount() {
        return carpetas.size();
    }

    public void removeItem(int position) {
        carpetas.remove(position);
        showDeleteIcon.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void addItem(String carpeta) {
        carpetas.add(carpeta);
        showDeleteIcon.add(false);
        notifyItemInserted(carpetas.size() - 1);
        notifyItemRangeChanged(0, getItemCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatButton button;
        ImageButton btnEliminar;

        ViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.btnElemento);
            btnEliminar = v.findViewById(R.id.btnEliminar);
        }
    }
}
