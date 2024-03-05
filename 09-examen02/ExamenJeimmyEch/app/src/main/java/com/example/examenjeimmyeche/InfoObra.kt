package com.example.examenjeimmyeche

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class InfoObra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_obra)

        val id = intent.getIntExtra("id",-1)

        if(id!=-1){
            val obra = BDMemoria.obras.filter { it -> it.id==id }.first()
            val artista = BDMemoria.artistas.filter { it -> it.idArtista==obra.idArtista }.first()

            val tvTitulo = findViewById<TextView>(R.id.tv_titulo_obra)
            val tvDisciplina = findViewById<TextView>(R.id.tv_disciplina_obra)
            val tvAnio = findViewById<TextView>(R.id.tv_anio_obra)
            val tvValor = findViewById<TextView>(R.id.tv_valor_obra)
            val tvAbstracta = findViewById<TextView>(R.id.tv_abstracta_obra)
            val tvArtista = findViewById<TextView>(R.id.tv_artista)


            tvTitulo.setText("Nombre: "+obra.titulo.toString())
            tvDisciplina.setText("Disciplina: ${obra.disciplinaArtistica.toString()}")
            tvAnio.setText("Año: ${obra.anioCreacion.toString()}")
            tvValor.setText("Valor: ${obra.valorEstimado.toString()}")
            tvAbstracta.setText("Abstracta: "+if(obra.esAbstracta) "SI" else "NO")
            tvArtista.setText("Artista: ${artista.nombre.toString()}")
        }
        else{
            mostrarSnackbar("Error al leer los datos")
        }
    }
    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.lv_obras),
                texto,
                Snackbar.LENGTH_LONG,
            )
            .show()
    }
}