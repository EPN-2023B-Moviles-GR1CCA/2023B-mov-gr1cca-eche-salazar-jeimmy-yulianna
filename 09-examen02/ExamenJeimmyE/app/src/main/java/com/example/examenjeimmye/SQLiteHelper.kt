package com.example.examenjeimmye

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class SQLiteHelper (
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "ExamenJeimmyEche",
    null,
    1
){
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaArtista =
            """
               CREATE TABLE ARTISTA(
               idArtista INTEGER PRIMARY KEY AUTOINCREMENT, 
               nombre VARCHAR(250), 
               fechaNacimiento TEXT,
               cantidadObras INTEGER,
               paisNacimiento VARCHAR(250),
               esInternacional BOOLEANO) 
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaArtista)

        val scriptSQLCrearTablaObra =
            """
               CREATE TABLE OBRA(
               id INTEGER PRIMARY KEY AUTOINCREMENT,
               titulo TEXT,
               disciplinaArtistica TEXT,
               anioCreacion INTEGER,
               valorEstimado REAL,
               esAbstracta BOOLEAN,
               idArtista INTEGER,
               FOREIGN KEY(idArtista) REFERENCES ARTISTA(idArtista)
               ) 
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaObra)
    }

    override fun onUpgrade(p0: SQLiteDatabase?,
                           p1: Int,
                           p2: Int) {}

}