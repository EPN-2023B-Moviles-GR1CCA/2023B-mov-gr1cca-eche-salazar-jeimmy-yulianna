package com.example.examenjeimmye

import android.content.ContentValues
import android.content.Context

class Obra (
    val id: Int?,
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
                    val titulo = getString(getColumnIndexOrThrow("titulo"))
                    val disciplinaArtistica = getString(getColumnIndexOrThrow("disciplinaArtistica"))
                    val anioCreacion = getInt(getColumnIndexOrThrow("anioCreacion"))
                    val valorEstimado = getDouble(getColumnIndexOrThrow("valorEstimado"))
                    val esAbstracta = getInt(getColumnIndexOrThrow("esAbstracta")) != 0 // Convertir Int a Boolean
                    val idActor = getInt(getColumnIndexOrThrow("actor_id"))
                    obras.add(Obra(id, titulo, disciplinaArtistica, anioCreacion, valorEstimado, esAbstracta, idArtista))
                }
            }
            cursor.close()
            db.close()
            return obras
        }
    }

    fun crearObra(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val contenedor = ContentValues()
        contenedor.put("id", id)
        contenedor.put("titulo", this.titulo)
        contenedor.put("disciplinaArtistica", this.disciplinaArtistica)
        contenedor.put("anioCreacion", this.anioCreacion)
        contenedor.put("valorEstimado", this.valorEstimado)
        contenedor.put("esAbstracta", if (this.esAbstracta) 1 else 0)
        contenedor.put("idArtista", this.idArtista)
        val crear = db.insert(
            "OBRA",
            null,
            contenedor
        )
        db.close()
        return crear.toInt() != -1
    }

    fun actualizarObra(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val seleccion = arrayOf(this.id.toString())
        val contenedor = ContentValues()
        contenedor.put("titulo", this.titulo)
        contenedor.put("disciplinaArtistica", this.disciplinaArtistica)
        contenedor.put("anioCreacion", this.anioCreacion)
        contenedor.put("valorEstimado", this.valorEstimado)
        contenedor.put("esAbstracta", this.esAbstracta)
        contenedor.put("idArtista", this.idArtista)
        val actulizar = db.update("OBRA", contenedor, "id=?", seleccion)
        db.close()
        return actulizar != -1

    }

    fun eliminarObra(context: Context): Boolean {
        val db = SQLiteHelper(context).writableDatabase
        val seleccion = arrayOf(this.id.toString())
        val eliminar = db.delete("OBRA", "id=?", seleccion)
        db.close()
        return eliminar != -1
    }

    override fun toString(): String {
        val esAbstracta = if(this.esAbstracta) "si" else "no"
        return "ID: $id\n" +
                "Titulo: $titulo\n" +
                "DisciplinaArtistica: $disciplinaArtistica\n" +
                "anioCreacion: $anioCreacion\n" +
                "valorEstimado: $valorEstimado\n" +
                "esAbstracta: $esAbstracta\n"
    }
}

