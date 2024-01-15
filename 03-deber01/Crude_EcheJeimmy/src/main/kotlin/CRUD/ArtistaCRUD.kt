package CRUD

import Entidades.Artista
import Entidades.ObraDeArte
import java.io.File
import java.text.SimpleDateFormat

class ArtistaCRUD {

        private val artistaFILE = "C:\\AppMov\\Repositorio\\2023B-mov-gr1cca-eche-salazar-jeimmy-yulianna\\03-deber01\\Crude_EcheJeimmy\\src\\artistas.txt"
        fun createArtista(artista: Artista) {
            val artistas = readArtistas()
            artistas.add(artista)
            saveArtistas(artistas)
        }

    fun readArtistas(): MutableList<Artista> {
        val artistaFile = File(artistaFILE)
        if (!artistaFile.exists()) {
            artistaFile.createNewFile()  // Crea el archivo si no existe
            return mutableListOf()    // Retorna una lista vacía
        }
        return try {
            artistaFile.readLines().mapNotNull { line ->
                val parts = line.split(",")

                if (parts.size < 6) return@mapNotNull null

                try {
                    val idArtista = parts[0].toInt()
                    val obrasDeArtista = readObrasByArtistaId(idArtista)

                    Artista(
                        idArtista = idArtista,
                        nombre = parts[1],
                        fechaNacimiento = SimpleDateFormat("yyyy-MM-dd").parse(parts[2]),
                        cantidadObras = parts[3].toInt(),
                        paisNacimiento = parts[4],
                        esInternacional = parts[5].toBoolean(),
                        obrasDeArte = obrasDeArtista.toMutableList()
                    )
                } catch (e: Exception) {
                    println("Error al procesar línea: $line, Error: $e")
                    null
                }
            }.toMutableList()
        } catch (e: Exception) {
            println("Error al leer el archivo de artistas. Detalles: $e")
            mutableListOf()
        }
    }

    fun readObrasByArtistaId(idArtista: Int): List<ObraDeArte> {
        val obraCRUD = ObraCRUD()

        return obraCRUD.readObras().filter { it.idArtista == idArtista }
    }

    fun updateArtista(id: Int, nuevoArtista: Artista) {
            val artistas = readArtistas().toMutableList()
            val indice = artistas.indexOfFirst { it.idArtista == id }
            if (indice != -1) {
                artistas[indice] = nuevoArtista
                saveArtistas(artistas)
            }
        }

        fun deleteArtista(id: Int) {
            val artistas = readArtistas().toMutableList()
            artistas.removeIf { it.idArtista == id }
            saveArtistas(artistas)
        }

    fun saveArtistas(artistas: List<Artista>) {
        val artistaFile = File(artistaFILE)

        artistaFile.bufferedWriter().use { writer ->
            artistas.forEach { artista ->
                writer.write(
                    "${artista.idArtista},${artista.nombre},${SimpleDateFormat("yyyy-MM-dd").format(artista.fechaNacimiento)}," +
                            "${artista.cantidadObras},${artista.paisNacimiento},${artista.esInternacional}," +
                            "${artista.obrasDeArte.joinToString("/") { it.id.toString() }}\n"
                )
            }
        }
    }
    fun mostrarArtista(artista: Artista) {
        println("ID del Artista: ${artista.idArtista}")
        println("Nombre del Artista: ${artista.nombre}")
        println("Fecha de Nacimiento del Artista: ${artista.fechaNacimiento}")
        println("Cantidad de Obras del Artista: ${artista.cantidadObras}")
        println("País de Nacimiento del Artista: ${artista.paisNacimiento}")
        println("Es Internacional: ${artista.esInternacional}")
        // Mostrar obras de arte del artista
        if (artista.obrasDeArte.isNotEmpty()) {
            println("Obras de Arte del Artista:")
            artista.obrasDeArte.forEach { obra ->
                println("- Título: ${obra.titulo}")
            }
        } else {
            println("Este artista no tiene obras de arte asociadas.")
        }

        println("=======================================")
    }

}