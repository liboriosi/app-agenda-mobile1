package com.mobile.tabalho.gerenciador_contatos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private List<Contato> contatos = new ArrayList<>();
    private OnItemClickListener listener;

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contato_item, parent, false);
        return new ContatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatoViewHolder holder, int position) {
        Contato contato = contatos.get(position);
        holder.tvNome.setText(contato.getNome());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(contato);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public static class ContatoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome;

        public ContatoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvListContatoNome);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Contato contato);
    }
}

