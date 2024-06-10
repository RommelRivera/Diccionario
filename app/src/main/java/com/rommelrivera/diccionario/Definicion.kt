package com.rommelrivera.diccionario

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

class Definicion : AppCompatActivity() {

    private lateinit var lblDefPalabra : TextView
    private lateinit var lblDefTipo : TextView
    private lateinit var lblDefinicion : TextView
    private lateinit var btnEditar : Button
    private lateinit var extras : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicion)

        extras = intent.extras!!
        lblDefPalabra = findViewById(R.id.lblDefPalabra)
        lblDefTipo = findViewById(R.id.lblDefTipo)
        lblDefinicion = findViewById(R.id.lblDefinicion)
        btnEditar = findViewById(R.id.btnEditar)

        var palabra : Palabra = Database(this).selectPalabraById(extras.getInt("idPalabra"))
        var idioma : Idioma = Database(this).selectIdiomaById(palabra.idIdioma)

        findViewById<ConstraintLayout>(R.id.base).setBackgroundColor(Color.parseColor(idioma.colFondo))
        window.statusBarColor = Color.parseColor(idioma.colBoton)

        lblDefPalabra.text = palabra.nombre
        lblDefPalabra.setTextColor(Color.parseColor(idioma.colTexto))
        lblDefTipo.text = palabra.tipoPalabra
        lblDefTipo.setTextColor(Color.parseColor(idioma.colTexto))
        lblDefinicion.text = palabra.definicion
        lblDefinicion.setTextColor(Color.parseColor(idioma.colTexto))

        btnEditar.setBackgroundColor(Color.parseColor(idioma.colBoton))
        (btnEditar as MaterialButton).iconTint = ColorStateList.valueOf(Color.parseColor(idioma.colBotonTexto))
        btnEditar.setOnClickListener {
            finish()
            var intent = Intent(this, CrearEditarPalabra::class.java)
            intent.putExtra("idPalabra", palabra.idPalabra)
            intent.putExtra("idIdioma", palabra.idIdioma)
            intent.putExtra("idTipoPalabra", palabra.idTipoPalabra)
            startActivity(intent)
        }
    }
}