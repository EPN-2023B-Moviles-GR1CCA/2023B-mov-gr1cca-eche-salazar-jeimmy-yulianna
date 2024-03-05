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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    val artistas = BDMemoria.artistas

    val callbackEditarArtista =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if (result.resultCode == Activity.RESULT_OK){
                mostrarSnackbar("Datos Guardados")
                actualizarListView()
            }
        }
    val callbackDetallesArtista =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if (result.resultCode == Activity.RESULT_OK){
                actualizarListView()
            }
        }
    private fun obtenerArtistas() {
        db.collection("artistas")
            .get()
            .addOnSuccessListener { result ->
                artistas.clear()
                for (document in result) {
                    val artista = document.toObject(Artista::class.java)
                    artistas.add(artista)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                mostrarSnackbar("Error al obtener artistas: $exception")
            }
    }
    private lateinit var adaptador: ArrayAdapter<Artista>
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        val lv_artistas = findViewById<ListView>(R.id.lv_artistas)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, // como se va a ver (XML)
            artistas
        )
        lv_artistas.adapter = adaptador
        adaptador.notifyDataSetChanged()
        obtenerArtistas()
        val btn_crear_artista = findViewById<Button>(R.id.btn_nuevo_artista)
        btn_crear_artista
            .setOnClickListener {
                lanzarIntentEditarArtista("CREAR ARTISTA",-1)
            }
        registerForContextMenu(lv_artistas)
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
                lanzarIntentEditarArtista("EDITAR ARTISTA",posicionItemSeleccionado)

                return true
            }
            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }
            R.id.mi_detalles -> {
                lanzarIntentDetallesArtista(posicionItemSeleccionado)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    fun lanzarIntentEditarArtista(titulo:String, position:Int){
        val intentEditarArtista = Intent(this, FormularioArtista::class.java)
        intentEditarArtista.putExtra("titulo",titulo)
        intentEditarArtista.putExtra("position",position)
        callbackEditarArtista.launch(intentEditarArtista)

        // Actualizar el documento del artista en Firestore
        db.collection("artistas")
            .document(BDMemoria.artistas[position].idArtista.toString())
            .set(BDMemoria.artistas[position])
            .addOnSuccessListener {
                mostrarSnackbar("Artista actualizado correctamente")
            }
            .addOnFailureListener { e ->
                mostrarSnackbar("Error al actualizar el artista: $e")
            }
    }

    fun lanzarIntentDetallesArtista(position: Int){
        val intentEditarArtista = Intent(this, InfoArtista::class.java)
        intentEditarArtista.putExtra("position",position)
        callbackDetallesArtista.launch(intentEditarArtista)
    }
    fun abrirDialogo(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar?")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                val id = BDMemoria.artistas[posicionItemSeleccionado].idArtista
                val result = BDMemoria.borrarArtista(id)

                actualizarListView()

                // Eliminar el documento del artista en Firestore
                db.collection("artistas").document(id.toString())
                    .delete()
                    .addOnSuccessListener {
                        mostrarSnackbar("Artista eliminado correctamente")
                    }
                    .addOnFailureListener { exception ->
                        mostrarSnackbar("Error al eliminar artista: $exception")
                    }


                if(result)
                    mostrarSnackbar("Artista eliminado")
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
    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.lv_artistas),
                texto,
                Snackbar.LENGTH_LONG,
            )
            .show()
    }
    /*fun irActividad(
        clase: Class<*>
    ){
        var intent = Intent(this, clase)
        startActivity(intent)
    }*/

    fun actualizarListView(){
        val lv_artistas = findViewById<ListView>(R.id.lv_artistas)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, // como se va a ver (XML)
            artistas
        )
        lv_artistas.adapter = adaptador
        adaptador.notifyDataSetChanged()
    }
}