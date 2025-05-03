package com.example.adoteme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_ong extends AppCompatActivity {

    EditText emailEditText, senhaEditText;
    Button loginBtn, cadastroBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ong);

        // Inicializa os elementos
        emailEditText = findViewById(R.id.email);
        senhaEditText = findViewById(R.id.senha);
        loginBtn = findViewById(R.id.login);
        cadastroBtn = findViewById(R.id.cadastro);

        // Inicializa o banco de dados
        db = new DatabaseHelper(this);

        loginBtn.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                boolean loginValido = db.verificarLogin(email, senha);
                if (loginValido) {
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, perfil_ong.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cadastroBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, cadastro_ong.class);
            startActivity(intent);
        });
    }
}
