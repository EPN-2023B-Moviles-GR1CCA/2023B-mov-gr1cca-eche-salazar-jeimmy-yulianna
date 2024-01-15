package CRUD

import Entidades.ObraDeArte
import java.io.File

class ObraCRUD {

    private val obraFILE = "C:\\AppMov\\Repositorio\\2023B-mov-gr1cca-eche-salazar-jeimmy-yulianna\\03-deber01\\Crude_EcheJeimmy\\src\\obras.txt"

    fun createObra(obra: ObraDeArte, idArtista: Int) {
        val obras = readObras()
        val artistaCRUD = ArtistaCRUD()

        // Verificar si el artista existe
        val artistaExistente = artistaCRUD.readArtistas().find { it.idArtista == idArtista }

        if (artistaExistente != null) {
            // Asociar la obra al artista
            obra.idArtista = idArtista
            obras.add(obra)
            saveObras(obras)

            // También podrías actualizar la lista de obras del artista si lo deseas
            artistaExistente.obrasDeArte.add(obra)
            val artistas = artistaCRUD.readArtistas().toMutableList()
            artistas.removeIf { it.idArtista == idArtista }
            artistas.add(artistaExistente)
            artistaCRUD.saveArtistas(artistas)

            println("Obra de arte asociada al artista '${artistaExistente.nombre}' correctamente.")
        } else {
            println("No se encontró un artista con el ID '$idArtista'. La obra no se asoció a ningún artista.")
        }
    }

    fun readObras(): MutableList<ObraDeArte> {
        val obraFile = File(obraFILE)
        if (!obraFile.exists()) {
            obraFile.createNewFile()  // Crea el archivo si no existe
            return mutableListOf()    // Retorna una lista vacía
        }
        return obraFile.readLines().mapNotNull { line ->
                val parts = line.split(",")
                if (parts.size < 6) return@mapNotNull null

                try {
                    ObraDeArte(
                        id = parts[0].toInt(),
                        titulo = parts[1],
                        disciplinaArtistica = parts[2],
                        anoCreacion = parts[3].toInt(),
                        valorEstimado = parts[4].toDouble(),
                        esAbstracta = parts[5].toBoolean(),
                        idArtista = parts[6].toInt()
                    )
                }catch (e: Exception) {
                    null
                }
        }.toMutableList()
    }

    fun updateObra(id: Int, nuevaObra: ObraDeArte) {
        val obras = readObras().toMutableList()
        val indice = obras.indexOfFirst { it.id == id }
        if (indice != -1) {
            obras[indice] = nuevaObra
            saveObras(obras)
        }
    }

    fun deleteObra(id: Int) {
        val obras = readObras().toMutableList()
        obras.removeIf { it.id == id }
        saveObras(obras)
    }

    private fun saveObras(obras: List<ObraDeArte>) {
        val obraFile = File(obraFILE)
        obraFile.bufferedWriter().use { writer ->
            obras.forEach { obra ->
                writer.write(
                    "${obra.id},${obra.titulo},${obra.disciplinaArtistica}," +
                            "${obra.anoCreacion},${obra.valorEstimado},${obra.esAbstracta},${obra.idArtista}\n"
                )
            }
        }
    }

    fun mostrarObra(obra: ObraDeArte) {
        val artistas = ArtistaCRUD()
        println("ID de la Obra: ${obra.id}")
        println("Título de la Obra: ${obra.titulo}")
        println("Disciplina Artística: ${obra.disciplinaArtistica}")
        println("Año de Creación: ${obra.anoCreacion}")
        println("Valor Estimado: ${obra.valorEstimado}")
        println("Es Abstracta: ${obra.esAbstracta}")
        // Buscar el nombre del artista correspondiente al ID
        val nombreArtista = artistas.readArtistas().find { it.idArtista == obra.idArtista }?.nombre ?: "Desconocido"
        println("Artista: $nombreArtista")
        println("=======================================")
    }
}
