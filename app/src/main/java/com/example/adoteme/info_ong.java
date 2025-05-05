package com.example.adoteme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class info_ong extends AppCompatActivity {

    // ----- Views -----
    private EditText nomeOng, endereco, telefone1, telefone2,
            instagram, pix1, pix2;
    private Button   salvarBtn, fotoBtn;
    private ImageView imageView;

    // ----- Dados -----
    private byte[] imagemSelecionada;
    private String emailDaOng;          // recebido por Intent

    private DatabaseHelper db;
    private ActivityResultLauncher<Intent> selecionarImagemLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ong);

        // 1) Obtém o e‑mail enviado pela PerfilOngActivity
        emailDaOng = getIntent().getStringExtra("EMAIL_ONG");
        if (emailDaOng == null) {        // segurança
            Toast.makeText(this, "Erro: e‑mail da ONG não informado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2) Bind de views
        nomeOng   = findViewById(R.id.nome_ong);
        endereco  = findViewById(R.id.endereco);
        telefone1 = findViewById(R.id.telefone_1);
        telefone2 = findViewById(R.id.telefone_2);
        instagram = findViewById(R.id.insta);
        pix1      = findViewById(R.id.pix_1);
        pix2      = findViewById(R.id.pix_2);
        fotoBtn   = findViewById(R.id.foto);
        salvarBtn = findViewById(R.id.mudanca);
        imageView = findViewById(R.id.imageView);

        db = new DatabaseHelper(this);

        // 3) Carrega dados existentes (se houver)
        carregarInfoSeExistir();

        // 4) Selecionar imagem
        selecionarImagemLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imagemSelecionada = bitmapToByteArray(bmp);
                            imageView.setImageBitmap(bmp);
                        } catch (IOException e) {
                            Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        fotoBtn.setOnClickListener(v -> {
            Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selecionarImagemLauncher.launch(it);
        });

        // 5) Salvar
        salvarBtn.setOnClickListener(v -> salvarInfo());
    }

    // ------------------------------------------------------------------
    private void carregarInfoSeExistir() {
        InfoOng info = db.buscarInfoOng(emailDaOng);
        if (info == null) return;

        nomeOng.setText  (info.nome_ong);
        endereco.setText (info.endereco);
        telefone1.setText(info.telefone_1);
        telefone2.setText(info.telefone_2);
        instagram.setText(info.insta);
        pix1.setText     (info.pix_1);
        pix2.setText     (info.pix_2);                      // ajuste se usar pix2
        imagemSelecionada = info.foto;

        if (info.foto != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(
                    info.foto, 0, info.foto.length));
        }
    }

    private void salvarInfo() {
        String nome   = nomeOng.getText().toString().trim();
        String end    = endereco.getText().toString().trim();
        String tel1   = telefone1.getText().toString().trim();
        String tel2   = telefone2.getText().toString().trim();
        String insta  = instagram.getText().toString().trim();
        String p1     = pix1.getText().toString().trim();
        String p2     = pix2.getText().toString().trim();

        if (nome.isEmpty() || end.isEmpty() || tel1.isEmpty() || insta.isEmpty() || p1.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = db.salvarOuAtualizarInfoONG(
                emailDaOng, nome, end, tel1, tel2, insta, p1, p2, imagemSelecionada);

        if (ok) {
            Toast.makeText(this, "Informações salvas!", Toast.LENGTH_SHORT).show();
            finish();   // volta para PerfilOngActivity (que recarrega os dados no onResume)
        } else {
            Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }
}
