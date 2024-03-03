package com.example.examenjeimmye

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class FormularioArtista: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_artista)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val botonCrearArtista = findViewById<Button>(R.id.btn_guardar_artista)
        botonCrearArtista.setOnClickListener {
            val ptNombre = findViewById<EditText>(R.id.pt_nombre_artista)
            val ptFecha = findViewById<EditText>(R.id.td_nacimiento_artista)
            val ptPais = findViewById<EditText>(R.id.pt_pais_artista)
            val ptObras = findViewById<EditText>(R.id.pt_obras_artista)
            val swInternacional = findViewById<Switch>(R.id.sw_internacional_artista)

            if (ptNombre.text.toString().trim().isEmpty() ||
                ptFecha.text.toString().trim().isEmpty() ||
                ptObras.text.toString().trim().isEmpty() ||
                ptPais.text.toString().trim().isEmpty()
            ){
                mostrarSnackbar("Completar campos")
            } else {
                val artista = Artista(
                    null,
                    ptNombre.text.toString(),
                    ptFecha.text.toString(),
                    ptObras.text.toString().toInt(),
                    ptPais.text.toString(),
                    swInternacional.isChecked()
                )
                val respuesta = artista.crearArtista(this)
                if (respuesta) {
                    mostrarSnackbar("Artista: \"${artista.nombre}\" ha sido creado")
                    ptNombre.setText(artista.nombre.toString())
                    ptFecha.setText(artista.fechaNacimiento.toString())
                    ptPais.setText(artista.paisNacimiento.toString())
                    ptObras.setText(artista.cantidadObras.toString())
                    swInternacional.isChecked=artista.esInternacional

                }
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