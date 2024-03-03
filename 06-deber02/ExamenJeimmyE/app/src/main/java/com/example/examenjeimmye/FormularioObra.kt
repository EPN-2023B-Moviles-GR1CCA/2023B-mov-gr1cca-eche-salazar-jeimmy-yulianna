package com.example.examenjeimmye

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class FormularioObra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_obra)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val idArtista = intent.getIntExtra("idArtista", 0)

        val botonCrearPersonaje = findViewById<Button>(R.id.btn_editar_obra)
        botonCrearPersonaje.setOnClickListener {
            val etNombre = findViewById<EditText>(R.id.et_titulo_obra)
            val etAnio = findViewById<EditText>(R.id.et_anio_obra)
            val etValor = findViewById<EditText>(R.id.et_valor_obra)
            val etDisciplina = findViewById<EditText>(R.id.et_disciplina_obra)
            val swAbstracta  = findViewById<Switch>(R.id.sw_abstracta_obra)

            if(etNombre.text.toString().trim().isEmpty() ||
                etAnio.text.toString().trim().isEmpty() ||
                etValor.text.toString().trim().isEmpty() ||
                etDisciplina.text.toString().trim().isEmpty()
            ){
                mostrarSnackbar("Por favor, llena  todos los campos")
            } else {
                val obra = Obra(
                    null,
                    etNombre.text.toString(),
                    etDisciplina.text.toString(),
                    etAnio.text.toString().toInt(),
                    etValor.text.toString().toDouble(),
                    swAbstracta.isChecked(),
                    idArtista
                )
                val respuesta = obra.crearObra(this)
                if (respuesta) {
                    mostrarSnackbar("Obra: \"${obra.titulo}\" ha sido creado")
                    etNombre.setText(obra.titulo.toString())
                    etAnio.setText(obra.anioCreacion.toString())
                    etValor.setText(obra.valorEstimado.toString())
                    etDisciplina.setText(obra.disciplinaArtistica.toString())
                    swAbstracta.isChecked=obra.esAbstracta
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
                findViewById(R.id.ly_form_obra), // view
                texto, // texto
                Snackbar.LENGTH_LONG // tiempo
            )
            .show()
    }
}