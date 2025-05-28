package com.example.adoteme;


/** Modelo com as informações da ONG. */
public class InfoOng {

    public final String nome_ong;
    public final String endereco;
    public final String telefone_1;
    public final String telefone_2;
    public final String insta;
    public final String pix_1;
    public final String pix_2;
    public final byte[] foto;   // foto em blob

    public InfoOng(String nome,
                   String endereco,
                   String telefone1,
                   String telefone2,
                   String instagram,
                   String pix1,
                   String pix2,
                   byte[] foto) {

        this.nome_ong      = nome;
        this.endereco  = endereco;
        this.telefone_1 = telefone1;
        this.telefone_2 = telefone2;
        this.insta = instagram;
        this.pix_1      = pix1;
        this.pix_2      = pix2;
        this.foto      = foto;
    }

    /* Getters */

    public String getNome()      { return nome_ong; }
    public String getEndereco()  { return endereco; }
    public String getTelefone1() { return telefone_1; }
    public String getTelefone2() { return telefone_2; }
    public String getInstagram() { return insta; }
    public String getPix1()      { return pix_1; }
    public String getPix2()      { return pix_2; }
    public byte[]  getFoto()     { return foto; }
}
