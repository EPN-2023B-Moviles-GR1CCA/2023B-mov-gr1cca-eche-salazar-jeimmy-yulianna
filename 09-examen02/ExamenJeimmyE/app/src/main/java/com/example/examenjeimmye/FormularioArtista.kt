package com.example.examenjeimmye

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date



class FormularioArtista: AppCompatActivity() {

    companion object {
        private const val TAG = "FormularioArtista"
    }

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_artista)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fun mostrarSnackbar(texto: String) {
            Snackbar
                .make(
                    findViewById(R.id.ly_form_artista), // view
                    texto, // texto
                    Snackbar.LENGTH_LONG // tiempo
                )
                .show()
        }

        fun irActividad(clase: Class<*>) {
            val intent = Intent(this, clase)
            startActivity(intent)
        }

        val botonCrearArtista = findViewById<Button>(R.id.btn_guardar_artista)
        botonCrearArtista.setOnClickListener {
            val ptID = findViewById<EditText>(R.id.pt_id_artista)
            val ptNombre = findViewById<EditText>(R.id.pt_nombre_artista)
            val ptFecha = findViewById<EditText>(R.id.td_nacimiento_artista)
            val ptPais = findViewById<EditText>(R.id.pt_pais_artista)
            val ptObras = findViewById<EditText>(R.id.pt_obras_artista)
            val swInternacional = findViewById<Switch>(R.id.sw_internacional_artista)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy")

            var fechaNacimientoDate: Date? = null
            try {
                fechaNacimientoDate = dateFormat.parse(ptFecha.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (fechaNacimientoDate != null) {
                // Crear un mapa con los datos del artista
                val artista = hashMapOf(
                    "ptID" to ptID.text.toString().toInt(),
                    "ptNombre" to ptNombre.text.toString(),
                    "ptFecha" to ptFecha.text.toString(),
                    "ptObras" to ptObras.text.toString().toInt(),
                    "ptPais" to ptPais.text.toString(),
                    "swInternacional" to swInternacional.isChecked()
                )
                val artista1 = Artista(
                    null,
                    ptNombre.text.toString(),
                    ptFecha.text.toString(),
                    ptObras.text.toString().toInt(),
                    ptPais.text.toString(),
                    swInternacional.isChecked()
                )
                val respuesta = artista1.crearArtista(this)
                if (respuesta) {
                    mostrarSnackbar("Artista: \"${artista1.nombre}\" ha sido creado")
                    ptNombre.setText(artista1.nombre.toString())
                    ptFecha.setText(artista1.fechaNacimiento.toString())
                    ptPais.setText(artista1.paisNacimiento.toString())
                    ptObras.setText(artista1.cantidadObras.toString())
                    swInternacional.isChecked = artista1.esInternacional

                    // Agregar el artista a Firestore
                    db.collection("artistas")
                        .add(artista)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Artista creado con ID: ${documentReference.id}")
                            mostrarSnackbar("Se ha creado el artista ${ptNombre.text}")
                            irActividad(ArtistaBD::class.java)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error al crear el artista", e)
                        }
                } else {
                    mostrarSnackbar("Fecha de nacimiento no válida")
                }
            }
        }
    }
}