package com.example.adoteme;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class ItemAnimalActivity extends AppCompatActivity {

    private ImageView img, setaEsq, setaDir;
    private TextView  nome, idade, raca, vacinas, sexo, castrado, condicoes;
    private List<byte[]> fotos;
    private int idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_animal);

        // Bind de views
        img       = findViewById(R.id.imagem_animal);
        setaEsq   = findViewById(R.id.seta_esquerda);
        setaDir   = findViewById(R.id.seta_direita);
        nome      = findViewById(R.id.nome);
        idade     = findViewById(R.id.idade);
        raca      = findViewById(R.id.raca);
        vacinas   = findViewById(R.id.vacinas);
        sexo      = findViewById(R.id.sexo);
        castrado  = findViewById(R.id.castrado);
        condicoes = findViewById(R.id.condicoes);

        // ID passado pela Grid
        int idAnimal = getIntent().getIntExtra("ID_ANIMAL", -1);
        AnimalCompleto a = new DatabaseHelper(this).buscarAnimalPorId(idAnimal);
        if (a == null) { finish(); return; }

        // Preenche textos
        nome.setText(a.nome);
        idade.setText(a.idade);
        raca.setText(a.raca);
        vacinas.setText(a.vacina);
        sexo.setText(a.sexo);
        castrado.setText(a.castrado);
        condicoes.setText(a.condicoes);

        // Lista de fotos (só adiciona se não for null)
        fotos = Arrays.asList(a.foto1, a.foto2, a.foto3, a.foto4);
        mostrarFoto();

        setaEsq.setOnClickListener(v -> {
            idx = (idx - 1 + fotos.size()) % fotos.size();
            mostrarFoto();
        });

        setaDir.setOnClickListener(v -> {
            idx = (idx + 1) % fotos.size();
            mostrarFoto();
        });
    }

    private void mostrarFoto() {
        if (fotos.get(idx) != null) {
            img.setImageBitmap(
                    BitmapFactory.decodeByteArray(fotos.get(idx), 0, fotos.get(idx).length)
            );
        } else {
            img.setImageResource(R.drawable.logo);
        }
    }
}
