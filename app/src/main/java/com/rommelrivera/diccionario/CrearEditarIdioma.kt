package com.rommelrivera.diccionario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class CrearEditarIdioma : AppCompatActivity() {

    private lateinit var lblCrearEditar : TextView
    private lateinit var txtIdioma : TextView
    private lateinit var btnColorBotones : Button
    private lateinit var btnColorFondo : Button
    private lateinit var btnColorTexto : Button
    private lateinit var btnColorBotonTexto : Button
    private lateinit var btnColorDelete : Button
    private lateinit var btnColorDeleteIcon : Button
    private lateinit var btnDeleteIdioma : Button
    private lateinit var btnCrearEditarIdioma : Button

    private lateinit var nombre : String
    private var idioma : Idioma = Idioma(-1,"","","","","","","")
    private var colBoton : String = "#666666"
    private var colFondo : String = "#FDF6FE"
    private var colTexto : String = "#444444"
    private var colBotonTexto : String = "#FFFFFF"
    private var colDelete : String = "#BB1010"
    private var colDeleteIcon : String = "#FFFFFF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_editar_idioma)

        lblCrearEditar = findViewById(R.id.lblCrearEditarIdioma)
        txtIdioma = findViewById(R.id.txtCrearEditarIdioma)
        btnColorBotones = findViewById(R.id.btnColorBotones)
        btnColorFondo = findViewById(R.id.btnColorFondo)
        btnColorTexto = findViewById(R.id.btnColorTexto)
        btnColorBotonTexto = findViewById(R.id.btnColorBotonTexto)
        btnColorDelete = findViewById(R.id.btnColorDelete)
        btnColorDeleteIcon = findViewById(R.id.btnColorDeleteIcon)
        btnDeleteIdioma = findViewById(R.id.btnDeleteIdioma)
        btnCrearEditarIdioma = findViewById(R.id.btnAddIdioma)

        var extras : Bundle = intent.extras!!
        var accion : String = extras.getString("accion", "")
        nombre = extras.getString("idioma", "")

        txtIdioma.text = nombre

        if (accion == "crear") {
            lblCrearEditar.text = "Crear idioma"
            btnCrearEditarIdioma.text = "Crear"
        } else {
            lblCrearEditar.text = "Editar idioma"
            btnCrearEditarIdioma.text = "Editar"
            btnDeleteIdioma.visibility = View.VISIBLE

            idioma = Database(this).selectIdiomaByNombre(nombre)
            colBoton = idioma.colBoton
            colFondo = idioma.colFondo
            colTexto = idioma.colTexto
            colBotonTexto = idioma.colBotonTexto
            colDelete = idioma.colDelete
            colDeleteIcon = idioma.colDeleteIcon
        }

        btnColorBotones.setOnClickListener {
            var intent : Intent = Intent(this, ColorDialog::class.java)
            intent.putExtra("accion", "colBoton")
            intent.putExtra("colBoton", colBoton)
            intent.putExtra("colFondo", colFondo)
            intent.putExtra("colTexto", colTexto)
            intent.putExtra("colBotonTexto", colBotonTexto)
            intent.putExtra("colDelete", colDelete)
            intent.putExtra("colDeleteIcon", colDeleteIcon)
            startActivityForResult(intent, 0)
        }

        btnColorFondo.setOnClickListener {
            var intent : Intent = Intent(this, ColorDialog::class.java)
            intent.putExtra("accion", "colFondo")
            intent.putExtra("colBoton", colBoton)
            intent.putExtra("colFondo", colFondo)
            intent.putExtra("colTexto", colTexto)
            intent.putExtra("colBotonTexto", colBotonTexto)
            intent.putExtra("colDelete", colDelete)
            intent.putExtra("colDeleteIcon", colDeleteIcon)
            startActivityForResult(intent, 0)
        }

        btnColorTexto.setOnClickListener {
            var intent : Intent = Intent(this, ColorDialog::class.java)
            intent.putExtra("accion", "colTexto")
            intent.putExtra("colBoton", colBoton)
            intent.putExtra("colFondo", colFondo)
            intent.putExtra("colTexto", colTexto)
            intent.putExtra("colBotonTexto", colBotonTexto)
            intent.putExtra("colDelete", colDelete)
            intent.putExtra("colDeleteIcon", colDeleteIcon)
            startActivityForResult(intent, 0)
        }

        btnColorBotonTexto.setOnClickListener {
            var intent : Intent = Intent(this, ColorDialog::class.java)
            intent.putExtra("accion", "colBotonTexto")
            intent.putExtra("colBoton", colBoton)
            intent.putExtra("colFondo", colFondo)
            intent.putExtra("colTexto", colTexto)
            intent.putExtra("colBotonTexto", colBotonTexto)
            intent.putExtra("colDelete", colDelete)
            intent.putExtra("colDeleteIcon", colDeleteIcon)
            startActivityForResult(intent, 0)
        }

        btnColorDelete.setOnClickListener {
            var intent : Intent = Intent(this, ColorDialog::class.java)
            intent.putExtra("accion", "colDelete")
            intent.putExtra("colBoton", colBoton)
            intent.putExtra("colFondo", colFondo)
            intent.putExtra("colTexto", colTexto)
            intent.putExtra("colBotonTexto", colBotonTexto)
            intent.putExtra("colDelete", colDelete)
            intent.putExtra("colDeleteIcon", colDeleteIcon)
            startActivityForResult(intent, 0)
        }

        btnColorDeleteIcon.setOnClickListener {
            var intent : Intent = Intent(this, ColorDialog::class.java)
            intent.putExtra("accion", "colDeleteIcon")
            intent.putExtra("colBoton", colBoton)
            intent.putExtra("colFondo", colFondo)
            intent.putExtra("colTexto", colTexto)
            intent.putExtra("colBotonTexto", colBotonTexto)
            intent.putExtra("colDelete", colDelete)
            intent.putExtra("colDeleteIcon", colDeleteIcon)
            startActivityForResult(intent, 0)
        }

        btnCrearEditarIdioma.setOnClickListener {
            if (!txtIdioma.text.isNullOrBlank()) {
                if (accion == "crear") {
                    if (!Database(this).checkIdioma(txtIdioma.text.toString())){
                        Database(this).insertIdioma(txtIdioma.text.toString(), colBoton, colFondo, colTexto, colBotonTexto, colDelete, colDeleteIcon)
                        var intent: Intent = Intent(this, Lista::class.java)
                        intent.putExtra("idioma", txtIdioma.text.toString())
                        //setResult(1, intent)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Ya existe un idioma con ese nombre.", Toast.LENGTH_SHORT).show()
                    }
                } else if (Database(this).checkIdioma(txtIdioma.text.toString()) && idioma.idIdioma != Database(this).selectIdiomaByNombre(txtIdioma.text.toString()).idIdioma) {
                    Toast.makeText(this, "Ya existe un idioma con ese nombre.", Toast.LENGTH_SHORT).show()
                } else {
                    Database(this).updateIdioma(idioma.idIdioma, txtIdioma.text.toString(), colBoton, colFondo, colTexto, colBotonTexto, colDelete, colDeleteIcon)
                    var intent: Intent = Intent()
                    intent.putExtra("idioma", txtIdioma.text.toString())
                    setResult(1, intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Ingrese un nombre para el idioma.", Toast.LENGTH_SHORT).show()
            }
        }

        btnDeleteIdioma.setOnClickListener {
            Database(this).deleteIdioma(nombre)
            setResult(-1)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != 1) {
            if (resultCode != 0) finish()
            return
        }

        var extras : Bundle = data!!.extras!!
        this.colBoton = extras.getString("colBoton", this.colBoton)
        this.colTexto = extras.getString("colTexto", this.colTexto)
        this.colFondo = extras.getString("colFondo", this.colFondo)
        this.colBotonTexto = extras.getString("colBotonTexto", this.colBotonTexto)
        this.colDelete = extras.getString("colDelete", this.colDelete)
        this.colDeleteIcon = extras.getString("colDeleteIcon", this.colDeleteIcon)
    }
}