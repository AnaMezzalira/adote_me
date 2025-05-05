package com.example.adoteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AdoteMe.db";
    private static final int    DATABASE_VERSION = 1;

    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ----------------------------------------------------------------
    //  CRIAÇÃO DAS TABELAS
    // ----------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {

        // ONGs (cadastro básico)
        db.execSQL(
                "CREATE TABLE ongs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome   TEXT," +
                        "email  TEXT UNIQUE," +
                        "cnpj   TEXT," +
                        "cidade TEXT," +
                        "estado TEXT," +
                        "senha  TEXT)"
        );

        // Informações complementares da ONG
        db.execSQL(
                "CREATE TABLE info_ong (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email     TEXT," +
                        "nome      TEXT," +
                        "endereco  TEXT," +
                        "telefone1 TEXT," +
                        "telefone2 TEXT," +
                        "instagram TEXT," +
                        "pix1      TEXT," +
                        "pix2      TEXT," +
                        "foto      BLOB)"
        );

        // Animais
        db.execSQL(
                "CREATE TABLE animais (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email_ong TEXT," +
                        "nome      TEXT," +
                        "idade     TEXT," +
                        "sexo      TEXT," +
                        "castrado  TEXT," +
                        "raca      TEXT," +
                        "vacina    TEXT," +
                        "condicoes TEXT," +
                        "foto1 BLOB, foto2 BLOB, foto3 BLOB, foto4 BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS ongs");
        db.execSQL("DROP TABLE IF EXISTS info_ong");
        db.execSQL("DROP TABLE IF EXISTS animais");
        onCreate(db);
    }

    // ----------------------------------------------------------------
    //  ONGS (cadastro e login)
    // ----------------------------------------------------------------
    public boolean inserirONG(String nome, String email, String cnpj,
                              String cidade, String estado, String senha) {

        ContentValues v = new ContentValues();
        v.put("nome",   nome);
        v.put("email",  email);
        v.put("cnpj",   cnpj);
        v.put("cidade", cidade);
        v.put("estado", estado);
        v.put("senha",  senha);

        return getWritableDatabase().insert("ongs", null, v) != -1;
    }

    public boolean verificarLogin(String email, String senha) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT 1 FROM ongs WHERE email=? AND senha=?",
                new String[]{ email, senha }
        );
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    // ----------------------------------------------------------------
    //  INFO_ONG  –  insert  OU  update
    // ----------------------------------------------------------------
    public boolean salvarOuAtualizarInfoONG(String email, String nome, String endereco,
                                            String tel1,  String tel2, String insta,
                                            String pix1,  String pix2, byte[] foto) {

        SQLiteDatabase db = getWritableDatabase();

        // verifica se já existe registro para este e‑mail
        Cursor c = db.rawQuery("SELECT id FROM info_ong WHERE email=?", new String[]{email});
        boolean existe = c.moveToFirst();
        c.close();

        ContentValues v = new ContentValues();
        v.put("email",     email);
        v.put("nome",      nome);
        v.put("endereco",  endereco);
        v.put("telefone1", tel1);
        v.put("telefone2", tel2);
        v.put("instagram", insta);
        v.put("pix1",      pix1);
        v.put("pix2",      pix2);
        v.put("foto",      foto);

        long res;
        if (existe) {
            res = db.update("info_ong", v, "email=?", new String[]{email});
        } else {
            res = db.insert("info_ong", null, v);
        }
        return res != -1;
    }

    public InfoOng buscarInfoOng(String email) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM info_ong WHERE email=? ORDER BY id DESC LIMIT 1",
                new String[]{ email }
        );
        InfoOng info = null;
        if (c.moveToFirst()) {
            info = new InfoOng(
                    c.getString(c.getColumnIndexOrThrow("nome")),
                    c.getString(c.getColumnIndexOrThrow("endereco")),
                    c.getString(c.getColumnIndexOrThrow("telefone1")),
                    c.getString(c.getColumnIndexOrThrow("telefone2")),
                    c.getString(c.getColumnIndexOrThrow("instagram")),
                    c.getString(c.getColumnIndexOrThrow("pix1")),
                    c.getString(c.getColumnIndexOrThrow("pix2")),
                    c.getBlob  (c.getColumnIndexOrThrow("foto"))
            );
        }
        c.close();
        return info;
    }

    // ----------------------------------------------------------------
    //  ANIMAIS
    // ----------------------------------------------------------------
    public boolean inserirAnimal(String email, String nome, String idade,
                                 String sexo, String castrado, String raca,
                                 String vacina, String condicoes,
                                 List<byte[]> fotos) {

        ContentValues v = new ContentValues();
        v.put("email_ong", email);
        v.put("nome",      nome);
        v.put("idade",     idade);
        v.put("sexo",      sexo);
        v.put("castrado",  castrado);
        v.put("raca",      raca);
        v.put("vacina",    vacina);
        v.put("condicoes", condicoes);
        for (int i = 0; i < fotos.size() && i < 4; i++)
            v.put("foto" + (i + 1), fotos.get(i));

        try {
            getWritableDatabase().insertOrThrow("animais", null, v);
            return true;
        } catch (Exception e) {          // ← mostra erro real
            Log.e("DB_INSERIR_ANIMAL", "SQLException", e);
            return false;
        }
    }


    public List<Animal> buscarAnimais(String email) {
        List<Animal> lista = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, nome, foto1 FROM animais WHERE email_ong=?",
                new String[]{ email }
        );
        while (c.moveToNext()) {
            lista.add(new Animal(
                    c.getInt   (c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("nome")),
                    c.getBlob  (c.getColumnIndexOrThrow("foto1"))
            ));
        }
        c.close();
        return lista;
    }

    public boolean excluirAnimal(int id) {
        return getWritableDatabase()
                .delete("animais", "id=?", new String[]{ String.valueOf(id) }) > 0;
    }

    public AnimalCompleto buscarAnimalPorId(int id) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM animais WHERE id=? LIMIT 1",
                new String[]{ String.valueOf(id) }
        );
        AnimalCompleto a = null;
        if (c.moveToFirst()) {
            a = new AnimalCompleto(
                    c.getInt   (c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("nome")),
                    c.getString(c.getColumnIndexOrThrow("idade")),
                    c.getString(c.getColumnIndexOrThrow("raca")),
                    c.getString(c.getColumnIndexOrThrow("vacina")),
                    c.getString(c.getColumnIndexOrThrow("sexo")),
                    c.getString(c.getColumnIndexOrThrow("castrado")),
                    c.getString(c.getColumnIndexOrThrow("condicoes")),
                    c.getBlob  (c.getColumnIndexOrThrow("foto1")),
                    c.getBlob  (c.getColumnIndexOrThrow("foto2")),
                    c.getBlob  (c.getColumnIndexOrThrow("foto3")),
                    c.getBlob  (c.getColumnIndexOrThrow("foto4"))
            );
        }
        c.close();
        return a;
    }
}
