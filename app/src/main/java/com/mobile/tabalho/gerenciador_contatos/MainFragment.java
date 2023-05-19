package com.mobile.tabalho.gerenciador_contatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContatos, new ListarFragment()).commit();
        }

        ImageButton btnAdicionar = v.findViewById(R.id.buttonAdicionar);
        btnAdicionar.setOnClickListener(v1 -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AdicionarFragment()).commit());

        return v;
    }
}