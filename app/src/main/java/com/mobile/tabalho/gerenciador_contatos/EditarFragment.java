package com.mobile.tabalho.gerenciador_contatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.List;

public class EditarFragment extends Fragment {

    public EditarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar, container, false);

        Bundle args = getArguments();
        if (args != null) {
            long contatoId = args.getLong("contatoId", -1);
            if (contatoId != -1) {
                Contato contato = getContatoFromDatabase(contatoId);
            }
        }

        if (args != null && args.containsKey("contatoId")) {
            long contatoId = args.getLong("contatoId");

            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            Contato contato = databaseHelper.getContatoById(contatoId);

            if (contato != null) {
                List<Numero> numeros = contato.getNumeros();

                System.out.println("Contato: " + contato.getNome());

                if (numeros != null) {
                    System.out.println("Números:");

                    for (Numero numero : numeros) {
                        System.out.println("Número: " + numero.getNumero() + ", Tipo: " + numero.getTipo());
                    }
                } else {
                    System.out.println("O contato não possui números.");
                }
            } else {
                System.out.println("O contato não foi encontrado.");
            }

            databaseHelper.close();
        }


        return view;
    }

    private Contato getContatoFromDatabase(long contatoId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        Contato contato = databaseHelper.getContatoById(contatoId);
        databaseHelper.close();
        return contato;
    }
}