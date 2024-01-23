package com.example.examenjeimmyeche

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class FormularioArtista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_artista)

        val titulo = intent.getStringExtra("titulo")
        val position = intent.getIntExtra("position",-1)
        val tpTitulo = findViewById<TextView>(R.id.tv_title)

        if(titulo!=null)
            tpTitulo.setText(titulo.toString())

        val ptID = findViewById<EditText>(R.id.pt_id_artista)
        val ptNombre = findViewById<EditText>(R.id.pt_nombre_artista)
        val ptFecha = findViewById<EditText>(R.id.td_nacimiento_artista)
        val ptPais = findViewById<EditText>(R.id.pt_pais_artista)
        val ptObras = findViewById<EditText>(R.id.pt_obras_artista)
        val swInternacional = findViewById<Switch>(R.id.sw_internacional_artista)


        if(position!=-1) {

            val artista = BDMemoria.artistas[position]
            ptID.setText(artista.idArtista.toString())
            ptNombre.setText(artista.nombre.toString())
            ptFecha.setText(artista.fechaNacimiento.year.toString()+"-"+artista.fechaNacimiento.month.toString()+"-"+artista.fechaNacimiento.day.toString())
            ptPais.setText(artista.paisNacimiento.toString())
            ptObras.setText(artista.cantidadObras.toString())
            swInternacional.isChecked=artista.esInternacional
        }
        else{
            ptID.setText(BDMemoria.calcularIdNuevoArtista().toString())
        }

        val botonGuardar = findViewById<Button>(R.id.btn_guardar_artista)
        botonGuardar
            .setOnClickListener {  guardarDatos(position)}
    }
    fun guardarDatos(position:Int){
        val ptID = findViewById<EditText>(R.id.pt_id_artista)
        val ptNombre = findViewById<EditText>(R.id.pt_nombre_artista)
        val ptFecha = findViewById<EditText>(R.id.td_nacimiento_artista)
        val ptPais = findViewById<EditText>(R.id.pt_pais_artista)
        val ptObras = findViewById<EditText>(R.id.pt_obras_artista)
        val swInternacional = findViewById<Switch>(R.id.sw_internacional_artista)

        val id = ptID.text.toString().toInt()
        val nombre=ptNombre.text.toString()
        val fechaT = ptFecha.text.toString().split('-')
        val fecha = Date(fechaT[0].toInt(),fechaT[1].toInt(),fechaT[2].toInt())
        val pais = ptPais.text.toString()
        val obras = ptObras.text.toString().toInt()
        val internacional = swInternacional.isChecked()

        var result=false;

        if(position==-1)
            result = BDMemoria.crearArtista(nombre,fecha,obras,pais,internacional)
        else {
            BDMemoria.editarArtista(id, nombre,fecha,obras,pais,internacional)
            result = true
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
        Snackbar.make(
            findViewById(R.id.ly_form_artista),
            texto,
            Snackbar.LENGTH_LONG
        )
            .show()
    }
}