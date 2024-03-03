package com.example.examenjeimmye

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class ObraBD : AppCompatActivity() {

    private lateinit var adaptador: ArrayAdapter<Obra>
    private lateinit var listView: ListView
    private var idArtista: Int = 0
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        startForResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val titulo = data?.getStringExtra("titulo")
                    mostrarSnackbar("La obra \"$titulo\" ha sido modificada")
                }
            }

        idArtista = intent.getIntExtra("idArtista", 0)
        val nombreArtista= intent.getStringExtra("nombreArtista")
        val tvNombre = findViewById<TextView>(R.id.tv_nombre_artista)
        tvNombre.text = "Nombre: ${nombreArtista}"

        val fechaNac=intent.getStringExtra("fechaNac")
        val tvFecha = findViewById<TextView>(R.id.tv_fecha_artista)
        tvFecha.text = "Fecha: "+ fechaNac

        val cantObras=intent.getIntExtra("cantObras", 0)
        val tvObras = findViewById<TextView>(R.id.tv_obras_artista)
        tvObras.text = "Obras: ${cantObras}"

        val esIntern=intent.getBooleanExtra("esIntern", false)
        val tvInternacional = findViewById<TextView>(R.id.tv_internacional_artista)
        tvInternacional.text=  "Internacional: ${esIntern}"

        val paisNac=intent.getStringExtra("paisNac")
        val tvPais = findViewById<TextView>(R.id.tv_pais_artista)
        tvPais.text = "País: ${paisNac}"


        listView = findViewById<ListView>(R.id.lv_obras)
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adaptador


        val botonNuevaObra = findViewById<Button>(R.id.btn_nueva_obra)
        botonNuevaObra.setOnClickListener{
            val intent = Intent(this, FormularioObra::class.java)
            intent.putExtra("idActor",idArtista)
            startActivity(intent)
        }




        registerForContextMenu(listView)
    }

    override fun onResume() {
        super.onResume()
        actualizarListView()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // Llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            menu.findItem(R.id.mi_verDetalles).isVisible = false
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar ->{
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val obra = listView.adapter.getItem(info.position) as Obra
                val intent = Intent(this, InfoObra::class.java)
                intent.putExtra("id", obra.id)
                intent.putExtra("titulo", obra.titulo)
                intent.putExtra("disciplinaArtistica", obra.disciplinaArtistica)
                intent.putExtra("anioCreacion", obra.anioCreacion)
                intent.putExtra("valorEstimado", obra.valorEstimado)
                intent.putExtra("esAbstracta", obra.esAbstracta)
                intent.putExtra("idArtista", obra.idArtista)
                startForResult.launch(intent)
                return true
            }
            R.id.mi_eliminar ->{
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val obra = listView.adapter.getItem(info.position) as Obra
                obra.eliminarObra(this);
                mostrarSnackbar("Obra \"${obra.titulo}\" con id = ${obra.id} ha sido eliminado")
                actualizarListView()
                return true
            }
            else -> super.onContextItemSelected(item)
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

    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.lv_obras), // view
                texto, // texto
                Snackbar.LENGTH_LONG // tiempo
            )
            .show()
    }

    fun actualizarListView(){
        val obras = Obra.obtenerObras(this, idArtista)
        adaptador.clear()
        if (obras.isNotEmpty()) {
            adaptador.addAll(obras)
            adaptador.notifyDataSetChanged()
        } else {
            mostrarSnackbar("No hay personajes para este Actor")
        }
    }

}