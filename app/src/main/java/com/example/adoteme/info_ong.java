package com.example.adoteme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class info_ong extends AppCompatActivity {

    EditText nomeOng, endereco, telefone1, telefone2, instagram, pix1, pix2;
    Button salvarBtn, fotoBtn;
    ImageView imageView;
    byte[] imagemSelecionada;
    DatabaseHelper db;

    // email da ong logada (simulado por enquanto, substitua por Intent extra depois)
    String emailDaOng = "email@exemplo.com";

    ActivityResultLauncher<Intent> selecionarImagemLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ong);

        nomeOng = findViewById(R.id.nome_ong);
        endereco = findViewById(R.id.endereco);
        telefone1 = findViewById(R.id.telefone_1);
        telefone2 = findViewById(R.id.telefone_2);
        instagram = findViewById(R.id.insta);
        pix1 = findViewById(R.id.pix_1);
        pix2 = findViewById(R.id.pix_2);
        fotoBtn = findViewById(R.id.foto);
        salvarBtn = findViewById(R.id.mudanca);
        imageView = findViewById(R.id.imageView);

        db = new DatabaseHelper(this);

        selecionarImagemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            imagemSelecionada = bitmapToByteArray(bitmap);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        fotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selecionarImagemLauncher.launch(intent);
        });

        salvarBtn.setOnClickListener(view -> {
            String nome = nomeOng.getText().toString().trim();
            String end = endereco.getText().toString().trim();
            String tel1 = telefone1.getText().toString().trim();
            String tel2 = telefone2.getText().toString().trim();
            String insta = instagram.getText().toString().trim();
            String p1 = pix1.getText().toString().trim();
            String p2 = pix2.getText().toString().trim();

            if (nome.isEmpty() || end.isEmpty() || tel1.isEmpty() || insta.isEmpty() || p1.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean sucesso = db.salvarInfoONG(emailDaOng, nome, end, tel1, tel2, insta, p1, p2, imagemSelecionada);
            if (sucesso) {
                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
