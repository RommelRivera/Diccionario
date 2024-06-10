package com.rommelrivera.diccionario

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class CrearEditarPalabra : AppCompatActivity() {

    private lateinit var txtEditPalabra : TextView
    private lateinit var spnTipoPalabra : Spinner
    private lateinit var txtDefinicion : TextView
    private lateinit var btnAddPalabra : Button
    private lateinit var lblCrearEditar : TextView
    private lateinit var btnDeletePalabra : Button
    private lateinit var extras : Bundle
    private lateinit var idioma : Idioma

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_editar_palabra)

        extras = intent.extras!!
        var database : Database = Database(this)
        idioma = database.selectIdiomaById(extras.getInt("idIdioma", 0))
        txtEditPalabra = findViewById(R.id.txtEditPalabra)
        spnTipoPalabra = findViewById(R.id.spnTipoPalabra)
        txtDefinicion = findViewById(R.id.txtDefinicion)
        btnAddPalabra = findViewById(R.id.btnAddPalabra)
        lblCrearEditar = findViewById(R.id.lblCrearEditar)
        btnDeletePalabra = findViewById(R.id.btnDeletePalabra)

        findViewById<ConstraintLayout>(R.id.base).setBackgroundColor(Color.parseColor(idioma.colFondo))

        var accion : String = extras.getString("accion", "")
        var idPalabra : Int = extras.getInt("idPalabra", 0)
        var palabra : String = if (idPalabra != 0) Database(this).selectPalabraById(idPalabra).nombre else extras.getString("palabra", "")
        var tipoPalabra : Int = extras.getInt("idTipoPalabra", 1) - 1
        var definicion : String = if(idPalabra != 0) Database(this).selectPalabraById(idPalabra).definicion else ""

        window.statusBarColor = Color.parseColor(idioma.colBoton)

        txtEditPalabra.text = palabra
        txtEditPalabra.setTextColor(Color.parseColor(idioma.colTexto))
        txtEditPalabra.setHintTextColor(Color.parseColor(idioma.colTexto))
        txtEditPalabra.backgroundTintList = ColorStateList.valueOf(Color.parseColor(idioma.colTexto))
        txtDefinicion.text = definicion
        txtDefinicion.setTextColor(Color.parseColor(idioma.colTexto))
        txtDefinicion.setHintTextColor(Color.parseColor(idioma.colTexto))
        txtDefinicion.backgroundTintList = ColorStateList.valueOf(Color.parseColor(idioma.colTexto))

        var tiposPalabras : Array<String> = TipoPalabra().tiposPalabras.values.toTypedArray()

        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposPalabras)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTipoPalabra.adapter = adapter
        spnTipoPalabra.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent!!.getChildAt(0) as TextView).setBackgroundColor(Color.parseColor(idioma.colFondo))
                (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor(idioma.colTexto))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spnTipoPalabra.setSelection(tipoPalabra)
        spnTipoPalabra.setPopupBackgroundDrawable(ColorDrawable(Color.WHITE))

        if (accion == "crear") {
            btnAddPalabra.text = "Crear"
            lblCrearEditar.text = "Crear Palabra"
        } else {
            btnAddPalabra.text = "Editar"
            lblCrearEditar.text = "Editar Palabra"
            btnDeletePalabra.visibility = View.VISIBLE
        }
        lblCrearEditar.setTextColor(Color.parseColor(idioma.colTexto))

        btnAddPalabra.setBackgroundColor(Color.parseColor(idioma.colBoton))
        btnAddPalabra.setTextColor(Color.parseColor(idioma.colBotonTexto))
        btnAddPalabra.setOnClickListener {
            if (validate()) {
                if (accion == "crear") {
                    idPalabra = Database(this).insertPalabra(txtEditPalabra.text.toString(), idioma.idIdioma, spnTipoPalabra.selectedItemPosition + 1, txtDefinicion.text.toString()).toInt()
                } else {
                    Database(this).updatePalabra(idPalabra, txtEditPalabra.text.toString(), spnTipoPalabra.selectedItemPosition + 1, txtDefinicion.text.toString())
                }
                finish()
                var intent : Intent = Intent(this, Definicion::class.java)
                intent.putExtra("idPalabra", idPalabra)
                startActivity(intent)
            }
        }

        btnDeletePalabra.setBackgroundColor(Color.parseColor(idioma.colDelete))
        (btnDeletePalabra as MaterialButton).iconTint = ColorStateList.valueOf(Color.parseColor(idioma.colDeleteIcon))
        btnDeletePalabra.setOnClickListener {
            Database(this).deletePalabra(idPalabra)
            finish()
        }
    }

    fun validate()  : Boolean {
        if (txtEditPalabra.text.isNullOrBlank()) {
            Toast.makeText(this, "Ingrese un nombre para la palabra.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (txtDefinicion.text.isNullOrBlank()) {
            Toast.makeText(this, "Ingrese una definición para la palabra.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}