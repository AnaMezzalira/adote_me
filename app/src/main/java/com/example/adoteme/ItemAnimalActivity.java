package com.example.adoteme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ItemAnimalActivity extends AppCompatActivity {

    private ImageView img;
    private ImageButton setaEsq, setaDir;
    private TextView nome, idade, raca, vacinas, sexo, castrado, condicoes;
    private Button btnEdit, btnDelete;

    private final List<byte[]> fotos = new ArrayList<>();
    private int idx = 0;
    private int idAnimal;
    private DatabaseHelper db;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_animal);

        /* Bind de views */
        img       = findViewById(R.id.selecionar_fotos);
        setaEsq   = findViewById(R.id.seta_esquerda);
        setaDir   = findViewById(R.id.seta_direita);
        nome      = findViewById(R.id.nome_animal);
        idade     = findViewById(R.id.idade_animal);
        raca      = findViewById(R.id.raca);
        vacinas   = findViewById(R.id.vacina);
        sexo      = findViewById(R.id.sexo);
        castrado  = findViewById(R.id.castrado);
        condicoes = findViewById(R.id.condicoes_medicas);
        btnEdit   = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btnDelete);

        /* Banco + ID recebido da grid */
        db       = new DatabaseHelper(this);
        idAnimal = getIntent().getIntExtra("ID_ANIMAL", -1);

        carregarDados();               // preenche textos + fotos

        setaEsq.setOnClickListener(v -> {
            idx = (idx - 1 + fotos.size()) % fotos.size();
            mostrarFoto();
        });
        setaDir.setOnClickListener(v -> {
            idx = (idx + 1) % fotos.size();
            mostrarFoto();
        });

        /* ----------- EDITAR ----------- */
        btnEdit.setOnClickListener(v -> {
            startActivity(new Intent(this, cadastro_animal.class)
                    .putExtra("ID_ANIMAL", idAnimal));   // formulário em modo edição
        });

        /* ----------- REMOVER ----------- */
        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Remover animal")
                .setMessage("Tem certeza que deseja remover este animal?")
                .setPositiveButton("Remover", (d,w) -> {
                    if (db.excluirAnimal(idAnimal)) {
                        Toast.makeText(this, "Animal removido", Toast.LENGTH_SHORT).show();
                        finish();        // volta à lista
                    } else {
                        Toast.makeText(this, "Erro ao remover", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show()
        );
    }

    @Override protected void onResume() {
        super.onResume();
        carregarDados();               // caso volte do formulário após edição
    }

    /* ---------------- helpers ---------------- */

    private void carregarDados() {
        AnimalCompleto a = db.buscarAnimalPorId(idAnimal);
        if (a == null) { finish(); return; }

        nome.setText(a.nome);
        idade.setText("Idade: " + a.idade);
        raca.setText("Raça: " + a.raca);
        vacinas.setText("Vacinas: " + a.vacina);
        sexo.setText("Sexo: " + a.sexo);
        castrado.setText("Castrado: " + a.castrado);
        condicoes.setText("Condições médicas: " + a.condicoes);

        fotos.clear();
        if (a.foto1 != null) fotos.add(a.foto1);
        if (a.foto2 != null) fotos.add(a.foto2);
        if (a.foto3 != null) fotos.add(a.foto3);
        if (a.foto4 != null) fotos.add(a.foto4);
        if (fotos.isEmpty()) fotos.add(null);   // placeholder

        idx = 0;
        mostrarFoto();
    }

    private void mostrarFoto() {
        byte[] foto = fotos.get(idx);
        if (foto != null)
            img.setImageBitmap(BitmapFactory.decodeByteArray(foto, 0, foto.length));
        else
            img.setImageResource(R.drawable.logo);   // placeholder
    }
}
