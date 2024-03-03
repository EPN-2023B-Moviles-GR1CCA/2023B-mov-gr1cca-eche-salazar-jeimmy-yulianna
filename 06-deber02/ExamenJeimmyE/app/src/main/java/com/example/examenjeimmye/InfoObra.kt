package com.example.examenjeimmye

import android.annotation.SuppressLint
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

class InfoObra : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_obra)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etNombre = findViewById<EditText>(R.id.et_titulo_obra)
        val etAnio = findViewById<EditText>(R.id.et_anio_obra)
        val etValor = findViewById<EditText>(R.id.et_valor_obra)
        val etDisciplina = findViewById<EditText>(R.id.et_disciplina_obra)
        val swAbstracta  = findViewById<Switch>(R.id.sw_abstracta_obra)

        val id = intent.getIntExtra("idObra",0)
        val idArtista = intent.getIntExtra("idArtista",0)

        etNombre.setText(intent.getStringExtra("titulo"))
        etAnio.setText(intent.getStringExtra("año"))
        etValor.setText(intent.getStringExtra("valor"))
        etDisciplina.setText(intent.getStringExtra("disciplina"))

        val botonEditarObra = findViewById<Button>(R.id.btn_editar_obra)
        botonEditarObra.setOnClickListener {

            if (etNombre.text.toString().trim().isEmpty() ||
                etAnio.text.toString().trim().isEmpty() ||
                etValor.text.toString().trim().isEmpty() ||
                etDisciplina.text.toString().trim().isEmpty()
            ) {
                mostrarSnackbar("Completar campos")
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
                val respuesta = obra.actualizarObra(this)
                if (respuesta) {
                    val intent = Intent()
                    intent.putExtra("nombre",obra.titulo)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
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