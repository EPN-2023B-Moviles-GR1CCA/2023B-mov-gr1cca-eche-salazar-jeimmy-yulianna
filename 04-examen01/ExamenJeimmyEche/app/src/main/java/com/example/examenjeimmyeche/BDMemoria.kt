package com.example.examenjeimmyeche

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date

var BD="baseDatos";
class BDMemoria (contexto: Context):SQLiteOpenHelper(contexto, BD, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        var sql="create table tabla (idArtista INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "nombre VARCHAR(250), " +
                "fechaNacimiento TEXT, " +
                "cantidadObras INTEGER," +
                "paisNacimiento VARCHAR(250)," +
                "esInternacional BOOLEANO)";
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun crearArtista(artista:Artista): String {
        //prepara la base de datos
        val db=this.writableDatabase
        var contenedor = ContentValues();

        contenedor.put("nombre", artista.nombre)
        // Formatea la fecha como cadena antes de almacenarla en la base de datos
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val fechaFormateada = dateFormat.format(artista.fechaNacimiento)
        contenedor.put("fechaNacimiento", fechaFormateada)
        contenedor.put("cantidadObras", artista.cantidadObras)
        contenedor.put("paisNacimiento", artista.paisNacimiento)
        contenedor.put("esInternacional", artista.esInternacional)

        var resultado=db.insert("Artista", null, contenedor)
        if (resultado==-1.toLong()){
            return "existión falla en la Base de Datos"
        }else{
            return "Artista creado con éxito"
        }
    }
    /* fun listarDatos():MutableList<Artista>{
        var lista:MutableList<Artista> =ArrayList()
        val db=this.readableDatabase
        val sql= "select * from Artista";
        var resultado= db.rawQuery(sql,null)

        if(resultado.moveToFirst()){
            do{
                var arti= Artista()
                arti.idArtista=resultado.getString(resultado.getColumnIndex("idArtista")).toInt()
            }
        }
    }*/
    fun actualizar(idArtista: String, nombre: String, fechaNacimiento: Date, cantidadObras: Int, paisNacimiento: String, esInternacional: String ) {
        val db=this.writableDatabase
        var contenedor = ContentValues();
        contenedor.put("nombre",  nombre)
        // Formatea la fecha como cadena antes de almacenarla en la base de datos
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val fechaFormateada = dateFormat.format(fechaNacimiento)
        contenedor.put("fechaNacimiento", fechaFormateada)
        contenedor.put("cantidadObras", cantidadObras)
        contenedor.put("paisNacimiento", paisNacimiento)
        contenedor.put("esInternacional", esInternacional)

        db.update("Artista",contenedor, "idArtista=?", arrayOf(idArtista))

    }

    fun borrarArtista(idArtista: String){
        val db=this.writableDatabase
        if(idArtista.length>0){
            db.delete("Artista", "idArtista=?", arrayOf(idArtista))
        }
    }

}