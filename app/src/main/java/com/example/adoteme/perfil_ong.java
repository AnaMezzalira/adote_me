package com.example.adoteme;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class perfil_ong extends AppCompatActivity {

    // --- Views do card superior ---
    private ImageView fotoLogo;
    private TextView  txtNome, txtEndereco, txtTelefone,
            txtInstagram, txtPix1, txtPix2;

    // --- Grade de animais ---
    private GridView               gridAnimais;
    private FloatingActionButton   botaoAdicionar;
    private ImageButton            botaoEditar;
    private List<Animal>           listaAnimais;
    private GridAnimalAdapter      adapter;

    // --- Auxiliares ---
    private DatabaseHelper  db;
    private String          emailOng;   // recebido via Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_ong);

        // e‑mail da ONG logada (veio da tela de login)
        emailOng = getIntent().getStringExtra("EMAIL_ONG");

        // ------------ Bind das views ------------
        fotoLogo     = findViewById(R.id.foto_logo);
        txtNome      = findViewById(R.id.nome_ong);
        txtEndereco  = findViewById(R.id.endereco);
        txtTelefone  = findViewById(R.id.telefone);
        txtInstagram = findViewById(R.id.instagram);
        txtPix1      = findViewById(R.id.pix1);
        txtPix2      = findViewById(R.id.pix2);

        gridAnimais    = findViewById(R.id.grid_animais);
        botaoAdicionar = findViewById(R.id.botao_adicionar_animal);
        botaoEditar    = findViewById(R.id.botao_editar_info);

        db           = new DatabaseHelper(this);
        listaAnimais = db.buscarAnimais(emailOng);
        adapter      = new GridAnimalAdapter(this, listaAnimais);
        gridAnimais.setAdapter(adapter);

        // ----------- BOTÕES ------------
        botaoAdicionar.setOnClickListener(v -> {
            Intent it = new Intent(this, cadastro_animal.class);
            it.putExtra("EMAIL_ONG", emailOng);
            startActivity(it);
        });

        botaoEditar.setOnClickListener(v -> {
            Intent it = new Intent(this, info_ong.class);
            it.putExtra("EMAIL_ONG", emailOng);
            startActivity(it);
        });

        gridAnimais.setOnItemClickListener((parent, view, position, id) -> {
            Animal a = listaAnimais.get(position);
            Intent it = new Intent(this, ItemAnimalActivity.class);
            it.putExtra("ID_ANIMAL", a.id);
            startActivity(it);
        });

        // Primeira carga de informações da ONG
        atualizarCardInfo();
    }

    // ------------------------------------------------------------------
    // SEMPRE que voltamos para esta Activity, recarrega dados atualizados
    // ------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();

        // 1) Card superior
        atualizarCardInfo();

        // 2) Grade de animais
        listaAnimais.clear();
        listaAnimais.addAll(db.buscarAnimais(emailOng));
        adapter.notifyDataSetChanged();
    }

    // ---------------- Atualiza o card a partir do Banco ----------------
    private void atualizarCardInfo() {
        InfoOng info = db.buscarInfoOng(emailOng);
        if (info == null) return;  // ainda não cadastrou nada

        txtNome.setText     (info.nome_ong);
        txtEndereco.setText (info.endereco);
        txtTelefone.setText (info.telefone_1);
        txtInstagram.setText(info.insta);
        txtPix1.setText     (info.pix_1);
        txtPix2.setText     (info.telefone_2);  // se quiser mostrar um 2º Pix, basta ajustar

        if (info.foto != null)
            fotoLogo.setImageBitmap(BitmapFactory.decodeByteArray(
                    info.foto, 0, info.foto.length));
    }
}
