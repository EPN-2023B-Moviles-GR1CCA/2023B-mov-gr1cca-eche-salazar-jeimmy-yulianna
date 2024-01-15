import CRUD.ArtistaCRUD
import CRUD.ObraCRUD
import java.util.*

import Entidades.Artista
import Entidades.ObraDeArte
import java.text.SimpleDateFormat

fun main() {

    var op: String
    do{
        println("\n********** MENU *********")
        println("1. Artistas")
        println("2. Obras de arte")
        println("3. Salir")
        println("Seleccionar opción para trabajar")
        op = readLine() ?: ""
        when (op){
            "1" -> menuArtistas()
            "2" -> menuObras()
            "3" -> println("Hasta luego!")
            else -> println("Opción inexistente")
        }
    }while(op != "3")
}

fun menuArtistas(){
    val artistaCRUD = ArtistaCRUD()
    println("\n********** MENU PARA ARTISTAS*********")
    println("1. Agregar nuevo")
    println("2. Leer registros")
    println("3. Actualizar")
    println("4. Eliminar")
    println("5. Regresar")

    when (readLine()){
        "1" -> createArtista()
        "2" -> {
            println("Desea ver:")
            println("1. Toda la lista de artistas")
            println("2. Detalles de un solo artista")

            when (readLine()) {
                "1" -> {
                    val artistas = artistaCRUD.readArtistas()
                    if (artistas.isNotEmpty()) {
                        println("Lista de Artistas:")
                        artistas.forEach { artistaCRUD.mostrarArtista(it) }
                    } else {
                        println("No hay artistas registrados.")
                    }
                }

                "2" -> {
                    val artistas = artistaCRUD.readArtistas()
                    if (artistas.isNotEmpty()) {
                        println("Ingrese el ID del artista que desea ver en detalle:")
                        val idSeleccionado = readLine()?.toIntOrNull() ?: 0
                        val artistaSeleccionado = artistas.find { it.idArtista == idSeleccionado }

                        if (artistaSeleccionado != null) {
                            println("Detalles del Artista Seleccionado:")
                            artistaCRUD.mostrarArtista(artistaSeleccionado)
                        } else {
                            println("No se encontró un artista con el ID seleccionado.")
                        }
                    } else {
                        println("No hay artistas registrados.")
                    }
                }

                else -> println("Opción inválida.")
            }
        }
        "3" -> updateArtista()
        "4" -> deleteArtista()
        "5" -> return
        else -> println("Opción inexistente")
    }
}

fun createArtista(){
    val artistaCRUD = ArtistaCRUD()

    println("Ingrese el id del artista: ")
    val idArtista = readLine()!!.toInt()
    println("Ingrese el nombre del artista: ")
    val nombreArtista = readLine()!!
    println("Ingrese fecha de nacimiento del artista (yyyy-MM-dd): ")
    val fechaNacimientoStr = readLine() ?: ""
    var fechaN: Date? = null

    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        fechaN = dateFormat.parse(fechaNacimientoStr)
    } catch (e: Exception) {
        println("Error al convertir la fecha. Asegúrate de ingresarla en el formato correcto.")
    }
    println("Ingrese cantidad de obras del artista: ")
    val cantidadObras = readLine()!!.toInt()
    println("Ingrese país de nacimiento del artista: ")
    val paisNacimiento = readLine()!!
    println("Ingrese si es internacional el artista (true/false): ")
    val esInternacional = readLine()!!.toBoolean()

    if (fechaN != null) {
        val newArtista = Artista(idArtista, nombreArtista, fechaN, cantidadObras, paisNacimiento, esInternacional)
        artistaCRUD.createArtista(newArtista)

        println("Artista agregado correctamente")
    } else {
        println("No se pudo crear el artista debido a un error en la fecha.")
    }

}
fun updateArtista() {
    val artistaCRUD = ArtistaCRUD()
    println("Ingrese el nombre del artista que desea actualizar:")
    val nombre = readLine() ?: return

    val artistaExistente = artistaCRUD.readArtistas().find { it.nombre.equals(nombre, ignoreCase = true) }
    if (artistaExistente == null) {
        println("No se encuentra al artista.")
        return
    }
    if (artistaExistente != null) {
        println("Se encontró al artista:")
        println(artistaExistente)

        println("Seleccione la propiedad a actualizar:")
        println("1. Nombre")
        println("2. Fecha de nacimiento")
        println("3. Cantidad de obras")
        println("4. País de nacimiento")
        println("5. Es internacional")
        println("6. Cancelar")

        when (readLine()) {
            "1" -> {
                println("Ingrese el nuevo nombre del artista: ")
                val nuevoNombre = readLine() ?: artistaExistente.nombre
                artistaExistente.nombre = nuevoNombre
            }
            "2" -> {
                println("Ingrese la nueva fecha de nacimiento del artista (yyyy-MM-dd): ")
                val fechaNacimientoStr = readLine() ?: ""
                var fechaN: Date? = null

                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    fechaN = dateFormat.parse(fechaNacimientoStr)
                    val fechaNA = fechaN ?: artistaExistente.fechaNacimiento
                    artistaExistente.fechaNacimiento = fechaNA
                } catch (e: Exception) {
                    println("Error al convertir la fecha. Asegúrate de ingresarla en el formato correcto.")
                }
            }
            "3" -> {
                println("Ingrese la nueva cantidad de obras del artista: ")
                val nuevaCantidadObras = readLine()?.toInt() ?: artistaExistente.cantidadObras
                artistaExistente.cantidadObras = nuevaCantidadObras
            }
            "4" -> {
                println("Ingrese el nuevo país de nacimiento del artista: ")
                val nuevoPaisNacimiento = readLine() ?: artistaExistente.paisNacimiento
                artistaExistente.paisNacimiento = nuevoPaisNacimiento
            }
            "5" ->  {
                println("Ingrese si el artista es internacional (true/false): ")
                val esInternacional = readLine()?.toBoolean() ?: artistaExistente.esInternacional
                artistaExistente.esInternacional = esInternacional
            }
            "6" -> {
                println("Operación cancelada.")
            }
            else -> {
                println("Opción inválida.")
            }
        }
    } else {
        println("No se encontró un artista con ese ID.")
    }
    artistaCRUD.updateArtista(artistaExistente.idArtista, artistaExistente)
}

