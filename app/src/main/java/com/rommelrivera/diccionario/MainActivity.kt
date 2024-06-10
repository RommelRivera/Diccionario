package com.rommelrivera.diccionario

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class MainActivity : AppCompatActivity() {

    private lateinit var spnIdioma : Spinner
    private lateinit var btnElegirIdioma : Button
    private lateinit var btnCrearIdioma : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spnIdioma = findViewById(R.id.spnIdioma)
        btnCrearIdioma = findViewById(R.id.btnCrearIdioma)
        btnElegirIdioma = findViewById(R.id.btnElegirIdioma)
        //btnCrearIdioma.setTextColor(Color.parseColor("#FF00FF"))

        update()

        btnElegirIdioma.setOnClickListener {
            var intent : Intent = Intent(this, Lista::class.java)
            intent.putExtra("idioma", (spnIdioma.selectedView as TextView).text.toString())
            startActivity(intent)
        }

        btnCrearIdioma.setOnClickListener {
            var intent : Intent = Intent(this, CrearEditarIdioma::class.java)
            intent.putExtra("accion", "crear")
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        update()
    }

    fun update() {
        var database : Database = Database(this)

        var idiomas : Array<Idioma> = database.selectIdiomas()

        if (idiomas.isEmpty()) {
            var constraint : ConstraintLayout = findViewById(R.id.start)
            btnElegirIdioma.visibility = View.GONE
            var consBtnCrearIdioma = ConstraintSet()
            consBtnCrearIdioma.clone(constraint)
            consBtnCrearIdioma.setMargin(R.id.btnCrearIdioma, ConstraintSet.TOP, 130)
            consBtnCrearIdioma.applyTo(constraint)
        } else {
            var constraint : ConstraintLayout = findViewById(R.id.start)
            btnElegirIdioma.visibility = View.VISIBLE
            var consBtnCrearIdioma = ConstraintSet()
            consBtnCrearIdioma.clone(constraint)
            consBtnCrearIdioma.setMargin(R.id.btnCrearIdioma, ConstraintSet.TOP, 16)
            consBtnCrearIdioma.applyTo(constraint)
        }

        var nombres : MutableList<String> = mutableListOf()
        for (i in idiomas.indices) {
            nombres.add(idiomas[i].nombre)
        }

        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnIdioma.adapter = adapter
    }
}