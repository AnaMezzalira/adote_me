package com.example.adoteme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaOngsActivity extends AppCompatActivity {

    private EditText editBusca;
    private Spinner spinnerEstado;
    private RecyclerView recyclerOngs;

    private List<Ong> listaOngs = new ArrayList<>();
    private OngAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ongs);

        editBusca = findViewById(R.id.edit_busca);
        spinnerEstado = findViewById(R.id.spinner_estado);
        recyclerOngs = findViewById(R.id.recycler_ongs);

        String[] estados = {"Todos", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES",
                "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
                "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, estados);
        spinnerEstado.setAdapter(spinnerAdapter);

        DatabaseHelper db = new DatabaseHelper(this);
        listaOngs = db.buscarTodasOngs();

        adapter = new OngAdapter(listaOngs, this::abrirPerfilOng);


        Collections.sort(listaOngs, Comparator.comparing(Ong::getNome));

        adapter = new OngAdapter(listaOngs, this::abrirPerfilOng);
        recyclerOngs.setLayoutManager(new LinearLayoutManager(this));
        recyclerOngs.setAdapter(adapter);

        editBusca.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrar();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filtrar() {
        String textoBusca = editBusca.getText().toString().toLowerCase();
        String estadoSelecionado = spinnerEstado.getSelectedItem().toString();

        List<Ong> listaFiltrada = new ArrayList<>();

        for (Ong ong : listaOngs) {
            boolean nomeCombina = ong.getNome().toLowerCase().contains(textoBusca);
            boolean estadoCombina = estadoSelecionado.equals("Todos") || ong.getEstado().equals(estadoSelecionado);

            if (nomeCombina && estadoCombina) {
                listaFiltrada.add(ong);
            }
        }

        adapter.atualizarLista(listaFiltrada);
    }

    private void abrirPerfilOng(Ong ong) {
        Intent intent = new Intent(this, perfil_ong.class);
        intent.putExtra("EMAIL_ONG", ong.getEmail()); // ⚠️ Altere para o campo correto
        intent.putExtra("modo_leitura", true);        // 👈 Ativa o modo leitura
        startActivity(intent);
    }



}
