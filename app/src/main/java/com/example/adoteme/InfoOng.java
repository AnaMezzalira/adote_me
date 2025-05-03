package com.example.adoteme;

public class InfoOng {
    public String nome_ong;
    public String endereco;
    public String telefone_1;
    public String telefone_2;
    public String insta;
    public String pix_1;
    public byte[] foto;

    public InfoOng(String nome, String endereco, String telefone1, String telefone2,
                   String instagram, String pix1, byte[] foto) {
        this.nome_ong = nome;
        this.endereco = endereco;
        this.telefone_1 = telefone1;
        this.telefone_2 = telefone2;
        this.insta = instagram;
        this.pix_1 = pix1;
        this.foto = foto;
    }
}
