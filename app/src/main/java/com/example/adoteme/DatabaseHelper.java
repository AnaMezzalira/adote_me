package com.example.adoteme;

    // Importa bibliotecas necessárias
    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.widget.EditText;
    import android.widget.Spinner;

// Classe que gerencia o banco de dados
    public class DatabaseHelper extends SQLiteOpenHelper {

        // Nome e versão do banco de dados
        private static final String DATABASE_NAME = "Usuarios.db";
        private static final int DATABASE_VERSION = 1;

        // Nome da tabela e das colunas
        public static final String TABLE_NAME = "usuarios";
        public static final String COL_ID = "id";
        public static final String COL_NOME = "nome";
        public static final String COL_EMAIL = "email";
        public static final String COL_SENHA = "senha";
        public static final String COL_CNPJ = "cnpj";
        public static final String COL_ESTADO = "estado";
        public static final String COL_CIDADE = "cidade";

        // Construtor que chama a superclasse
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Cria a tabela do banco de dados
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  // id gerado automaticamente
                    COL_NOME + " TEXT, " +
                    COL_EMAIL + " TEXT UNIQUE, " +                     // email único
                    COL_SENHA + " TEXT, " +
                    COL_CNPJ + " TEXT, " +
                    COL_ESTADO + " TEXT, " +
                    COL_CIDADE + " TEXT)");
        }

        // Atualiza o banco se a versão mudar
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        // Função para inserir um novo usuário
        public boolean inserirUsuario(EditText nome, EditText email, EditText senha, EditText cnpj, Spinner estado, Spinner cidade) {
            SQLiteDatabase db = this.getWritableDatabase(); // abre o banco para escrita
            ContentValues contentValues = new ContentValues(); // estrutura de dados para guardar os valores
            contentValues.put(COL_NOME, String.valueOf(nome));
            contentValues.put(COL_EMAIL, String.valueOf(email));
            contentValues.put(COL_SENHA, String.valueOf(senha));
            contentValues.put(COL_CNPJ, String.valueOf(cnpj));
            contentValues.put(COL_ESTADO, String.valueOf(estado));
            contentValues.put(COL_CIDADE, String.valueOf(cidade));
            long resultado = db.insert(TABLE_NAME, null, contentValues); // insere na tabela

            return resultado != -1; // se resultado for -1, houve erro
        }

        // Função para verificar se o login é válido
        public boolean verificarLogin(String cnpj, String email, String senha) {
            SQLiteDatabase db = this.getReadableDatabase(); // abre o banco para leitura
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COL_EMAIL + "=? AND " + COL_SENHA + "=?", new String[]{email, senha});
            boolean resultado = cursor.getCount() > 0; // se encontrou um registro, o login é válido
            cursor.close();
            return resultado;
        }
    }

