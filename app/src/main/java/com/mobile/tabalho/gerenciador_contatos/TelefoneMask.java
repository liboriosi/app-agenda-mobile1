package com.mobile.tabalho.gerenciador_contatos;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TelefoneMask implements TextWatcher {
    private final EditText editText;

    public TelefoneMask(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);

        String telefone = s.toString();

        telefone = telefone.replaceAll("[^\\d]", "");

        int maxDigits = 11;
        if (!(telefone.length() > maxDigits)) {
            if (telefone.length() > 10) {
                telefone = telefone.replaceFirst("(\\d{2})(\\d{5})(\\d+)", "($1) $2-$3");
            } else if (telefone.length() > 6) {
                telefone = telefone.replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1) $2-$3");
            } else if (telefone.length() > 2) {
                telefone = telefone.replaceFirst("(\\d{2})(\\d+)", "($1) $2");
            }
        } else {
            telefone = telefone.substring(0, maxDigits);
            telefone = telefone.replaceFirst("(\\d{2})(\\d{5})(\\d+)", "($1) $2-$3");
        }

        editText.setText(telefone);
        editText.setSelection(editText.getText().length());
        editText.addTextChangedListener(this);
    }
}