package com.example.adoteme;

public class Animal {
    public int id;

    public String nome;
    public byte[] foto;

    public String idade;

    public String raca;

    public String vacina;
    public String sexo;
    public String castrado;

    public String condicoes_medicas;

    public Animal(int id, String nome, byte[] foto, String idade, String raca, String vacina,
                  String sexo, String castrado, String condicoes_medicas) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
        this.idade = idade;
        this.raca = raca;
        this.vacina = vacina;
        this.sexo = sexo;
        this.castrado = castrado;
        this.condicoes_medicas = condicoes_medicas;
    }

    public byte[] foto2() {
        return new byte[0];
    }

    public byte[] foto3() {
        return new byte[0];
    }

    public byte[] foto4() {
        return new byte[0];
    }

    public String condicoes_medicas() {
        return "";
    }

    public byte[] foto1() {
        return new byte[0];
    }
}
