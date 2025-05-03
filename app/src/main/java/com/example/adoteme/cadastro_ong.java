package com.example.adoteme;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class cadastro_ong extends AppCompatActivity {

    EditText nome, email, confirmarEmail, cnpj, cidade, senha, confirmarSenha;
    Spinner estado;
    Button btnCadastrar;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);

        // Ajuste da margem da tela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos campos
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        confirmarEmail = findViewById(R.id.confirmar_email);
        cnpj = findViewById(R.id.cnpj);
        cidade = findViewById(R.id.cidade);
        senha = findViewById(R.id.senha);
        confirmarSenha = findViewById(R.id.confirmar_senha);
        estado = findViewById(R.id.estado);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Spinner de estados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.estados_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estado.setAdapter(adapter);

        db = new DatabaseHelper(this);

        btnCadastrar.setOnClickListener(v -> {
            String nomeTexto = nome.getText().toString().trim();
            String emailTexto = email.getText().toString().trim();
            String confirmarEmailTexto = confirmarEmail.getText().toString().trim();
            String cnpjTexto = cnpj.getText().toString().trim();
            String cidadeTexto = cidade.getText().toString().trim();
            String estadoSelecionado = estado.getSelectedItem().toString();
            String senhaTexto = senha.getText().toString();
            String confirmarSenhaTexto = confirmarSenha.getText().toString();

            if (nomeTexto.isEmpty() || emailTexto.isEmpty() || confirmarEmailTexto.isEmpty() ||
                    cnpjTexto.isEmpty() || cidadeTexto.isEmpty() || senhaTexto.isEmpty() || confirmarSenhaTexto.isEmpty() ||
                    estadoSelecionado.equals("Selecione o estado")) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!emailTexto.equals(confirmarEmailTexto)) {
                Toast.makeText(this, "E-mails não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senhaTexto.equals(confirmarSenhaTexto)) {
                Toast.makeText(this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insere no banco
            boolean inserido = db.inserirONG(nomeTexto, emailTexto, cnpjTexto, cidadeTexto, estadoSelecionado, senhaTexto);
            if (inserido) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // volta pra tela anterior
            } else {
                Toast.makeText(this, "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
