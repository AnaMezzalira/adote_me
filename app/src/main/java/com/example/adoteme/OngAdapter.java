package com.example.adoteme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OngAdapter extends RecyclerView.Adapter<OngAdapter.ViewHolder> {

    private List<Ong> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Ong ong);
    }

    public OngAdapter(List<Ong> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OngAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new OngAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OngAdapter.ViewHolder holder, int position) {
        Ong ong = lista.get(position);
        holder.textView.setText(ong.getNome());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(ong));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void atualizarLista(List<Ong> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
