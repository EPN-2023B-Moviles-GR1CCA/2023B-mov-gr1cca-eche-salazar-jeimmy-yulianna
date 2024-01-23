package com.example.examenjeimmyeche

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

class InfoArtista : AppCompatActivity() {

    val callbackEditarObra =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if (result.resultCode == Activity.RESULT_OK){
                mostrarSnackbar("Datos Guardados")
                actualizarListView()
            }
        }

    val callbackDetallesObra =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if (result.resultCode == Activity.RESULT_OK){
                actualizarListView()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_artista)

        val position = intent.getIntExtra("position",-1)
        val artista=BDMemoria.artistas[position]

        val tvNombre = findViewById<TextView>(R.id.tv_nombre_artista)
        val tvObras = findViewById<TextView>(R.id.tv_obras_artista)
        val tvInternacional = findViewById<TextView>(R.id.tv_internacional_artista)
        val tvFecha = findViewById<TextView>(R.id.tv_fecha_artista)
        val tvPais = findViewById<TextView>(R.id.tv_pais_artista)


        if(artista != null){
            tvNombre.text = "Nombre: ${artista.nombre.toString()}"
            tvObras.text = "Obras: ${artista.cantidadObras.toString()}"
            tvInternacional.text = "Internacional: ${artista.esInternacional.toString()}"
            tvFecha.text = "Fecha: "+artista.fechaNacimiento.year.toString()+"-"+artista.fechaNacimiento.month.toString()+"-"+artista.fechaNacimiento.day.toString()
            tvPais.text = "País: ${artista.paisNacimiento.toString()}"
        }

        val lv_obras = findViewById<ListView>(R.id.lv_obras)
        actualizarListView()

        val btnNuevaObra = findViewById<Button>(R.id.btn_nueva_obra)
        btnNuevaObra
            .setOnClickListener {
                lanzarIntentEditarObra("CREAR OBRA", -1,artista.idArtista)
            }

        registerForContextMenu(lv_obras)

    }

    var posicionItemSeleccionado = 0
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu ,menu)
        //Obtener el id del ArrayListSeleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar -> {
                val position = intent.getIntExtra("position",-1)
                val id=BDMemoria.artistas[position].idArtista
                val obras = BDMemoria.obras.filter { it -> it.idArtista == id }
                val id_obra = obras[posicionItemSeleccionado].id
                lanzarIntentEditarObra("EDITAR OBRA",id_obra,id)
                return true
            }
            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }
            R.id.mi_detalles -> {
                val position = intent.getIntExtra("position",-1)
                val id=BDMemoria.artistas[position].idArtista
                val obras = BDMemoria.obras.filter { it -> it.idArtista == id }
                val id_obra = obras[posicionItemSeleccionado].id
                lanzarIntentDetallesObra(id_obra)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun lanzarIntentEditarObra(titulo:String, id:Int, idArtista:Int){
        val intentEditarObra = Intent(this, FormularioObra::class.java)
        intentEditarObra.putExtra("titulo",titulo)
        intentEditarObra.putExtra("idObra",id)
        intentEditarObra.putExtra("idArtista",idArtista)
        callbackEditarObra.launch(intentEditarObra)
    }
    fun abrirDialogo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar?")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                val position = intent.getIntExtra("position",-1)
                val id=BDMemoria.artistas[position].idArtista
                var obras = BDMemoria.obras.filter { it -> it.idArtista == id }
                val id_obra = obras[posicionItemSeleccionado].id
                val result = BDMemoria.borrarObra(id_obra)

                actualizarListView()

                if(result)
                    mostrarSnackbar("Obra eliminada")
                else
                    mostrarSnackbar("Error interno")

            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )
        val dialogo = builder.create()
        dialogo.show()
    }

    fun lanzarIntentDetallesObra(id:Int){
        val intentEditarObra = Intent(this, InfoObra::class.java)
        intentEditarObra.putExtra("id",id)
        callbackDetallesObra.launch(intentEditarObra)
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

    fun actualizarListView(){
        val position = intent.getIntExtra("position",-1)
        val id=BDMemoria.artistas[position].idArtista
        val lv_obras = findViewById<ListView>(R.id.lv_obras)
        val obras = BDMemoria.obras.filter { it -> it.idArtista == id }
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, // como se va a ver (XML)
            obras
        )
        lv_obras.adapter = adaptador

        adaptador.notifyDataSetChanged()
    }
}