package com.example.examenjeimmye

import android.content.ContentValues
import android.content.Context

class Artista(
    val idArtista: Int?,
    val nombre: String,
    val fechaNacimiento: String,
    val cantidadObras: Int,
    val paisNacimiento: String,
    val esInternacional: Boolean
){
    companion object {
        fun obtenerArtistas(context: Context): List<Artista> {
            val db = SQLiteHelper(context).readableDatabase
            val cursor = db.query("ARTISTA", null, null, null, null, null, null)

            val artistas = mutableListOf<Artista>()
            with(cursor) {
                while (moveToNext()) {
                    val idArtista = getInt(getColumnIndexOrThrow("idArtista"))
                    val nombre = getString(getColumnIndexOrThrow("nombre"))
                    val fechaNacimiento = getString(getColumnIndexOrThrow("fechaNacimiento"))
                    val cantidadObras = getInt(getColumnIndexOrThrow("cantidadObras"))
                    val paisNacimiento = getString(getColumnIndexOrThrow("paisNacimiento"))
                    val esInternacional =
                        getInt(getColumnIndexOrThrow("esInternacional")) != 0 // Convertir Int a Boolean
                    artistas.add(Artista(idArtista, nombre, fechaNacimiento, cantidadObras, paisNacimiento, esInternacional))
                }
            }
            cursor.close()
            db.close()
            return artistas
        }
    }

    fun crearArtista(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val contenedor = ContentValues()
        contenedor.put("idArtista", idArtista)
        contenedor.put("nombre", this.nombre)
        contenedor.put("fechaNacimiento", this.fechaNacimiento)
        contenedor.put("cantidadObras", this.cantidadObras)
        contenedor.put("paisNacimiento", this.paisNacimiento)
        contenedor.put("esInternacional", if (this.esInternacional) 1 else 0)
        val crear = db.insert(
            "ARTISTA",
            null,
            contenedor
        )
        db.close()
        return crear.toInt() != -1
    }

    fun eliminarArtista(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val seleccion = arrayOf(this.idArtista.toString())
        val eliminar = db.delete("ARTISTA", "idArtista=?", seleccion)
        db.close()
        return eliminar != -1
    }

    fun actualizarArtista(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val seleccion = arrayOf(this.idArtista.toString())
        val contenedor = ContentValues()
        contenedor.put("nombre", this.nombre)
        contenedor.put("fechaNacimiento", this.fechaNacimiento)
        contenedor.put("cantidadObras", this.cantidadObras)
        contenedor.put("paisNacimiento", this.paisNacimiento)
        contenedor.put("esInternacinal", if (this.esInternacional) 1 else 0)
        val actulizar = db.update("ARTISTA", contenedor, "idArtista=?", seleccion)
        db.close()
        return actulizar != -1

    }

    override fun toString(): String {
        val esInternacional = if(this.esInternacional) "si" else "no"
        return "IdArtista: $idArtista\n" +
                "Nombre: $nombre\n" +
                "FechaNacimiento: $fechaNacimiento\n" +
                "CantidadObras: $cantidadObras\n" +
                "paisNacimiento: $paisNacimiento\n" +
                "\nesInternacional: $esInternacional\n"
    }



}