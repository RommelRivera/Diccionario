package com.rommelrivera.diccionario

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context : Context) : SQLiteOpenHelper(context, "diccionario", null, 2) {
    private var writeDB : SQLiteDatabase = writableDatabase
    private var readDB : SQLiteDatabase = readableDatabase

    override fun onCreate(db : SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON;")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS tiposPalabras (" +
                    "idTipoPalabra INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL" +
                ");"
        )
        db.execSQL("CREATE TABLE IF NOT EXISTS idiomas (" +
                            "idIdioma INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nombre TEXT NOT NULL," +
                            "colBoton TEXT NOT NULL DEFAULT '#666666'," +
                            "colFondo TEXT NOT NULL DEFAULT '#FDF6FE'," +
                            "colTexto TEXT NOT NULL DEFAULT '#444444'," +
                            "colBotonTexto TEXT NOT NULL DEFAULT '#FFFFFF'," +
                            "colDelete TEXT NOT NULL DEFAULT '#BB1010'," +
                            "colDeleteIcon TEXT NOT NULL DEFAULT '#FFFFFF'" +
                        ");"
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS palabras (" +
                    "idPalabra INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "idIdioma INTEGER REFERENCES idiomas(idIdioma) NOT NULL," +
                    "idTipoPalabra INTEGER REFERENCES tiposPalabras(idTipoPalabra) NOT NULL," +
                    "definicion TEXT NOT NULL" +
                ");"
        )

        val tiposPalabras : Array<String> = TipoPalabra().tiposPalabras.values.toTypedArray()
        for (i in tiposPalabras.indices) {
            val values : ContentValues = ContentValues()
            values.put("nombre", tiposPalabras[i])
            db.insert("tiposPalabras", null, values)
        }
    }

    override fun onUpgrade(db : SQLiteDatabase, oldVersion : Int, newVersion : Int) {
        db.execSQL("DROP TABLE palabras;")
        db.execSQL("DROP TABLE tiposPalabras;")
        db.execSQL("DROP TABLE idiomas;")
        onCreate(db)
    }

    fun init() {

    }

    override fun onDowngrade(db : SQLiteDatabase, oldVersion : Int, newVersion : Int) {
        db.execSQL("DROP TABLE palabras;")
        db.execSQL("DROP TABLE tiposPalabras;")
        db.execSQL("DROP TABLE idiomas")
        onCreate(db)
    }

    fun iteratorIdioma(cursor : Cursor) : Idioma {
        val idIdioma : Int = cursor.getInt(0)
        val nombre : String = cursor.getString(1)
        val colBoton : String = cursor.getString(2)
        val colFondo : String = cursor.getString(3)
        val colTexto : String = cursor.getString(4)
        val colBotonTexto : String = cursor.getString(5)
        val colDelete : String = cursor.getString(6)
        val colDeleteIcon : String = cursor.getString(7)

        return Idioma(idIdioma, nombre, colBoton, colFondo, colTexto, colBotonTexto, colDelete, colDeleteIcon)
    }

    fun iteratorIdioma(cursor : Cursor, idiomas : Array<Idioma>) : Array<Idioma> {
        for (i in -1..<cursor.count) {
            if (i == -1) continue
            cursor.moveToNext()
            idiomas[i] = iteratorIdioma(cursor)
        }
        cursor.close()

        return idiomas
    }

    fun selectIdiomas() : Array<Idioma> {
        var cursor : Cursor = readDB.rawQuery("SELECT * FROM idiomas;", null)
        var idiomas : Array<Idioma> = Array(cursor.count) { Idioma(-1, "", "", "", "", "", "", "") }

        return iteratorIdioma(cursor, idiomas)
    }

    fun selectIdiomaById(idIdioma : Int) : Idioma {
        var cursor : Cursor = readDB.rawQuery("SELECT * FROM idiomas WHERE idIdioma = ?;", arrayOf(idIdioma.toString()))
        cursor.moveToNext()
        var idioma : Idioma = iteratorIdioma(cursor)
        cursor.close()

        return idioma
    }

    fun selectIdiomaByNombre(nombre : String) : Idioma {
        var cursor : Cursor = readDB.rawQuery("SELECT * FROM idiomas WHERE nombre = ?;", arrayOf(nombre))
        cursor.moveToNext()
        var idioma : Idioma = iteratorIdioma(cursor)
        cursor.close()

        return idioma
    }

    fun insertIdioma(nombre : String, colBoton : String, colFondo : String, colTexto : String, colBotonTexto : String, colDelete : String, colDeleteIcon : String) : Long {
        var values : ContentValues = ContentValues()
        values.put("nombre", nombre)
        values.put("colBoton", colBoton)
        values.put("colFondo", colFondo)
        values.put("colTexto", colTexto)
        values.put("colBotonTexto", colBotonTexto)
        values.put("colDelete", colDelete)
        values.put("colDeleteIcon", colDeleteIcon)
        return writeDB.insert("idiomas", null, values)
    }

    fun updateIdioma(idIdioma : Int, nombre : String, colBoton : String, colFondo : String, colTexto : String, colBotonTexto : String, colDelete : String, colDeleteIcon : String) : Int {
        var values : ContentValues = ContentValues()
        values.put("nombre", nombre)
        values.put("colBoton", colBoton)
        values.put("colFondo", colFondo)
        values.put("colTexto", colTexto)
        values.put("colBotonTexto", colBotonTexto)
        values.put("colDelete", colDelete)
        values.put("colDeleteIcon", colDeleteIcon)
        return writeDB.update("idiomas", values, "idIdioma = ?", arrayOf(idIdioma.toString()))
    }

    fun deleteIdioma(nombre : String) : Int {
        writeDB.delete("palabras", "idIdioma = ?", arrayOf(selectIdiomaByNombre(nombre).idIdioma.toString()))
        return writeDB.delete("idiomas", "nombre = ?", arrayOf<String>(nombre))
    }

    fun checkIdioma(nombre : String) : Boolean {
        var cursor : Cursor = readDB.rawQuery("SELECT * FROM idiomas WHERE nombre = ?;", arrayOf<String>(nombre))
        var exists : Boolean = cursor.moveToNext()
        cursor.close()

        return exists
    }

    fun iteratorPalabra(cursor : Cursor) : Palabra {
        val idPalabra : Int = cursor.getInt(0)
        val nombre : String = cursor.getString(1)
        val idIdioma : Int = cursor.getInt(2)
        val idTipoPalabra : Int = cursor.getInt(3)
        val definicion : String = cursor.getString(4)

        return Palabra(idPalabra, nombre, idIdioma, idTipoPalabra, definicion)
    }

    fun iteratorPalabra(cursor : Cursor, palabras : Array<Palabra>) : Array<Palabra> {
        for (i in -1..<cursor.count) {
            if (i == -1) continue
            cursor.moveToNext()
            palabras[i] = iteratorPalabra(cursor)
        }
        cursor.close()

        return palabras
    }

    fun selectPalabras(idIdioma : Int) : Array<Palabra> {
        val cursor : Cursor = readDB.rawQuery("SELECT * FROM palabras WHERE idIdioma = ? ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(nombre, ?, ?), ?, ?), ?, ?), ?, ?) COLLATE NOCASE;", arrayOf<String>(idIdioma.toString(),"*", "", "(", "", ")", "", "-", ""))
        var palabras : Array<Palabra> = Array(cursor.count) { Palabra(-1, "", -1, -1, "") }

        return iteratorPalabra(cursor, palabras)
    }

    fun selectPalabras(texto : String) : Array<Palabra> {
        val cursor : Cursor = readDB.rawQuery("SELECT * FROM palabras WHERE palabra LIKE ? OR definicion LIKE ?;", arrayOf<String>("%" + texto + "%", "%" + texto + "%"))
        var palabras : Array<Palabra> = emptyArray()

        return iteratorPalabra(cursor, palabras)
    }

    fun selectPalabraById(idPalabra : Int) : Palabra {
        val cursor : Cursor = readDB.rawQuery("SELECT * FROM palabras WHERE idPalabra = ?;", arrayOf<String>(idPalabra.toString()))
        cursor.moveToNext()
        val palabra : Palabra = iteratorPalabra(cursor)
        cursor.close()

        return palabra
    }

    fun insertPalabra(nombre : String, idIdioma : Int, idTipoPalabra : Int, definicion : String) : Long {
        val values : ContentValues = ContentValues()
        values.put("nombre", nombre)
        values.put("idIdioma", idIdioma)
        values.put("idTipoPalabra", idTipoPalabra)
        values.put("definicion", definicion)
        return writeDB.insert("palabras", null, values)
    }

    fun updatePalabra(idPalabra : Int, nombre : String, idTipoPalabra : Int, definicion : String): Int {
        val values : ContentValues = ContentValues()
        values.put("nombre", nombre)
        values.put("idTipoPalabra", idTipoPalabra)
        values.put("definicion", definicion)
        return writeDB.update("palabras", values, "idPalabra = ?;", arrayOf<String>(idPalabra.toString())
        )
    }

    fun deletePalabra(idPalabra: Int): Int {
        return writeDB.delete("palabras", "idPalabra = ?;", arrayOf<String>(idPalabra.toString()))
    }
}
