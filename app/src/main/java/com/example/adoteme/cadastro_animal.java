package com.example.adoteme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class cadastro_animal extends AppCompatActivity {

    EditText nome, raca, vacina, condicoes;
    Spinner sexo, castrado;
    ImageView foto1, foto2, foto3, foto4;
    Button btnSelecionarFotos, btnSalvar;

    ArrayList<byte[]> fotosSelecionadas = new ArrayList<>();
    ArrayList<ImageView> visualizadores;
    int fotoAtual = 0;

    DatabaseHelper db;
    String emailDaOng = "email@exemplo.com"; // trocar depois

    ActivityResultLauncher<Intent> selecionarImagem;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal);

        nome = findViewById(R.id.nome_animal);
        raca = findViewById(R.id.raca);
        vacina = findViewById(R.id.vacina);
        condicoes = findViewById(R.id.condicoes_medicas);
        sexo = findViewById(R.id.sexo);
        castrado = findViewById(R.id.castrado);

        foto1 = findViewById(R.id.foto1);
        foto2 = findViewById(R.id.foto2);
        foto3 = findViewById(R.id.foto3);
        foto4 = findViewById(R.id.foto4);

        btnSelecionarFotos = findViewById(R.id.selecionar_fotos);
        btnSalvar = findViewById(R.id.selecionar_fotos);

        visualizadores = new ArrayList<>();
        visualizadores.add(foto1);
        visualizadores.add(foto2);
        visualizadores.add(foto3);
        visualizadores.add(foto4);

        db = new DatabaseHelper(this);

        // Spinners
        ArrayAdapter<CharSequence> adapterSexo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Selecione", "Macho", "Fêmea"});
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapterSexo);

        ArrayAdapter<CharSequence> adapterCastrado = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Selecione", "Sim", "Não"});
        adapterCastrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        castrado.setAdapter(adapterCastrado);

        selecionarImagem = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            byte[] imgBytes = bitmapToBytes(bitmap);
                            if (fotoAtual < 4) {
                                fotosSelecionadas.add(imgBytes);
                                visualizadores.get(fotoAtual).setImageBitmap(bitmap);
                                fotoAtual++;
                            } else {
                                Toast.makeText(this, "Limite de 4 fotos atingido!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnSelecionarFotos.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selecionarImagem.launch(intent);
        });

        btnSalvar.setOnClickListener(v -> {
            if (nome.getText().toString().isEmpty() || sexo.getSelectedItemPosition() == 0 ||
                    castrado.getSelectedItemPosition() == 0 || fotosSelecionadas.size() == 0) {
                Toast.makeText(this, "Preencha os campos obrigatórios e selecione pelo menos 1 foto", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.inserirAnimal(
                    emailDaOng,
                    nome.getText().toString(),
                    sexo.getSelectedItem().toString(),
                    castrado.getSelectedItem().toString(),
                    raca.getText().toString(),
                    vacina.getText().toString(),
                    condicoes.getText().toString(),
                    fotosSelecionadas
            );

            if (ok) {
                Toast.makeText(this, "Animal cadastrado!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
