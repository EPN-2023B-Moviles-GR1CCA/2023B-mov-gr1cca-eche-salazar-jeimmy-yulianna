package com.example.examenjeimmyeche

class Artista(
    val idArtista: Int,
    val nombre: String,
    val fechaNacimiento: Long,
    val cantidadObras: Int,
    val paisNacimiento: String,
    val esInternacional: Boolean,
) {
    init {
        this.idArtista; idArtista;
        this.nombre; nombre;
        this.fechaNacimiento; fechaNacimiento;
        this.cantidadObras; cantidadObras;
        this.paisNacimiento; paisNacimiento;
        this.esInternacional; esInternacional;

    }
    override fun toString(): String {
        return "${idArtista}\t${nombre}"
    }
}