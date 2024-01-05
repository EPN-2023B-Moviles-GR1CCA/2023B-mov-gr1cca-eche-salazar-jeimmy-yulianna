package Entidades

data class ObraDeArte (
    val id: Int,
    val titulo: String,
    val disciplinaArtistica: String,
    val anoCreacion: Int,
    val valorEstimado: Double,
    val esAbstracta: Boolean,
    val idArtista: Int // Identificador del artista asociado a la obra
)