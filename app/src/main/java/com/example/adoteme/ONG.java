package com.example.adoteme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ONG extends AppCompatActivity {

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ong);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ong);

            // Coloque aqui, fora do setOnApplyWindowInsetsListener
            DatabaseHelper db = new DatabaseHelper(this);

            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                View view = findViewById(R.id.main);
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


            // Supondo que você já tenha os campos de entrada:
            EditText nome = findViewById(R.id.nome);
            EditText email = findViewById(R.id.email);
            EditText cnpj = findViewById(R.id.cnpj);
            EditText senha = findViewById(R.id.senha);
            Spinner estado = findViewById(R.id.estado);
            Spinner cidade = findViewById(R.id.cidade);


        View btnCadastrar = null;
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomeStr = nome.getText().toString();
                    String emailStr = email.getText().toString();
                    String cnpjStr = cnpj.getText().toString();
                    String senhaStr = senha.getText().toString();
                    String estadotr = estado.getSelectedItem().toString();
                    String cidadeStr = cidade.getSelectedItem().toString();

                    if (nome.getText().toString().isEmpty() || email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean sucesso = db.inserirUsuario(nome, email, senha, cnpj, estado, cidade);
                        if (sucesso) {
                            Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao cadastrar. Email já cadastrado?", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });





    }

}