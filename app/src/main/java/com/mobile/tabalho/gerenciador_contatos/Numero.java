package com.mobile.tabalho.gerenciador_contatos;

public class Numero {
    private long id;
    private final String numero;
    private final String tipo;

    public Numero(String numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }
}


