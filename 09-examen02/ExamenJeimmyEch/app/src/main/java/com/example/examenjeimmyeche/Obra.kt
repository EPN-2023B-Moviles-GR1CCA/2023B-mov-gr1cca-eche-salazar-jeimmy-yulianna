package com.example.examenjeimmyeche

import java.util.Date

class Obra(
    val id: Int,
    val idArtista: Int,
    val titulo: String,
    val disciplinaArtistica: String,
    val anioCreacion: Int,
    val valorEstimado: Double,
    val esAbstracta: Boolean
) {
    init {
        this.id; id;
        this.idArtista; idArtista;
        this.titulo; titulo;
        this.disciplinaArtistica; disciplinaArtistica;
        this.anioCreacion; anioCreacion;
        this.valorEstimado; valorEstimado;
        this.esAbstracta; esAbstracta;
    }

    override fun toString(): String {
        return "${id}\t${titulo}"
    }
}