package com.example.examenjeimmye

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class FormularioObra : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_obra)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

        val idArtista = intent.getIntExtra("idArtista", 0)
        val botonCrearObra = findViewById<Button>(R.id.btn_editar_obra)
        botonCrearObra.setOnClickListener {
            val etID = findViewById<EditText>(R.id.et_id_obra)
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
                val obra = hashMapOf(
                    "etID" to etID.text.toString().toInt(),
                    "etNombre" to etNombre.text.toString(),
                    "etDisciplina" to etDisciplina.text.toString(),
                    "etAnio" to etAnio.text.toString().toInt(),
                    "etValor" to etValor.text.toString().toDouble(),
                    "swAbstracta" to swAbstracta.isChecked(),
                    "idArtista" to idArtista
                )
                val obra1 = Obra(
                    null,
                    etNombre.text.toString(),
                    etDisciplina.text.toString(),
                    etAnio.text.toString().toInt(),
                    etValor.text.toString().toDouble(),
                    swAbstracta.isChecked(),
                    idArtista
                )
                val respuesta = obra1.crearObra(this)
                if (respuesta) {
                    mostrarSnackbar("Obra: \"${obra1.titulo}\" ha sido creado")
                    etNombre.setText(obra1.titulo.toString())
                    etAnio.setText(obra1.anioCreacion.toString())
                    etValor.setText(obra1.valorEstimado.toString())
                    etDisciplina.setText(obra1.disciplinaArtistica.toString())
                    swAbstracta.isChecked=obra1.esAbstracta
                }

                // Agregar la obra a Firestore
                db.collection("obras")
                    .add(obra)
                    .addOnSuccessListener { documentReference ->
                        mostrarSnackbar("Se ha creado la Obra ${etNombre.text} y el ID del artista es $idArtista")
                        val intent = Intent(this, InfoArtista::class.java)
                        intent.putExtra("etID", idArtista)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        mostrarSnackbar("Error al crear la obra: $e")
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