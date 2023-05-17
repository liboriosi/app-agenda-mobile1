package com.mobile.tabalho.gerenciador_contatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "agenda";
    private static final String TABLE_CONTATOS = "contatos";
    private static final String TABLE_NUMEROS = "numeros";

    private static final String COL_CONTATO_ID = "_id";
    private static final String COL_CONTATO_NOME = "nome";
    private static final String CREATE_TABLE_CONTATOS = "CREATE TABLE " + TABLE_CONTATOS + " (" +
            COL_CONTATO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_CONTATO_NOME + " VARCHAR(100))";

    private static final String COL_NUMERO_ID = "_id";
    private static final String COL_NUMERO_CONTATO_ID = "contato_id";
    private static final String COL_NUMERO_NUMERO = "numero";
    private static final String COL_NUMERO_TIPO = "tipo";
    private static final String CREATE_TABLE_NUMEROS = "CREATE TABLE " + TABLE_NUMEROS + " (" +
            COL_NUMERO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NUMERO_CONTATO_ID + " INTEGER, " +
            COL_NUMERO_NUMERO + " VARCHAR(100), " +
            COL_NUMERO_TIPO + " VARCHAR(100), " +
            "FOREIGN KEY (" + COL_NUMERO_CONTATO_ID + ") REFERENCES " + TABLE_CONTATOS + " (" + COL_CONTATO_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTATOS);
        db.execSQL(CREATE_TABLE_NUMEROS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMEROS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTATOS);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public long createContato(Contato contato) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CONTATO_NOME, contato.getNome());
        long contatoId = db.insert(TABLE_CONTATOS, null, cv);

        for (Numero numero : contato.getNumeros()) {
            ContentValues numeroCv = new ContentValues();
            numeroCv.put(COL_NUMERO_CONTATO_ID, contatoId);
            numeroCv.put(COL_NUMERO_NUMERO, numero.getNumero());
            numeroCv.put(COL_NUMERO_TIPO, numero.getTipo());
            db.insert(TABLE_NUMEROS, null, numeroCv);
        }

        db.close();
        return contatoId;
    }

    public void updateContato(Contato contato) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_CONTATO_NOME, contato.getNome());

        db.update(TABLE_CONTATOS, cv, COL_CONTATO_ID + " = ?", new String[]{String.valueOf(contato.getId())});

        db.delete(TABLE_NUMEROS, COL_NUMERO_CONTATO_ID + " = ?", new String[]{String.valueOf(contato.getId())});

        for (Numero numero : contato.getNumeros()) {
            ContentValues numeroCv = new ContentValues();
            numeroCv.put(COL_NUMERO_CONTATO_ID, contato.getId());
            numeroCv.put(COL_NUMERO_NUMERO, numero.getNumero());
            numeroCv.put(COL_NUMERO_TIPO, numero.getTipo());
            db.insert(TABLE_NUMEROS, null, numeroCv);
        }

        db.close();
    }

    public void deleteContato(long contatoId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NUMEROS, COL_NUMERO_CONTATO_ID + " = ?", new String[]{String.valueOf(contatoId)});
        db.delete(TABLE_CONTATOS, COL_CONTATO_ID + " = ?", new String[]{String.valueOf(contatoId)});

        db.close();
    }

    public void deleteNumero(long numeroId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NUMEROS, COL_NUMERO_ID + " = ?", new String[]{String.valueOf(numeroId)});
        db.close();
    }

    public List<Contato> getAllContatos() {
        List<Contato> contatos = new ArrayList<>();

        String selectQuery = "SELECT " + COL_CONTATO_ID + ", " + COL_CONTATO_NOME +
                " FROM " + TABLE_CONTATOS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int contatoIdIndex = cursor.getColumnIndex(COL_CONTATO_ID);
            int nomeIndex = cursor.getColumnIndex(COL_CONTATO_NOME);
            do {
                long contatoId = cursor.getLong(contatoIdIndex);
                String nome = cursor.getString(nomeIndex);

                Contato contato = new Contato(nome, null);
                contato.setId(contatoId);

                contatos.add(contato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return contatos;
    }

    public Contato getContatoById(long contatoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contato contato = null;

        String selectQuery = "SELECT * FROM " + TABLE_CONTATOS +
                " WHERE " + COL_CONTATO_ID + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(contatoId)});

        if (cursor.moveToFirst()) {
            int nomeIndex = cursor.getColumnIndex(COL_CONTATO_NOME);
            String nome = cursor.getString(nomeIndex);
            contato = new Contato(nome, null);
            contato.setId(contatoId);
        }

        cursor.close();

        if (contato != null) {
            selectQuery = "SELECT * FROM " + TABLE_NUMEROS +
                    " WHERE " + COL_NUMERO_CONTATO_ID + " = ?";

            cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(contatoId)});

            if (cursor.moveToFirst()) {
                int numeroIndex = cursor.getColumnIndex(COL_NUMERO_NUMERO);
                int tipoIndex = cursor.getColumnIndex(COL_NUMERO_TIPO);

                List<Numero> numeros = new ArrayList<>();
                do {
                    String numero = cursor.getString(numeroIndex);
                    String tipo = cursor.getString(tipoIndex);
                    Numero numeroTelefone = new Numero(numero, tipo);
                    numeros.add(numeroTelefone);
                } while (cursor.moveToNext());

                contato.setNumeros(numeros);
            }

            cursor.close();
        }

        db.close();

        return contato;
    }
}
