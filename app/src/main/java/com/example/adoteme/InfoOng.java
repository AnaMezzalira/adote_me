package com.example.adoteme;

/**
 * Modelo que representa as informações complementares de uma ONG.
 */
public class InfoOng {

    // --------- CAMPOS ----------
    public String nome_ong;
    public String endereco;
    public String telefone_1;
    public String telefone_2;
    public String insta;
    public String pix_1;
    public String pix_2;   // <‑‑ NOVO
    public byte[] foto;

    // --------- CONSTRUTOR (8 parâmetros) ----------
    public InfoOng(String nome, String endereco,
                   String telefone1, String telefone2,
                   String instagram,
                   String pix1, String pix2,      // agora inclui pix2
                   byte[] foto) {

        this.nome_ong   = nome;
        this.endereco   = endereco;
        this.telefone_1 = telefone1;
        this.telefone_2 = telefone2;
        this.insta      = instagram;
        this.pix_1      = pix1;
        this.pix_2      = pix2;
        this.foto       = foto;
    }
}
