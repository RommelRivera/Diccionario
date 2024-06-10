package com.rommelrivera.diccionario

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.MaterialShapeDrawable

class Lista : AppCompatActivity() {

    private lateinit var container : RecyclerView
    private lateinit var txtPalabra : TextView
    private lateinit var btnCrear : Button
    private lateinit var btnEditarIdioma : Button
    private lateinit var palabras : Array<Palabra>
    private lateinit var database : Database
    private lateinit var idioma : Idioma

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        var extras : Bundle = intent.extras!!
        database = Database(this)
        idioma = database.selectIdiomaByNombre(extras.getString("idioma", ""))
        findViewById<ConstraintLayout>(R.id.base).setBackgroundColor(Color.parseColor(idioma.colFondo))
        window.statusBarColor = Color.parseColor(idioma.colBoton)

        txtPalabra = findViewById(R.id.txtPalabra)
        txtPalabra.setTextColor(Color.parseColor(idioma.colTexto))
        txtPalabra.backgroundTintList = ColorStateList.valueOf(Color.parseColor(idioma.colTexto))

        btnCrear = findViewById(R.id.btnCrear)
        btnCrear.setBackgroundColor(Color.parseColor(idioma.colBoton))
        btnCrear.setTextColor(Color.parseColor(idioma.colBotonTexto))
        btnCrear.setOnClickListener {
            var intent : Intent = Intent(this, CrearEditarPalabra::class.java)
            if (!txtPalabra.text.isNullOrBlank()) {
                intent.putExtra("palabra", txtPalabra.text.toString())
            }
            txtPalabra.text = ""
            intent.putExtra("accion", "crear")
            intent.putExtra("idIdioma", idioma.idIdioma)
            startActivity(intent)
        }

        btnEditarIdioma = findViewById(R.id.btnEditarIdioma)
        btnEditarIdioma.setBackgroundColor(Color.parseColor(idioma.colBoton))
        (btnEditarIdioma as MaterialButton).iconTint = ColorStateList.valueOf(Color.parseColor(idioma.colBotonTexto))
        btnEditarIdioma.setOnClickListener {
            var intent : Intent = Intent(this, CrearEditarIdioma::class.java)
            intent.putExtra("accion", "editar")
            intent.putExtra("idioma", idioma.nombre)
            startActivityForResult(intent, 1)
        }

        palabras = database.selectPalabras(idioma.idIdioma)

        container = findViewById(R.id.container)
        container.layoutManager = LinearLayoutManager(this)
        container.adapter = PalabrasAdapter(this, palabras, idioma.colTexto)

        val border : MaterialShapeDrawable = MaterialShapeDrawable()
        border.fillColor = ColorStateList.valueOf(Color.TRANSPARENT)
        border.setStroke(5.0f, Color.parseColor(idioma.colTexto))
        findViewById<TextView>(R.id.border).background = border

        txtPalabra.addTextChangedListener {
            search()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != 1) {
            if (resultCode != 0) finish()
            return
        }

        var extras : Bundle = data!!.extras!!
        idioma = database.selectIdiomaByNombre(extras.getString("idioma", ""))
        findViewById<ConstraintLayout>(R.id.base).setBackgroundColor(Color.parseColor(idioma.colFondo))
        window.statusBarColor = Color.parseColor(idioma.colBoton)

        txtPalabra.setTextColor(Color.parseColor(idioma.colTexto))
        txtPalabra.backgroundTintList = ColorStateList.valueOf(Color.parseColor(idioma.colTexto))

        btnCrear.setBackgroundColor(Color.parseColor(idioma.colBoton))
        btnCrear.setTextColor(Color.parseColor(idioma.colBotonTexto))

        btnEditarIdioma.setBackgroundColor(Color.parseColor(idioma.colBoton))
        (btnEditarIdioma as MaterialButton).iconTint = ColorStateList.valueOf(Color.parseColor(idioma.colBotonTexto))
    }

    fun search() {
        var temp : MutableList<Palabra> = ArrayList()
        palabras = database.selectPalabras(idioma.idIdioma)
        for (i in palabras.indices) {
            if (palabras[i].nombre.lowercase().contains(txtPalabra.text.toString()) || palabras[i].definicion.lowercase().contains(txtPalabra.text.toString())) temp.add(palabras[i])
        }

        container.adapter = null
        container.recycledViewPool.clear()
        var adapter : PalabrasAdapter = PalabrasAdapter(this, temp.toTypedArray(), idioma.colTexto)
        container.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        search()
    }
}
