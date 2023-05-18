package com.mobile.tabalho.gerenciador_contatos;

public class Numero {
    private long id;
    private String numero;
    private String tipo;

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

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}


