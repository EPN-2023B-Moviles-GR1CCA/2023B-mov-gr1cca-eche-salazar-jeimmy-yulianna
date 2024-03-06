package com.example.examenjeimmye

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class InfoArtista : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_artista)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ptID = findViewById<EditText>(R.id.pt_id_artista)
        val ptNombre = findViewById<EditText>(R.id.pt_nombre_artista)
        val ptFecha = findViewById<EditText>(R.id.td_nacimiento_artista)
        val ptObras = findViewById<EditText>(R.id.pt_obras_artista)
        val ptPais = findViewById<EditText>(R.id.pt_pais_artista)
        val swInternacional = findViewById<Switch>(R.id.sw_internacional_artista)

        ptID.setText(intent.getStringExtra("idArtista"))
        ptNombre.setText(intent.getStringExtra("nombre"))
        ptFecha.setText(intent.getStringExtra("fechaNacimiento"))
        ptObras.setText(intent.getStringExtra("cantidadObras"))
        ptPais.setText(intent.getStringExtra("paisNacimiento"))


        val botonEditarArtista = findViewById<Button>(R.id.btn_editar_artista)
        botonEditarArtista.setOnClickListener {
            val ptID = findViewById<EditText>(R.id.pt_id_artista)
            val ptNombre = findViewById<EditText>(R.id.pt_nombre_artista)
            val ptFecha = findViewById<EditText>(R.id.td_nacimiento_artista)
            val ptPais = findViewById<EditText>(R.id.pt_pais_artista)
            val ptObras = findViewById<EditText>(R.id.pt_obras_artista)
            val swInternacional = findViewById<Switch>(R.id.sw_internacional_artista)

            /*if (ptNombre.text.toString().trim().isEmpty() ||
                ptFecha.text.toString().trim().isEmpty() ||
                ptObras.text.toString().trim().isEmpty() ||
                ptPais.text.toString().trim().isEmpty()
            ) {
                mostrarSnackbar("Completar campos")
            } else if (ptObras.text.toString().toInt() == null) {
                mostrarSnackbar("Cantidad de obras debe ser un número entero")
            } else {*/
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
                val respuesta = artista1.actualizarArtista(this)
                if (respuesta) {
                    val intent = Intent()
                    intent.putExtra("nombre",artista1.nombre)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

                // Actualizar  artista en Firestore
                db.collection("artistas")
                    .document(ptID.text.toString())
                    .set(artista)
                    .addOnSuccessListener {
                        mostrarSnackbar("Artista actualizado correctamente")
                    }
                    .addOnFailureListener { e ->
                        mostrarSnackbar("Error al actualizar el artista: $e")
                    }

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.ly_form_artista), // view
                texto, // texto
                Snackbar.LENGTH_LONG // tiempo
            )
            .show()
    }
}