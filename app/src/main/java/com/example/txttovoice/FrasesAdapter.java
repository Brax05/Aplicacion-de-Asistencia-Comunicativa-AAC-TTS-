package com.example.txttovoice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FrasesAdapter extends RecyclerView.Adapter<FrasesAdapter.ViewHolder> {

    private List<String> frases;
    private List<Boolean> showDeleteIcon;
    private FraseClickListener clickListener;
    private FraseLongPressListener longPressListener;
    private FraseEliminarListener eliminarListener;
    private Context context;

    public FrasesAdapter(List<String> frases,
                         FraseClickListener clickListener,
                         FraseLongPressListener longPressListener,
                         FraseEliminarListener eliminarListener,
                         Context context) {
        this.context = context;
        this.clickListener = clickListener;
        this.longPressListener = longPressListener;
        this.eliminarListener = eliminarListener;
        this.frases = new ArrayList<>(frases);
        ordenarFavoritosPrimero();
        this.showDeleteIcon = new ArrayList<>();
        for (int i = 0; i < this.frases.size(); i++) showDeleteIcon.add(false);
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
        holder.button.setContentDescription("Frase: " + frase);

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

        SharedPreferences favsPrefs = context.getSharedPreferences("FavoritoFrases", Context.MODE_PRIVATE);
        Set<String> favoritos = favsPrefs.getStringSet("favoritos", new HashSet<>());
        boolean esFavorito = favoritos.contains(frase);
        // Lógica inversa: favoritos = vacía, no favoritos = llena
        if (esFavorito) {
            holder.btnFavorito.setImageResource(R.drawable.ic_star_border);
        } else {
            holder.btnFavorito.setImageResource(R.drawable.ic_star);
        }

        holder.btnFavorito.setOnClickListener(v -> {
            Vibrator vib = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vib != null) vib.vibrate(80);

            SharedPreferences.Editor editor = favsPrefs.edit();
            Set<String> nuevosFavoritos = new HashSet<>(favsPrefs.getStringSet("favoritos", new HashSet<>()));

            if (nuevosFavoritos.contains(frase)) {
                nuevosFavoritos.remove(frase);
                Toast.makeText(context, "Frase eliminada de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                nuevosFavoritos.add(frase);
                Toast.makeText(context, "Frase agregada a favoritos", Toast.LENGTH_SHORT).show();
            }
            editor.putStringSet("favoritos", nuevosFavoritos);
            editor.apply();

            ordenarFavoritosPrimero();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return frases.size();
    }

    public void removeItem(int position) {
        frases.remove(position);
        showDeleteIcon.remove(position);
        ordenarFavoritosPrimero();
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void addItem(String frase) {
        frases.add(frase);
        showDeleteIcon.add(false);
        ordenarFavoritosPrimero();
        notifyItemInserted(frases.size() - 1);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void swapItems(int from, int to) {
        Collections.swap(frases, from, to);
        Collections.swap(showDeleteIcon, from, to);
        ordenarFavoritosPrimero();
        notifyItemMoved(from, to);
        notifyItemRangeChanged(0, getItemCount());
    }

    // Ordenar siempre favoritos arriba
    private void ordenarFavoritosPrimero() {
        SharedPreferences favsPrefs = context.getSharedPreferences("FavoritoFrases", Context.MODE_PRIVATE);
        Set<String> favoritos = favsPrefs.getStringSet("favoritos", new HashSet<>());
        List<String> favoritosList = new ArrayList<>();
        List<String> noFavoritosList = new ArrayList<>();
        for (String frase : frases) {
            if (favoritos.contains(frase)) {
                favoritosList.add(frase);
            } else {
                noFavoritosList.add(frase);
            }
        }
        frases.clear();
        frases.addAll(favoritosList);
        frases.addAll(noFavoritosList);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        ImageButton btnEliminar;
        ImageButton btnFavorito;

        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btnElemento);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnFavorito = itemView.findViewById(R.id.btnFavorito);
        }
    }
}
