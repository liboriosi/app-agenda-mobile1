package com.mobile.tabalho.gerenciador_contatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListarFragment extends Fragment implements ContatoAdapter.OnItemClickListener {

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
        contatoAdapter.setOnItemClickListener(this);

        carregarContatos();

        return view;
    }

    private void carregarContatos() {
        List<Contato> contatos;
        try (DatabaseHelper databaseHelper = new DatabaseHelper(getContext())) {
            contatos = databaseHelper.getAllContatos();
        }

        contatos.sort((contato1, contato2) -> contato1.getNome().compareToIgnoreCase(contato2.getNome()));

        contatoAdapter.setContatos(contatos);
    }

    @Override
    public void onItemClick(Contato contato) {
        Fragment editarFragment = new EditarFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("contatoId", contato.getId());
        editarFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, editarFragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarContatos();
    }
}