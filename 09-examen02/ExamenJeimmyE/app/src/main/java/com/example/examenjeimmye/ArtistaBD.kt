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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class ArtistaBD : AppCompatActivity() {

    private lateinit var adaptador: ArrayAdapter<Artista>
    private lateinit var listView: ListView
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var db: FirebaseFirestore
    private lateinit var arregloArtistas: MutableList<Artista>

    private var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artista)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = FirebaseFirestore.getInstance()
        startForResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val nombre = data?.getStringExtra("nombre")
                    mostrarSnackbar("El artista $nombre ha sido modificado")
                }
            }

        listView = findViewById<ListView>(R.id.lv_artistas)
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adaptador

        registerForContextMenu(listView)
        obtenerArtistas()

        val botonNuevoArtista = findViewById<Button>(R.id.btn_nuevo_artista)
        botonNuevoArtista.setOnClickListener {
            irActividad(FormularioArtista::class.java)
        }


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
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar ->{
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val artista = listView.adapter.getItem(info.position) as Artista
                val intent = Intent(this, InfoArtista::class.java)
                intent.putExtra("idArtista", artista.idArtista)
                intent.putExtra("nombre", artista.nombre)
                intent.putExtra("fechaNacimiento", artista.fechaNacimiento)
                intent.putExtra("cantidadObras", artista.cantidadObras)
                intent.putExtra("paisNacimiento", artista.paisNacimiento)
                intent.putExtra("esInternacional", artista.esInternacional)
                startForResult.launch(intent)
                return true
            }
            R.id.mi_eliminar ->{
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val artista = listView.adapter.getItem(info.position) as Artista
                artista.eliminarArtista(this);
                mostrarSnackbar("Artista \"${artista.nombre}\" con id = ${artista.idArtista} ha sido eliminado")
                actualizarListView()
                return true
            }
            R.id.mi_verDetalles ->{
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val artista = listView.adapter.getItem(info.position) as Artista
                val intent = Intent(this, ObraBD::class.java)
                intent.putExtra("idArtista", artista.idArtista)
                intent.putExtra("nombreArtista", artista.nombre)
                intent.putExtra("fechaNacimiento", artista.fechaNacimiento)
                intent.putExtra("cantidadObras", artista.cantidadObras)
                intent.putExtra("paisNacimiento", artista.paisNacimiento)
                intent.putExtra("esInternacional", artista.esInternacional)
                startActivity(intent)
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

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.lv_artistas), // view
                texto, // texto
                Snackbar.LENGTH_LONG // tiempo
            )
            .show()
    }

    fun actualizarListView(){
        val artistas = Artista.obtenerArtistas(this)
        adaptador.clear()
        adaptador.addAll(artistas)
        adaptador.notifyDataSetChanged()
    }
    private fun obtenerArtistas() {
        db.collection("artistas")
            .get()
            .addOnSuccessListener { result ->
                arregloArtistas.clear()
                for (document in result) {
                    val artista = document.toObject(Artista::class.java)
                    arregloArtistas.add(artista)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                mostrarSnackbar("Error al obtener artistas: $exception")
            }
    }

}