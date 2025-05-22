package com.example.adoteme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

    // ------------- Views -------------
    private EditText nome, idade, raca, vacina, condicoes;
    private Spinner  sexo, castrado;
    private ImageView foto1, foto2, foto3, foto4;
    private Button btnSelecionarFotos, btnSalvar;

    // ------------- Dados -------------
    private final ArrayList<byte[]> fotos = new ArrayList<>(4);
    private final ArrayList<ImageView> slots = new ArrayList<>(4);
    private int slotAtual = 0;              // 0‑3

    private DatabaseHelper db;
    private String emailDaOng;

    private ActivityResultLauncher<Intent> seletorImagem;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_cadastro_animal);

        // Bind
        nome  = findViewById(R.id.nome_animal);
        idade = findViewById(R.id.idade_animal);
        raca  = findViewById(R.id.raca);
        vacina= findViewById(R.id.vacina);
        condicoes = findViewById(R.id.condicoes_medicas);

        sexo     = findViewById(R.id.sexo);
        castrado = findViewById(R.id.castrado);

        foto1 = findViewById(R.id.foto1);
        foto2 = findViewById(R.id.foto2);
        foto3 = findViewById(R.id.foto3);
        foto4 = findViewById(R.id.foto4);

        btnSelecionarFotos = findViewById(R.id.selecionar_fotos);
        btnSalvar          = findViewById(R.id.mudanca);

        // Config inicial para lista de fotos (null)
        for (int i=0;i<4;i++) fotos.add(null);
        slots.add(foto1); slots.add(foto2); slots.add(foto3); slots.add(foto4);

        // Email recebido da tela PerfilOngActivity
        emailDaOng = getIntent().getStringExtra("EMAIL_ONG");

        db = new DatabaseHelper(this);

        // Spinners
        ArrayAdapter<String> adSexo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Selecione","Macho","Fêmea"});
        adSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adSexo);

        ArrayAdapter<String> adCast = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Selecione","Sim","Não"});
        adCast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        castrado.setAdapter(adCast);

        // Seletor de Imagem
        seletorImagem = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), res -> {
                    if (res.getResultCode()==RESULT_OK && res.getData()!=null){
                        Uri uri = res.getData().getData();
                        try{
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            slots.get(slotAtual).setImageBitmap(bmp);
                            fotos.set(slotAtual, bmpToBytes(bmp));
                        }catch (IOException e){
                            Toast.makeText(this,"Erro ao carregar imagem",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnSelecionarFotos.setOnClickListener(v -> {
            Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            seletorImagem.launch(it);
        });

        // Cada miniatura define qual slot será sobrescrito; long‑press limpa slot
        for (int i=0;i<slots.size();i++){
            int idx = i;
            slots.get(i).setOnClickListener(v -> slotAtual = idx);
            slots.get(i).setOnLongClickListener(v -> {
                slots.get(idx).setImageResource(R.drawable.logo);
                fotos.set(idx,null);
                return true;
            });
        }

        btnSalvar.setOnClickListener(v -> salvarAnimal());
    }

    // ------------- SALVAR -------------
    private void salvarAnimal(){

        // Validações mínimas
        if (nome.getText().toString().trim().isEmpty() ||
                idade.getText().toString().trim().isEmpty() ||
                sexo.getSelectedItemPosition()==0 ||
                castrado.getSelectedItemPosition()==0) {
            Toast.makeText(this,"Preencha os campos obrigatórios",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean temFoto = false;
        for (byte[] f : fotos) if (f!=null){ temFoto=true; break; }
        if (!temFoto){
            Toast.makeText(this,"Selecione pelo menos 1 foto",Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria lista sem nulls
        ArrayList<byte[]> fotosValidas = new ArrayList<>();
        for (byte[] f : fotos) if (f!=null) fotosValidas.add(f);

        boolean ok = db.inserirAnimal(
                emailDaOng,
                nome.getText().toString(),
                idade.getText().toString(),
                sexo.getSelectedItem().toString(),
                castrado.getSelectedItem().toString(),
                raca.getText().toString(),
                vacina.getText().toString(),
                condicoes.getText().toString(),
                fotosValidas
        );

        if (ok) {
            Toast.makeText(this, "Animal cadastrado!", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(this, perfil_ong.class);
            it.putExtra("EMAIL_ONG", emailDaOng);
            startActivity(it);
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
        }

    }


    private byte[] bmpToBytes(Bitmap b){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,100,out);
        return out.toByteArray();
    }
}
