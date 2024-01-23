package com.example.examenjeimmyeche

import java.util.Date

class BDMemoria {
    companion object {
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

        init {

            artistas.add(Artista(1,"Artista1",Date(2000,1,1),10,"Ecuador",true))
            artistas.add(Artista(2,"Artista2",Date(2000,1,1),10,"Colombia",true))
            artistas.add(Artista(3,"Artista3",Date(2000,1,1),10,"Venezuela",true))
            artistas.add(Artista(4,"Artista4",Date(2000,1,1),10,"Argentina",true))
            artistas.add(Artista(5,"Artista5",Date(2000,1,1),10,"España",true))

            obras.add(Obra(1,1,"obra1", "oleo",2000,20.0,true))
            obras.add(Obra(2,1,"obra2", "oleo",2001,21.0,false))
            obras.add(Obra(3,1,"obra3", "oleo",2002,22.0,true))
            obras.add(Obra(4,2,"obra4", "oleo",2003,23.0,false))
            obras.add(Obra(5,2,"obra5", "oleo",2004,24.0,true))
            obras.add(Obra(6,2,"obra6", "oleo",2005,25.0,false))
            obras.add(Obra(7,3,"obra7", "oleo",2006,26.0,true))
            obras.add(Obra(8,3,"obra8", "oleo",2007,27.0,false))
            obras.add(Obra(9,3,"obra9", "oleo",2008,28.0,true))
        }
    }
}