package com.mobile.tabalho.gerenciador_contatos;

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
import java.util.Arrays;
import java.util.List;

public class EditarFragment extends Fragment {
    List<String> options = new ArrayList<>(Arrays.asList("Celular", "Comercial", "Residencial"));
    private EditText nomeCompletoEditText;
    private LinearLayout phoneContainer;
    private Contato contato;
    private List<Numero> numeros;

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

        nomeCompletoEditText = view.findViewById(R.id.nomeCompletoEditText);
        phoneContainer = view.findViewById(R.id.phoneContainer);
        ImageButton addPhoneButton = view.findViewById(R.id.addPhoneButton);
        Button atualizarContatoButton = view.findViewById(R.id.atualizarContatoButton);
        Button excluirContatoButton = view.findViewById(R.id.excluirContatoButton);

        addPhoneButton.setOnClickListener(v -> adicionarCampoTelefone(null));

        atualizarContatoButton.setOnClickListener(v -> atualizarContato());

        excluirContatoButton.setOnClickListener(v -> excluirContato());

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MainFragment()).commit());

        Bundle args = getArguments();
        if (args != null && args.containsKey("contatoId")) {
            long contatoId = args.getLong("contatoId");
            carregarContato(contatoId);
        }

        return view;
    }

    private void carregarContato(long contatoId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        contato = databaseHelper.getContatoById(contatoId);
        if (contato != null) {
            nomeCompletoEditText.setText(contato.getNome());
            numeros = contato.getNumeros();
            if (numeros != null) {
                for (Numero numero : numeros) {
                    adicionarCampoTelefone(numero);
                }
            }
        }
        databaseHelper.close();
    }

    private void adicionarCampoTelefone(Numero numero) {
        View phoneFieldView = LayoutInflater.from(getContext()).inflate(R.layout.phone_field, phoneContainer, false);
        EditText phoneNumberEditText = phoneFieldView.findViewById(R.id.phoneNumberEditText);
        Spinner phoneTypeSpinner = phoneFieldView.findViewById(R.id.phoneTypeSpinner);
        ImageButton removePhoneButton = phoneFieldView.findViewById(R.id.removePhoneButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneTypeSpinner.setAdapter(adapter);

        if (numero != null) {
            phoneNumberEditText.setText(numero.getNumero());
            phoneTypeSpinner.setSelection(adapter.getPosition(numero.getTipo()));
            removePhoneButton.setVisibility(View.VISIBLE);
            removePhoneButton.setOnClickListener(v -> removerCampoTelefone(phoneFieldView, numero));
        } else {
            removePhoneButton.setVisibility(View.GONE);
        }

        phoneContainer.addView(phoneFieldView);

        int childCount = phoneContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = phoneContainer.getChildAt(i);
            ImageButton childRemoveButton = childView.findViewById(R.id.removePhoneButton);
            childRemoveButton.setVisibility(childCount > 1 ? View.VISIBLE : View.GONE);
        }
    }

    private void removerCampoTelefone(View phoneFieldView, Numero numero) {
        phoneContainer.removeView(phoneFieldView);
        if (numero != null) {
            numeros.remove(numero);
        }

        int childCount = phoneContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = phoneContainer.getChildAt(i);
            ImageButton childRemoveButton = childView.findViewById(R.id.removePhoneButton);
            childRemoveButton.setVisibility(childCount > 1 ? View.VISIBLE : View.GONE);
        }
    }

    private void atualizarContato() {
        String nomeCompleto = nomeCompletoEditText.getText().toString();
        List<Numero> numerosAtualizados = obterNumerosAtualizados();

        if (nomeCompleto.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, insira o nome completo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (numerosAtualizados.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, insira pelo menos um número de telefone", Toast.LENGTH_SHORT).show();
            return;
        }

        contato.setNome(nomeCompleto);
        contato.setNumeros(numerosAtualizados);

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        databaseHelper.updateContato(contato);
        databaseHelper.close();

        Toast.makeText(getContext(), "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MainFragment())
                .commit();
    }

    private List<Numero> obterNumerosAtualizados() {
        List<Numero> numerosAtualizados = new ArrayList<>();

        for (int i = 0; i < phoneContainer.getChildCount(); i++) {
            View phoneFieldView = phoneContainer.getChildAt(i);
            EditText phoneNumberEditText = phoneFieldView.findViewById(R.id.phoneNumberEditText);
            Spinner phoneTypeSpinner = phoneFieldView.findViewById(R.id.phoneTypeSpinner);

            String numero = phoneNumberEditText.getText().toString();
            String tipo = phoneTypeSpinner.getSelectedItem().toString();

            if (!numero.isEmpty()) {
                Numero numeroObj = new Numero(numero, tipo);
                numerosAtualizados.add(numeroObj);
            }
        }

        return numerosAtualizados;
    }

    private void excluirContato() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        databaseHelper.deleteContato(contato.getId());
        databaseHelper.close();

        Toast.makeText(getContext(), "Contato excluído com sucesso", Toast.LENGTH_SHORT).show();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MainFragment())
                .commit();
    }
}