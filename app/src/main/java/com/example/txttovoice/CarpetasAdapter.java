package com.example.txttovoice;

import android.content.Context;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarpetasAdapter extends RecyclerView.Adapter<CarpetasAdapter.ViewHolder> {

    private List<String> carpetas;
    private List<Boolean> showDeleteIcon;
    private CarpetaClickListener clickListener;
    private CarpetaLongPressListener longPressListener;
    private CarpetaEliminarListener eliminarListener;
    private Context context;

    public CarpetasAdapter(List<String> carpetas,
                           CarpetaClickListener clickListener,
                           CarpetaLongPressListener longPressListener,
                           CarpetaEliminarListener eliminarListener,
                           Context context) {
        this.context = context;
        this.carpetas = new ArrayList<>(carpetas);
        this.clickListener = clickListener;
        this.longPressListener = longPressListener;
        this.eliminarListener = eliminarListener;

        ordenarFavoritosPrimero();

        this.showDeleteIcon = new ArrayList<>();
        for (int i = 0; i < this.carpetas.size(); i++) showDeleteIcon.add(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carpetas_button, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarpetasAdapter.ViewHolder holder, int position) {
        String carpeta = carpetas.get(position);
        holder.button.setText(carpeta);

        // Fondo personalizado según color guardado
        SharedPreferences prefs = holder.button.getContext().getSharedPreferences("CarpetasColores", Context.MODE_PRIVATE);
        int defaultColor = holder.button.getContext().getResources().getColor(R.color.carpeta_color_azul);
        int color = prefs.getInt(carpeta, defaultColor);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(18);
        holder.button.setBackground(drawable);
        holder.button.setTextColor(0xFF222222);

        holder.btnEliminar.setVisibility(showDeleteIcon.get(position) ? View.VISIBLE : View.GONE);

        holder.button.setOnClickListener(v -> clickListener.onCarpetaClick(carpeta));

        holder.button.setOnLongClickListener(v -> {
            for (int i = 0; i < showDeleteIcon.size(); i++) showDeleteIcon.set(i, i == position);
            notifyDataSetChanged();
            longPressListener.onCarpetaLongPressed(carpeta);
            eliminarListener.onEliminarClick(position);
            return true;
        });

        holder.btnEliminar.setOnClickListener(v -> eliminarListener.onEliminarClick(position));

        // FAVORITOS Carpeta - lógica inversa (llena = no favorito, vacía = favorito)
        SharedPreferences favPrefs = context.getSharedPreferences("FavoritoCarpetas", Context.MODE_PRIVATE);
        Set<String> favoritos = favPrefs.getStringSet("favoritosCarpetas", new HashSet<>());
        boolean esFavorita = favoritos.contains(carpeta);

        if (esFavorita) {
            holder.btnFavoritoCarpeta.setImageResource(R.drawable.ic_star_border);
        } else {
            holder.btnFavoritoCarpeta.setImageResource(R.drawable.ic_star);
        }

        holder.btnFavoritoCarpeta.setOnClickListener(v -> {
            SharedPreferences.Editor editor = favPrefs.edit();
            Set<String> nuevosFavoritos = new HashSet<>(favPrefs.getStringSet("favoritosCarpetas", new HashSet<>()));

            if (nuevosFavoritos.contains(carpeta)) {
                nuevosFavoritos.remove(carpeta);
                holder.btnFavoritoCarpeta.setImageResource(R.drawable.ic_star);
                Toast.makeText(context, "Carpeta eliminada de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                nuevosFavoritos.add(carpeta);
                holder.btnFavoritoCarpeta.setImageResource(R.drawable.ic_star_border);
                Toast.makeText(context, "Carpeta agregada a favoritos", Toast.LENGTH_SHORT).show();
            }
            editor.putStringSet("favoritosCarpetas", nuevosFavoritos);
            editor.apply();

            ordenarFavoritosPrimero();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return carpetas.size();
    }

    public void removeItem(int position) {
        carpetas.remove(position);
        showDeleteIcon.remove(position);
        ordenarFavoritosPrimero();
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void addItem(String carpeta) {
        carpetas.add(carpeta);
        showDeleteIcon.add(false);
        ordenarFavoritosPrimero();
        notifyItemInserted(carpetas.size() - 1);
        notifyItemRangeChanged(0, getItemCount());
    }

    // Ordenar favoritos arriba
    private void ordenarFavoritosPrimero() {
        SharedPreferences favPrefs = context.getSharedPreferences("FavoritoCarpetas", Context.MODE_PRIVATE);
        Set<String> favoritos = favPrefs.getStringSet("favoritosCarpetas", new HashSet<>());
        List<String> favoritasList = new ArrayList<>();
        List<String> noFavoritasList = new ArrayList<>();
        for (String carpeta : carpetas) {
            if (favoritos.contains(carpeta)) {
                favoritasList.add(carpeta);
            } else {
                noFavoritasList.add(carpeta);
            }
        }
        carpetas.clear();
        carpetas.addAll(favoritasList);
        carpetas.addAll(noFavoritasList);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatButton button;
        ImageButton btnEliminar;
        ImageButton btnFavoritoCarpeta;
        ViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.btnElemento);
            btnEliminar = v.findViewById(R.id.btnEliminar);
            btnFavoritoCarpeta = v.findViewById(R.id.btnFavoritoCarpeta);
        }
    }
}
