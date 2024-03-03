package com.example.examenjeimmyeche

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class FormularioObra : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_obra)

        val title = intent.getStringExtra("titulo")
        val id = intent.getIntExtra("idObra",-1)
        val idArtista = intent.getIntExtra("idArtista",0)
        val tvTitle = findViewById<TextView>(R.id.tv_title_obra)

        tvTitle.text = title.toString()

        val etID = findViewById<EditText>(R.id.et_id_obra)
        val etIdartista = findViewById<EditText>(R.id.et_id_artista_obra)
        val etNombre = findViewById<EditText>(R.id.et_titulo_obra)
        val etAnio = findViewById<EditText>(R.id.et_anio_obra)
        val etValor = findViewById<EditText>(R.id.et_valor_obra)
        val etDisciplina = findViewById<EditText>(R.id.et_disciplina_obra)
        val swAbstracta  = findViewById<Switch>(R.id.sw_abstracta_obra)

        if(id!=-1){
            val obra = BDMemoria.obras.filter { it -> it.id == id }.first()
            etID.setText(obra.id.toString())
            etIdartista.setText(obra.idArtista.toString())
            etNombre.setText(obra.titulo.toString())
            etAnio.setText(obra.anioCreacion.toString())
            etValor.setText(obra.valorEstimado.toString())
            etDisciplina.setText(obra.disciplinaArtistica.toString())
            swAbstracta.isChecked=obra.esAbstracta
        }
        else{
            etID.setText(BDMemoria.calcularIdNuevaObra().toString())
            etIdartista.setText(idArtista.toString())
        }

        val btnGuardar = findViewById<Button>(R.id.btn_guardar_obra)
        btnGuardar
            .setOnClickListener { guardarDatos(id)}

    }

    fun guardarDatos(id: Int){
        val etID = findViewById<EditText>(R.id.et_id_obra)
        val etIdartista = findViewById<EditText>(R.id.et_id_artista_obra)
        val etNombre = findViewById<EditText>(R.id.et_titulo_obra)
        val etAnio = findViewById<EditText>(R.id.et_anio_obra)
        val etValor = findViewById<EditText>(R.id.et_valor_obra)
        val etDisciplina = findViewById<EditText>(R.id.et_disciplina_obra)
        val swAbstracta  = findViewById<Switch>(R.id.sw_abstracta_obra)

        val idObra = etID.text.toString().toInt()
        val idArtista = etIdartista.text.toString().toInt()
        val nombre = etNombre.text.toString()
        val anio = etAnio.text.toString().toInt()
        val valor = etValor.text.toString().toDouble()
        val disciplina = etDisciplina.text.toString()
        val abstracta = swAbstracta.isChecked()

        var result=false

        if(id==-1){
            result = BDMemoria.crearObra(idArtista,nombre,disciplina,anio,valor,abstracta)
        }
        else{
            BDMemoria.editarObra(idObra,idArtista,nombre,disciplina,anio,valor,abstracta)
            result=true
        }
        if(result){
            setResult(
                RESULT_OK,
            )
            finish()
        }
        else{
            mostrarSnackbar("Error guardando datos, intenta nuevamente")
        }
    }
    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.ly_form_obra),
                texto,
                Snackbar.LENGTH_LONG,
            )
            .show()
    }
}