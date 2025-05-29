package com.example.adoteme;

public class Ong {
    private String nome;
    private String estado;
    private String email;

    // 👇 Agora o construtor recebe o e-mail corretamente
    public Ong(String nome, String email, String estado) {
        this.nome = nome;
        this.email = email;
        this.estado = estado;
    }

    public String getNome() { return nome; }
    public String getEstado() { return estado; }
    public String getEmail() { return email; }
}
