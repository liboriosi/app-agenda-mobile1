package com.mobile.tabalho.gerenciador_contatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;

public class ListarFragment extends Fragment {

    private ContatoAdapter contatoAdapter;

    public ListarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar, container, false);

        RecyclerView recyclerViewContatos = view.findViewById(R.id.recyclerViewContatos);
        recyclerViewContatos.setLayoutManager(new LinearLayoutManager(getContext()));

        contatoAdapter = new ContatoAdapter();
        recyclerViewContatos.setAdapter(contatoAdapter);

        carregarContatos();

        return view;
    }

    private void carregarContatos() {
        List<Contato> contatos;
        try (DatabaseHelper databaseHelper = new DatabaseHelper(getContext())) {
            contatos = databaseHelper.getAllContatos();
        }

        contatos.sort(new Comparator<Contato>() {
            @Override
            public int compare(Contato contato1, Contato contato2) {
                return contato1.getNome().compareToIgnoreCase(contato2.getNome());
            }
        });

        contatoAdapter.setContatos(contatos);
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarContatos();
    }
}