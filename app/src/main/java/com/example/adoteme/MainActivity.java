package com.example.adoteme;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private Button buttonAdotante;
    private Button buttonONG;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonAdotante = (Button) findViewById(R.id.buttonAdotante);
        buttonAdotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAdotante();

            }
        });

        buttonONG = (Button) findViewById(R.id.buttonloginONG);
        buttonONG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login_ong.class);
                startActivity(intent);

            }
        });

    }
    public void abrirAdotante(){
        //abrir outra activity
        Intent intent = new Intent(this, Adotante.class);
        startActivity(intent);

    }
}