package com.example.examenjeimmyeche

import java.util.Date
import com.google.firebase.database.*

class BDMemoria {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var firedatabase : FirebaseDatabase
        val artistas = arrayListOf<Artista>()
        val obras = arrayListOf<Obra>()

        /***********ARTISTAS****************/
        fun calcularIdNuevoArtista(callback: (Int) -> Unit) {
            database.child("artistas").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val id = snapshot.childrenCount.toInt() + 1
                    callback(id)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores aquí
                }
            })
        }
    fun crearArtista(
        nombre: String,
        fechaNacimiento: Long,
        cantidadObras: Int,
        paisNacimiento: String,
        esInternacional: Boolean,
        callback: (Boolean) -> Unit
    ) {
        calcularIdNuevoArtista { id ->
            val artista = Artista(id, nombre, fechaNacimiento, cantidadObras, paisNacimiento, esInternacional)
            database.child("artistas").child(id.toString()).setValue(artista)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener {
                    callback(false)
                }
        }
    }

        fun borrarArtista(id: Int): Boolean{
            val artista = artistas.filter { it -> it.idArtista==id }.first()
            if(artista != null){
                return artistas.remove(artista)
            }
            else return false

        }
        fun editarArtista(
            id: Int,
            nombre: String,
            fechaNacimiento: Long,
            cantidadObras: Int,
            paisNacimiento: String,
            esInternacional: Boolean,
        ){
            val indice = artistas.indexOfFirst { it.idArtista==id }
            artistas[indice] = Artista(id,nombre,fechaNacimiento, cantidadObras, paisNacimiento, esInternacional)
        }
        /***********OBRAS****************/
        fun calcularIdNuevaObra(): Int{
            val obra = obras.last()
            if( obra!=null)
                return obra.id + 1
            else
                return 1
        }
        fun crearObra(
            idArtista: Int,
            titulo: String,
            disciplinaArtistica: String,
            anioCreacion: Int,
            valorEstimado: Double,
            esAbstracta: Boolean
        ): Boolean{
            var id = calcularIdNuevaObra()
            return obras.add(Obra(id, idArtista, titulo, disciplinaArtistica, anioCreacion, valorEstimado, esAbstracta))
        }
        fun borrarObra(id: Int): Boolean{
            val obra=obras.firstOrNull { it.id==id }
            if (obra!=null){
                return obras.remove(obra)
            }
            else return false
        }
        fun editarObra(
            id: Int,
            idArtista: Int,
            titulo: String,
            disciplinaArtistica: String,
            anioCreacion: Int,
            valorEstimado: Double,
            esAbstracta: Boolean
        ){
            val indice = obras.indexOfFirst { it.id==id }
            obras[indice] = Obra(id,idArtista, titulo, disciplinaArtistica, anioCreacion, valorEstimado, esAbstracta)
        }

        init {


        }

}