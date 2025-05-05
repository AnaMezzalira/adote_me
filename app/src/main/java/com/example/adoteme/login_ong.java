package com.example.adoteme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_ong extends AppCompatActivity {

    EditText emailEditText, senhaEditText;
    Button   loginBtn,      cadastroBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ong);

        emailEditText = findViewById(R.id.email);
        senhaEditText = findViewById(R.id.senha);
        loginBtn      = findViewById(R.id.login);
        cadastroBtn   = findViewById(R.id.cadastro);

        db = new DatabaseHelper(this);

        loginBtn.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.verificarLogin(email, senha)) {
                Intent it = new Intent(this, perfil_ong.class);
                it.putExtra("EMAIL_ONG", email);
                startActivity(it);
                finish();
            } else {
                Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        cadastroBtn.setOnClickListener(v ->
                startActivity(new Intent(this, cadastro_ong.class))
        );
    }
}
