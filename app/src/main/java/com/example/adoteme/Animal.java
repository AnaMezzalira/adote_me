package com.example.adoteme;

public class Animal {
    public int id;

    public String nome;
    public byte[] f1, f2, f3, f4;

    public String idade;

    public String raca;

    public String vacina;
    public String sexo;
    public String castrado;

    public String condicoes_medicas;

    public Animal(int id, String nome, byte[] foto1, byte[] foto2, byte[] foto3, byte[] foto4, String idade, String raca, String vacina,
                  String sexo, String castrado, String condicoes_medicas) {
        this.id = id;
        this.nome = nome;
        this.f1 = foto1;
        this.f2 = foto2;
        this.f3 = foto3;
        this.f4 = foto4;
        this.idade = idade;
        this.raca = raca;
        this.vacina = vacina;
        this.sexo = sexo;
        this.castrado = castrado;
        this.condicoes_medicas = condicoes_medicas;
    }
    public byte[] foto1() { return f1; }
    public byte[] foto2() { return f2; }
    public byte[] foto3() { return f3; }
    public byte[] foto4() { return f4; }


    public String condicoes_medicas() {
        return "";
    }

}
