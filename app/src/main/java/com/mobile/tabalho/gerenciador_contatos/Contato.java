package com.mobile.tabalho.gerenciador_contatos;

import java.util.List;

public class Contato {
    private long id;
    private String nome;
    private List<Numero> numeros;

    public Contato(String nome, List<Numero> numeros) {
        this.nome = nome;
        this.numeros = numeros;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Numero> getNumeros() {
        return numeros;
    }

    public void setNumeros(List<Numero> numeros) {
        this.numeros = numeros;
    }
}