fun deleteArtista(){
    val artistaCRUD = ArtistaCRUD()
    println("Ingrese el id del artista por eliminar:")
    val id = readLine()?.toInt() ?: return
    val artistaAEliminar = artistaCRUD.readArtistas().find { it.idArtista == id }

    if (artistaAEliminar != null) {
        println("Se encontró al artista:")
        println(artistaAEliminar)

        println("¿Está seguro de que desea eliminar este artista? (Sí/No)")
        when (readLine()?.trim()?.toLowerCase()) {
            "si", "sí" -> {
                artistaCRUD.deleteArtista(id)
                println("Artista eliminado con éxito.")
            }
            else -> {
                println("Operación cancelada.")
            }
        }
    } else {
        println("No se encontró un artista con ese ID.")
    }
}
fun menuObras(){
    val artistaCRUD = ArtistaCRUD()
    val obraCRUD = ObraCRUD()
    println("\n**** MENU PARA OBRAS DE ARTE*****")
    println("1. Agregar nuevo")
    println("2. Leer registros")
    println("3. Actualizar")
    println("4. Eliminar")
    println("5. Regresar")

    when (readLine()) {
        "1" ->
            createObra()
        "2" -> {
            println("Desea ver:")
            println("1. Toda la lista de obras de arte")
            println("2. Detalles de una sola obra de arte")

            when (readLine()) {
                "1" -> {
                    val obras = obraCRUD.readObras()
                    if (obras.isNotEmpty()) {
                        println("Lista de Obras de Arte:")
                        obras.forEach { obraCRUD.mostrarObra(it) }
                    } else {
                        println("No hay obras de arte registradas.")
                    }
                }

                "2" -> {
                    val obras = obraCRUD.readObras()
                    if (obras.isNotEmpty()) {
                        println("Ingrese el ID de la obra de arte que desea ver en detalle:")
                        val idSeleccionado = readLine()?.toIntOrNull() ?: 0
                        val obraSeleccionada = obras.find { it.id == idSeleccionado }

                        if (obraSeleccionada != null) {
                            println("Detalles de la Obra de Arte Seleccionada:")
                            obraCRUD.mostrarObra(obraSeleccionada)
                        } else {
                            println("No se encontró una obra de arte con el ID seleccionado.")
                        }
                    } else {
                        println("No hay obras de arte registradas.")
                    }
                }

                else -> println("Opción inválida.")
            }
        }
        "3" -> updateObra()
        "4" -> deleteObra()
        "5" -> return
        else -> println("Opción inexistente")
    }
}

fun createObra() {
    val obraCRUD = ObraCRUD()

    println("Ingrese el ID del artista al que pertenece la obra de arte: ")
    val idArtista = readLine()?.toIntOrNull() ?: 0

    println("Ingrese el ID de la obra de arte: ")
    val idObra = readLine()?.toIntOrNull() ?: 0
    println("Ingrese el título de la obra de arte: ")
    val tituloObra = readLine()!!
    println("Ingrese la disciplina artística de la obra de arte: ")
    val disciplinaObra = readLine()!!
    println("Ingrese el año de creación de la obra de arte: ")
    val anoCreacionObra = readLine()?.toIntOrNull() ?: 0
    println("Ingrese el valor estimado de la obra de arte: ")
    val valorEstimadoObra = readLine()?.toDoubleOrNull() ?: 0.0
    println("La obra de arte es abstracta? (true/false): ")
    val esAbstractaObra = readLine()?.toBoolean() ?: false

    val nuevaObra = ObraDeArte(idObra, tituloObra, disciplinaObra, anoCreacionObra, valorEstimadoObra, esAbstractaObra, idArtista)
    obraCRUD.createObra(nuevaObra, idArtista)
    println("Obra de arte agregada correctamente")
}

