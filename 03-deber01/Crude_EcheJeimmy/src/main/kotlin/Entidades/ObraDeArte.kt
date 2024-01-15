package Entidades

data class ObraDeArte (
    var id: Int,
    var titulo: String,
    var disciplinaArtistica: String,
    var anoCreacion: Int,
    var valorEstimado: Double,
    var esAbstracta: Boolean,
    var idArtista: Int
)