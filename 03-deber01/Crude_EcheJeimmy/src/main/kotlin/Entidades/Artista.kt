package Entidades

import java.util.Date

data class Artista (
    var idArtista: Int,
    var nombre: String,
    var fechaNacimiento: Date,
    var cantidadObras: Int,
    var paisNacimiento: String,
    var esInternacional: Boolean,
    var obrasDeArte: MutableList<ObraDeArte> = mutableListOf()
)