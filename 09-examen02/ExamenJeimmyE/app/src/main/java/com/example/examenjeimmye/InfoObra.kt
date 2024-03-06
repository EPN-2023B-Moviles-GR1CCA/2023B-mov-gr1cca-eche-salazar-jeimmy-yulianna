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
import com.google.firebase.firestore.FirebaseFirestore

class InfoObra : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_obra)

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

        val idObra = intent.getIntExtra("idObra", 0)
        val etNombre = findViewById<EditText>(R.id.et_titulo_obra)
        val etAnio = findViewById<EditText>(R.id.et_anio_obra)
        val etValor = findViewById<EditText>(R.id.et_valor_obra)
        val etDisciplina = findViewById<EditText>(R.id.et_disciplina_obra)
        val swAbstracta  = findViewById<Switch>(R.id.sw_abstracta_obra)
        val idArtista = intent.getIntExtra("idArtista",0)
        etNombre.setText(intent.getStringExtra("titulo"))
        etAnio.setText(intent.getStringExtra("anioCreacion"))
        etValor.setText(intent.getStringExtra("valorEstimado"))
        etDisciplina.setText(intent.getStringExtra("disciplinaArtistica"))
        val obraR = db.collection("obras").document(idObra.toString())

        obraR.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val obra = document.toObject(Obra::class.java)
                    if (obra != null) {
                        val etNombre = findViewById<EditText>(R.id.et_titulo_obra)
                        val etAnio = findViewById<EditText>(R.id.et_anio_obra)
                        val etValor = findViewById<EditText>(R.id.et_valor_obra)
                        val etDisciplina = findViewById<EditText>(R.id.et_disciplina_obra)
                        val swAbstracta  = findViewById<Switch>(R.id.sw_abstracta_obra)

                        val id = intent.getIntExtra("idObra",0)
                        val idArtista = intent.getIntExtra("idArtista",0)


                        etNombre.setText(intent.getStringExtra("titulo"))
                        etAnio.setText(intent.getStringExtra("anioCreacion"))
                        etValor.setText(intent.getStringExtra("valorEstimado"))
                        etDisciplina.setText(intent.getStringExtra("disciplinaArtistica"))

                    }
                } else {
                    mostrarSnackbar("No se encontró la obra")
                }
            }
            .addOnFailureListener { exception ->
                mostrarSnackbar("Error al obtener la obra: $exception")
            }

        val botonEditarObra = findViewById<Button>(R.id.btn_editar_obra)
        botonEditarObra.setOnClickListener {
            val idObra = findViewById<EditText>(R.id.et_id_obra)
            val etNombre = findViewById<EditText>(R.id.et_titulo_obra)
            val etAnio = findViewById<EditText>(R.id.et_anio_obra)
            val etValor = findViewById<EditText>(R.id.et_valor_obra)
            val etDisciplina = findViewById<EditText>(R.id.et_disciplina_obra)
            val swAbstracta  = findViewById<Switch>(R.id.sw_abstracta_obra)

            val obra = hashMapOf(
                "idObra" to idObra.text.toString().toInt(),
                "etNombre" to etNombre.text.toString(),
                "etDisciplina" to etDisciplina.text.toString(),
                "etAnio" to etAnio.text.toString().toInt(),
                "etValor" to etValor.text.toString().toDouble(),
                "swAbstracta" to swAbstracta.isChecked()
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
            val respuesta = obra1.actualizarObra(this)
            if (respuesta) {
                val intent = Intent()
                intent.putExtra("nombre",obra1.titulo)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

                obraR.update(obra as Map<String, Any>)
                    .addOnSuccessListener {
                        mostrarSnackbar("Obra actualizada")
                        irActividad(ObraBD::class.java)
                    }
                    .addOnFailureListener { e ->
                        mostrarSnackbar("Error al actualizar el álbum: $e")
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
    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}