package com.example.adoteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AdoteMe.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cadastro da ONG
        String CREATE_TABLE_ONGS = "CREATE TABLE ongs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "email TEXT UNIQUE," +
                "cnpj TEXT," +
                "cidade TEXT," +
                "estado TEXT," +
                "senha TEXT)";
        db.execSQL(CREATE_TABLE_ONGS);

        // Info da ONG
        String CREATE_TABLE_INFO = "CREATE TABLE info_ong (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT," +
                "nome TEXT," +
                "endereco TEXT," +
                "telefone1 TEXT," +
                "telefone2 TEXT," +
                "instagram TEXT," +
                "pix1 TEXT," +
                "pix2 TEXT," +
                "foto BLOB)";
        db.execSQL(CREATE_TABLE_INFO);

        // Animais da ONG
        String CREATE_TABLE_ANIMAIS = "CREATE TABLE animais (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email_ong TEXT," +
                "nome TEXT," +
                "sexo TEXT," +
                "castrado TEXT," +
                "raca TEXT," +
                "vacina TEXT," +
                "condicoes TEXT," +
                "foto1 BLOB," +
                "foto2 BLOB," +
                "foto3 BLOB," +
                "foto4 BLOB)";
        db.execSQL(CREATE_TABLE_ANIMAIS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ongs");
        db.execSQL("DROP TABLE IF EXISTS info_ong");
        db.execSQL("DROP TABLE IF EXISTS animais");
        onCreate(db);
    }

    // ---------------------------
    // MÉTODOS DE ONG
    // ---------------------------

    public boolean inserirONG(String nome, String email, String cnpj, String cidade, String estado, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("email", email);
        values.put("cnpj", cnpj);
        values.put("cidade", cidade);
        values.put("estado", estado);
        values.put("senha", senha);

        long resultado = db.insert("ongs", null, values);
        return resultado != -1;
    }

    public boolean verificarLogin(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ongs WHERE email = ? AND senha = ?", new String[]{email, senha});
        boolean valido = cursor.getCount() > 0;
        cursor.close();
        return valido;
    }

    // ---------------------------
    // MÉTODOS DE INFO_ONG
    // ---------------------------

    public boolean salvarInfoONG(String email, String nome, String endereco, String telefone1, String telefone2,
                                 String instagram, String pix1, String pix2, byte[] imagem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("nome", nome);
        values.put("endereco", endereco);
        values.put("telefone1", telefone1);
        values.put("telefone2", telefone2);
        values.put("instagram", instagram);
        values.put("pix1", pix1);
        values.put("pix2", pix2);
        values.put("foto", imagem);

        long result = db.insert("info_ong", null, values);
        return result != -1;
    }

    public InfoOng buscarInfoOng(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM info_ong WHERE email = ? ORDER BY id DESC LIMIT 1", new String[]{email});
        if (cursor.moveToFirst()) {
            InfoOng info = new InfoOng(
                    cursor.getString(cursor.getColumnIndexOrThrow("nome_ong")),
                    cursor.getString(cursor.getColumnIndexOrThrow("endereco")),
                    cursor.getString(cursor.getColumnIndexOrThrow("telefone_1")),
                    cursor.getString(cursor.getColumnIndexOrThrow("telefone_2")),
                    cursor.getString(cursor.getColumnIndexOrThrow("instag")),
                    cursor.getString(cursor.getColumnIndexOrThrow("pix_1")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("foto"))
            );
            cursor.close();
            return info;
        }
        cursor.close();
        return null;
    }

    // ---------------------------
    // MÉTODOS DE ANIMAIS
    // ---------------------------

    public boolean inserirAnimal(String email, String nome, String sexo, String castrado, String raca, String vacina,
                                 String condicoes, List<byte[]> fotos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email_ong", email);
        values.put("nome", nome);
        values.put("sexo", sexo);
        values.put("castrado", castrado);
        values.put("raca", raca);
        values.put("vacina", vacina);
        values.put("condicoes", condicoes);

        for (int i = 0; i < fotos.size(); i++) {
            values.put("foto" + (i + 1), fotos.get(i));
        }

        long result = db.insert("animais", null, values);
        return result != -1;
    }

    public List<Animal> buscarAnimais(String email) {
        List<Animal> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, nome, foto1 FROM animais WHERE email_ong = ?", new String[]{email});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            byte[] foto = cursor.getBlob(cursor.getColumnIndexOrThrow("foto1"));
            lista.add(new Animal(id, nome, foto));
        }
        cursor.close();
        return lista;
    }

    public boolean excluirAnimal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("animais", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
