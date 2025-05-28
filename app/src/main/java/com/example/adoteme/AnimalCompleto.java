package com.example.adoteme;

public class AnimalCompleto {
    public int     id;
    public String  nome;
    public String  idade;
    public String  raca;
    public String  vacina;
    public String  sexo;
    public String  castrado;
    public String  condicoes;

    // até 4 fotos (BLOBs vindas do SQLite).
    public byte[]  foto1, foto2, foto3, foto4;

    public AnimalCompleto(int id, String nome, String idade, String raca, String vacina,
                          String sexo, String castrado, String condicoes,
                          byte[] f1, byte[] f2, byte[] f3, byte[] f4) {
        this.id        = id;
        this.nome      = nome;
        this.idade     = idade;
        this.raca      = raca;
        this.vacina    = vacina;
        this.sexo      = sexo;
        this.castrado  = castrado;
        this.condicoes = condicoes;
        this.foto1     = f1;
        this.foto2     = f2;
        this.foto3     = f3;
        this.foto4     = f4;
    }
}

