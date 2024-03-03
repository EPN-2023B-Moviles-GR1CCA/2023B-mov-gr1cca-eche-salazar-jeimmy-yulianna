package com.example.examenjeimmyeche

import android.content.ContentValues
import android.content.Context
import java.util.Date

class Obra(
    val id: Int,
    val titulo: String,
    val disciplinaArtistica: String,
    val anioCreacion: Int,
    val valorEstimado: Double,
    val esAbstracta: Boolean,

    val idArtista: Int
) {
    companion object{
        fun obtenerObras(context: Context, idArtista: Int): List<Obra> {
            val db = SQLiteHelper(context).readableDatabase
            val cursor = db.query("OBRA", null, "idArtista = ?", arrayOf(idArtista.toString()), null, null, null)

            val obras = mutableListOf<Obra>()
            with(cursor) {
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow("id"))
                    val nombre = getString(getColumnIndexOrThrow("nombre"))
                    val fecha = getString(getColumnIndexOrThrow("fecha"))
                    val pelicula = getString(getColumnIndexOrThrow("pelicula"))
                    val idActor = getInt(getColumnIndexOrThrow("actor_id"))
                    obras.add(Obra(id, titulo, disciplinaArtistica, idActor))
                }
            }
            cursor.close()
            db.close()
            return personajes
        }
    }

    fun crearPersonaje(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val valores = ContentValues()
        valores.put("id", id)
        valores.put("nombre", this.nombre)
        valores.put("fecha", this.fecha)
        valores.put("pelicula", this.pelicula)
        valores.put("actor_id", this.actor_id)
        val crear = db.insert(
            "PERSONAJE",
            null,
            valores
        )
        db.close()
        return crear.toInt() != -1
    }

    fun actualizarPersonaje(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val seleccion = arrayOf(this.id.toString())
        val valores = ContentValues()
        valores.put("nombre", this.nombre)
        valores.put("fecha", this.fecha)
        valores.put("pelicula", this.pelicula )
        valores.put("actor_id", this.actor_id)
        val actulizar = db.update("PERSONAJE", valores, "id=?", seleccion)
        db.close()
        return actulizar != -1

    }

    fun eliminarPersonaje(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val seleccion = arrayOf(this.id.toString())
        val eliminar = db.delete("PERSONAJE", "id=?", seleccion)
        db.close()
        return eliminar != -1
    }

    override fun toString(): String {
        return "ID: $id\nNombre: $nombre\nFecha: $fecha\nPelícula: $pelicula"
    }
}