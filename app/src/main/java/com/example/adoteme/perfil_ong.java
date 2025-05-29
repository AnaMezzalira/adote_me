package com.example.adoteme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/** Tela de perfil da ONG */
public class perfil_ong extends AppCompatActivity
        implements GridAnimalAdapter.OnAnimalDeleteListener {

    private ImageView foto;
    private TextView  nome_ong, endereco, telefone_1,
            insta, pix_1, pix_2;
    /** Grid que “cresce” até exibir todas as linhas */
    private ExpandableHeightGridView grid;
    private FloatingActionButton fabAdd;
    private ImageButton btnEdit;

    private DatabaseHelper db;
    private final List<Animal> lista = new ArrayList<>();
    private GridAnimalAdapter adapter;
    private String emailOng;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_perfil_ong);

        /* e-mail enviado pela tela de login ou catálogo */
        emailOng = getIntent().getStringExtra("EMAIL_ONG");

        /* Views */
        foto       = findViewById(R.id.foto_logo);
        nome_ong   = findViewById(R.id.nome_ong);
        endereco   = findViewById(R.id.endereco);
        telefone_1 = findViewById(R.id.telefone);
        insta      = findViewById(R.id.instagram);
        pix_1      = findViewById(R.id.pix1);
        pix_2      = findViewById(R.id.pix2);

        grid   = findViewById(R.id.grid_animais);
        fabAdd = findViewById(R.id.botao_adicionar_animal);
        btnEdit= findViewById(R.id.botao_editar_info);

        /* Verifica se é modo leitura (adotante) */
        boolean modoLeitura = getIntent().getBooleanExtra("modo_leitura", false);
        if (modoLeitura) {
            fabAdd.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }

        /* Banco e adapter */
        db = new DatabaseHelper(this);
        lista.addAll(db.buscarAnimais(emailOng));
        adapter = new GridAnimalAdapter(this, lista, this);
        grid.setAdapter(adapter);

        /* Botões */
        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, cadastro_animal.class)
                        .putExtra("EMAIL_ONG", emailOng))
        );

        btnEdit.setOnClickListener(v ->
                startActivity(new Intent(this, info_ong.class)
                        .putExtra("EMAIL_ONG", emailOng))
        );

        grid.setOnItemClickListener((p, v, pos, id) ->
                startActivity(new Intent(this, ItemAnimalActivity.class)
                        .putExtra("ID_ANIMAL", (int) id))
        );

        atualizarCard();

        emailOng = getIntent().getStringExtra("EMAIL_ONG");

        if (emailOng == null || emailOng.isEmpty()) {
            Toast.makeText(this, "EMAIL_ONG está nulo ou vazio", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarCard();
        recarregarGrid();
    }

    /* Atualiza dados da ONG */
    private void atualizarCard() {
        InfoOng i = db.buscarInfoOng(emailOng);
        if (i == null) return;

        nome_ong.setText(i.getNome());
        endereco.setText(i.getEndereco());
        telefone_1.setText(i.getTelefone1());
        insta.setText(i.getInstagram());
        pix_1.setText(i.getPix1());
        pix_2.setText(i.getPix2());

        if (i.getFoto() != null)
            foto.setImageBitmap(BitmapFactory.decodeByteArray(i.getFoto(), 0, i.getFoto().length));
    }

    /* Atualiza grid de animais */
    private void recarregarGrid() {
        lista.clear();
        lista.addAll(db.buscarAnimais(emailOng));
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    /* Callback de exclusão */
    @Override
    public void onDelete(Animal a) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir animal")
                .setMessage("Tem certeza que deseja excluir \"" + a.nome + "\"?")
                .setPositiveButton("Excluir", (d, w) -> {
                    db.excluirAnimal(a.id);
                    Toast.makeText(this, "Animal excluído.", Toast.LENGTH_SHORT).show();
                    recarregarGrid();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


}