fun updateObra() {
    val obraCRUD = ObraCRUD()
    println("Ingrese el ID de la obra de arte que desea actualizar:")
    val idObra = readLine()?.toIntOrNull() ?: return

    val obraExistente = obraCRUD.readObras().find { it.id == idObra }
    if (obraExistente == null) {
        println("No se encuentra la obra de arte.")
        return
    }

    println("Se encontró la obra de arte:")
    obraCRUD.mostrarObra(obraExistente)

    println("Seleccione la propiedad a actualizar:")
    println("1. Título")
    println("2. Disciplina artística")
    println("3. Año de creación")
    println("4. Valor estimado")
    println("5. Es abstracta")
    println("6. ID del artista")
    println("7. Cancelar")

    when (readLine()) {
        "1" -> {
            println("Ingrese el nuevo título de la obra de arte: ")
            val nuevoTitulo = readLine() ?: obraExistente.titulo
            obraExistente.titulo = nuevoTitulo
        }
        "2" -> {
            println("Ingrese la nueva disciplina artística de la obra de arte: ")
            val nuevaDisciplina = readLine() ?: obraExistente.disciplinaArtistica
            obraExistente.disciplinaArtistica = nuevaDisciplina
        }
        "3" -> {
            println("Ingrese el nuevo año de creación de la obra de arte: ")
            val nuevoAnoCreacion = readLine()?.toIntOrNull() ?: obraExistente.anoCreacion
            obraExistente.anoCreacion = nuevoAnoCreacion
        }
        "4" -> {
            println("Ingrese el nuevo valor estimado de la obra de arte: ")
            val nuevoValorEstimado = readLine()?.toDoubleOrNull() ?: obraExistente.valorEstimado
            obraExistente.valorEstimado = nuevoValorEstimado
        }
        "5" -> {
            println("La obra de arte es abstracta? (true/false): ")
            val esAbstracta = readLine()?.toBoolean() ?: obraExistente.esAbstracta
            obraExistente.esAbstracta = esAbstracta
        }
        "6" -> {
            println("Ingrese el nuevo ID del artista al que pertenece la obra de arte: ")
            val nuevoIdArtista = readLine()?.toIntOrNull()
            if (nuevoIdArtista != null) {
                val viejoIdArtista = obraExistente.idArtista
                obraExistente.idArtista = nuevoIdArtista
                // Actualizar la lista de obras en el archivo de artistas
                actualizarObrasEnArtista(viejoIdArtista, nuevoIdArtista, obraExistente)
            } else {
                println("Entrada inválida. No se actualizó el ID del artista.")
            }
        }

        "7" -> {
            println("Operación cancelada.")
        }
        else -> {
            println("Opción inválida.")
        }
    }

    obraCRUD.updateObra(obraExistente.id, obraExistente)
}

fun deleteObra() {
    val obraCRUD = ObraCRUD()
    println("Ingrese el ID de la obra de arte por eliminar:")
    val idObra = readLine()?.toIntOrNull() ?: return

    val obraAEliminar = obraCRUD.readObras().find { it.id == idObra }

    if (obraAEliminar != null) {
        println("Se encontró la obra de arte:")
        obraCRUD.mostrarObra(obraAEliminar)

        println("¿Está seguro de que desea eliminar esta obra de arte? (Sí/No)")
        when (readLine()?.trim()?.toLowerCase()) {
            "si", "sí" -> {
                obraCRUD.deleteObra(idObra)
                println("Obra de arte eliminada con éxito.")
            }
            else -> {
                println("Operación cancelada.")
            }
        }
    } else {
        println("No se encontró una obra de arte con ese ID.")
    }
}

private fun actualizarObrasEnArtista(viejoIdArtista: Int, nuevoIdArtista: Int, obra: ObraDeArte) {
    val artistas = ArtistaCRUD().readArtistas()

    // Buscar el artista antiguo
    val artistaAntiguo = artistas.find { it.idArtista == viejoIdArtista }

    if (artistaAntiguo != null) {
        // Quitar la obra del artista antiguo
        artistaAntiguo.obrasDeArte.removeIf { it.id == obra.id }

        // Guardar los cambios en el archivo de artistas
        val artistaCRUD = ArtistaCRUD()
        artistaCRUD.updateArtista(viejoIdArtista, artistaAntiguo)
    }

    // Buscar el artista nuevo
    val artistaNuevo = artistas.find { it.idArtista == nuevoIdArtista }

    if (artistaNuevo != null) {
        // Añadir la obra al artista nuevo
        artistaNuevo.obrasDeArte.add(obra)

        // Guardar los cambios en el archivo de artistas
        val artistaCRUD = ArtistaCRUD()
        artistaCRUD.updateArtista(nuevoIdArtista, artistaNuevo)
    }
}


