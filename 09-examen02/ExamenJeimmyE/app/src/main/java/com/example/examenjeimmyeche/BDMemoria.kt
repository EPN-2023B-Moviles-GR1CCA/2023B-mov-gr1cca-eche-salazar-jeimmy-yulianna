package com.example.examenjeimmyeche

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class BDMemoria {

    companion object {
        //lateinit var db: FirebaseFirestore
        val artistas = arrayListOf<Artista>()
        val obras = arrayListOf<Obra>()

        /***********ARTISTAS****************/
        fun calcularIdNuevoArtista(): Int{
            val artista = artistas.last()
            if (artista!=null)
                return artista.idArtista + 1
            else
                return 1
        }

        fun crearArtista(
            nombre: String,
            fechaNacimiento: Date,
            cantidadObras: Int,
            paisNacimiento: String,
            esInternacional: Boolean,
        ): Boolean{
            var id = calcularIdNuevoArtista()
            return artistas.add(Artista(id, nombre, fechaNacimiento, cantidadObras, paisNacimiento, esInternacional))
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
            fechaNacimiento: Date,
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

        /*init {

            artistas.add(Artista(1, "Pablo Picasso", Date(1881, 10, 25), 10, "España", false))
            artistas.add(Artista(2, "Frida Kahlo", Date(1907, 7, 6), 10, "México", false))
            artistas.add(Artista(3, "Vincent van Gogh", Date(1853, 3, 30), 10, "Países Bajos", false))
            artistas.add(Artista(4, "Leonardo da Vinci", Date(1452, 4, 15), 10, "Italia", false))
            artistas.add(Artista(5, "Claude Monet", Date(1840, 11, 14), 10, "Francia", false))


            obras.add( Obra(1, 1, "Guernica", "Pintura", 1937, 0.0, true))
            obras.add(Obra(2, 2, "Las dos Fridas", "Pintura", 1939, 0.0, true))
            obras.add(Obra(3, 3, "Noche estrellada", "Pintura", 1889, 0.0, true))
            obras.add(Obra(4, 4, "La última cena", "Pintura", 1498, 0.0, false))
            obras.add(Obra(5, 5, "Nenúfares", "Pintura", 1906, 0.0, true))
        }*/
    }
}