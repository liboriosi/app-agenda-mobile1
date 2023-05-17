package com.mobile.tabalho.gerenciador_contatos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AdicionarFragment extends Fragment {

    private LinearLayout phoneContainer;
    private int phoneCount = 1;

    private DatabaseHelper databaseHelper;

    public AdicionarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);
        databaseHelper = new DatabaseHelper(getContext());

        phoneContainer = view.findViewById(R.id.phoneContainer);
        ImageButton addPhoneButton = view.findViewById(R.id.addPhoneButton);

        addPhoneButton.setOnClickListener(v -> addPhoneField());

        Button buttonAdicionar = view.findViewById(R.id.buttonAdicionar);
        buttonAdicionar.setOnClickListener(v -> salvarContato());

        return view;
    }

    private void addPhoneField() {
        @SuppressLint("InflateParams") View phoneFieldView = LayoutInflater.from(getContext()).inflate(R.layout.phone_field, null);

        Spinner phoneTypeSpinner = phoneFieldView.findViewById(R.id.phoneTypeSpinner);
        ImageButton removePhoneButton = phoneFieldView.findViewById(R.id.removePhoneButton);

        removePhoneButton.setOnClickListener(v -> removePhoneField(phoneFieldView));

        List<String> options = new ArrayList<>();
        options.add("Celular");
        options.add("Comercial");
        options.add("Residencial");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        phoneTypeSpinner.setAdapter(adapter);

        phoneContainer.addView(phoneFieldView);

        int childCount = phoneContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = phoneContainer.getChildAt(i);
            ImageButton childRemoveButton = childView.findViewById(R.id.removePhoneButton);
            childRemoveButton.setVisibility(childCount > 1 ? View.VISIBLE : View.GONE);
        }
    }

    private void removePhoneField(View phoneFieldView) {
        if (phoneContainer.getChildCount() > 0) {
            phoneContainer.removeView(phoneFieldView);
        }

        int childCount = phoneContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = phoneContainer.getChildAt(i);
            ImageButton childRemoveButton = childView.findViewById(R.id.removePhoneButton);
            childRemoveButton.setVisibility(childCount > 1 ? View.VISIBLE : View.GONE);
        }
    }

    private void salvarContato() {
        EditText fullNameEditText = getView().findViewById(R.id.nomeCompletoEditText);
        String nome = fullNameEditText.getText().toString();

        if (nome.trim().isEmpty()) {
            Toast.makeText(getContext(), "Digite um nome válido", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Numero> numeros = new ArrayList<>();

        int childCount = phoneContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = phoneContainer.getChildAt(i);
            Spinner phoneTypeSpinner = childView.findViewById(R.id.phoneTypeSpinner);
            EditText phoneNumberEditText = childView.findViewById(R.id.phoneNumberEditText);

            String tipo = phoneTypeSpinner.getSelectedItem().toString();
            String numero = phoneNumberEditText.getText().toString();

            if (!numero.trim().isEmpty()) {
                Numero numeroTelefone = new Numero(numero, tipo);
                numeros.add(numeroTelefone);
            }
        }

        if (numeros.isEmpty()) {
            Toast.makeText(getContext(), "Adicione pelo menos um número de telefone", Toast.LENGTH_SHORT).show();
            return;
        }

        Contato contato = new Contato(nome, numeros);

        long contatoId = databaseHelper.createContato(contato);

        if (contatoId != -1) {
            Toast.makeText(getContext(), "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
            fullNameEditText.setText("");
            phoneContainer.removeAllViews();
            addPhoneField();

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new MainFragment())
                    .commit();
        } else {
            Toast.makeText(getContext(), "Erro ao salvar o contato", Toast.LENGTH_SHORT).show();
        }
    }
}