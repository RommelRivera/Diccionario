package com.rommelrivera.diccionario

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton

class ColorDialog : AppCompatActivity() {

    private lateinit var llyColFondo : LinearLayout
    private lateinit var lblColTexto : TextView
    private lateinit var btnColBoton : Button
    private lateinit var btnColDelete : Button

    private lateinit var txtRed : TextView
    private lateinit var skbRed : SeekBar
    private lateinit var txtGreen : TextView
    private lateinit var skbGreen : SeekBar
    private lateinit var txtBlue : TextView
    private lateinit var skbBlue : SeekBar
    private lateinit var txtHex : TextView

    private lateinit var btnAplicarColores : Button
    private lateinit var btnCancelar : Button

    private var colBoton : String = "#666666"
    private var colFondo : String = "#FDF6FE"
    private var colTexto : String = "#444444"
    private var colBotonTexto : String = "#FFFFFF"
    private var colDelete : String = "#BB1010"
    private var colDeleteIcon : String = "#FFFFFF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.color_dialog)

        llyColFondo = findViewById(R.id.llyColFondo)
        lblColTexto = findViewById(R.id.lblColTexto)
        btnColBoton = findViewById(R.id.btnColBoton)
        btnColDelete = findViewById(R.id.btnColDelete)

        txtRed = findViewById(R.id.txtRed)
        skbRed = findViewById(R.id.skbRed)
        txtGreen = findViewById(R.id.txtGreen)
        skbGreen = findViewById(R.id.skbGreen)
        txtBlue = findViewById(R.id.txtBlue)
        skbBlue = findViewById(R.id.skbBlue)
        txtHex = findViewById(R.id.txtHex)

        btnAplicarColores = findViewById(R.id.btnAplicarColores)
        btnCancelar = findViewById(R.id.btnCancelar)

        var extras : Bundle = intent.extras!!
        colBoton = extras.getString("colBoton", "#666666")
        colFondo = extras.getString("colFondo", "#FDF6FE")
        colTexto = extras.getString("colTexto", "#444444")
        colBotonTexto = extras.getString("colBotonTexto", "#FFFFFF")
        colDelete = extras.getString("colDelete", "#BB1010")
        colDeleteIcon = extras.getString("colDeleteIcon", "#FFFFFF")

        llyColFondo.setBackgroundColor(Color.parseColor(colFondo))
        lblColTexto.setTextColor(Color.parseColor(colTexto))
        btnColBoton.setBackgroundColor(Color.parseColor(colBoton))
        btnColBoton.setTextColor(Color.parseColor(colBotonTexto))
        btnColDelete.setBackgroundColor(Color.parseColor(colDelete))
        (btnColDelete as MaterialButton).iconTint = ColorStateList.valueOf(Color.parseColor(colDeleteIcon))

        var colorEdit : String = ""
        when (extras.getString("accion", "")) {
            "colBoton" -> colorEdit = colBoton
            "colFondo" -> colorEdit = colFondo
            "colTexto" -> colorEdit = colTexto
            "colBotonTexto" -> colorEdit = colBotonTexto
            "colDelete" -> colorEdit = colDelete
            "colDeleteIcon" -> colorEdit = colDeleteIcon
        }

        var color : Int = Color.parseColor(colorEdit)

        txtRed.text = color.red.toString()
        skbRed.progress = color.red
        txtGreen.text = color.green.toString()
        skbGreen.progress = color.green
        txtBlue.text = color.blue.toString()
        skbBlue.progress = color.blue
        txtHex.text = colorEdit.substring(1, colorEdit.length)

        txtRed.addTextChangedListener {
            if (txtRed.text.isNullOrBlank()) txtRed.text = "0"
            if (txtRed.text.toString().toInt() > 255) txtRed.text = "255"
            skbRed.progress = txtRed.text.toString().toInt()
            (txtRed as EditText).setSelection(txtRed.text.length)
        }

        txtGreen.addTextChangedListener {
            if (txtGreen.text.isNullOrBlank()) txtGreen.text = "0"
            if (txtGreen.text.toString().toInt() > 255) txtGreen.text = "255"
            skbGreen.progress = txtGreen.text.toString().toInt()
            (txtGreen as EditText).setSelection(txtGreen.text.length)
        }

        txtBlue.addTextChangedListener {
            if (txtBlue.text.isNullOrBlank()) txtBlue.text = "0"
            if (txtBlue.text.toString().toInt() > 255) txtBlue.text = "255"
            skbBlue.progress = txtBlue.text.toString().toInt()
            (txtBlue as EditText).setSelection(txtBlue.text.length)
        }

        txtHex.addTextChangedListener(object : TextWatcher {
            var text : String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                text = txtHex.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var hexChars: Array<Char> = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
                var valid: Boolean = false
                if (!txtHex.text.isNullOrBlank()) {
                    for (i in hexChars.indices) {
                        if (txtHex.text.toString().uppercase()[txtHex.text.toString().length - 1] == hexChars[i]) {
                            valid = true
                        }
                    }
                    if (!valid) {
                        txtHex.text = text
                    }
                    if (txtHex.text.length == 6) {
                        var red : Int = txtHex.text.toString().substring(0,2).toInt(16)
                        var green : Int = txtHex.text.toString().substring(2,4).toInt(16)
                        var blue : Int = txtHex.text.toString().substring(4,6).toInt(16)
                        var color : Int = Color.rgb(red, green, blue)

                        txtRed.text = red.toString()
                        txtGreen.text = green.toString()
                        txtBlue.text = blue.toString()

                        (txtHex as EditText).selectAll()
                    }
                }
            }
        })

        skbRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtRed.text = progress.toString()
                color = Color.rgb(progress, color.green, color.blue)
                setColor(color, extras.getString("accion", ""))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        skbGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtGreen.text = progress.toString()
                color = Color.rgb(color.red, progress, color.blue)
                setColor(color,extras.getString("accion", ""))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        skbBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtBlue.text = progress.toString()
                color = Color.rgb(color.red, color.green, progress)
                setColor(color, extras.getString("accion", ""))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnAplicarColores.setOnClickListener {
            var intent : Intent = Intent()
            intent.putExtra("colBoton", colBoton)
            intent.putExtra("colFondo", colFondo)
            intent.putExtra("colTexto", colTexto)
            intent.putExtra("colBotonTexto", colBotonTexto)
            intent.putExtra("colDelete", colDelete)
            intent.putExtra("colDeleteIcon", colDeleteIcon)
            setResult(1, intent)
            finish()
        }

        btnCancelar.setOnClickListener {
            setResult(0)
            finish()
        }
    }

    fun setColor(color : Int, accion : String) {
        when (accion) {
            "colBoton" -> {
                btnColBoton.setBackgroundColor(color)
                colBoton = String.format("#%06X", (0xFFFFFF and color))
            }
            "colFondo" -> {
                llyColFondo.setBackgroundColor(color)
                colFondo = String.format("#%06X", (0xFFFFFF and color))
            }
            "colTexto" -> {
                lblColTexto.setTextColor(color)
                colTexto = String.format("#%06X", (0xFFFFFF and color))
            }
            "colBotonTexto" -> {
                btnColBoton.setTextColor(color)
                colBotonTexto = String.format("#%06X", (0xFFFFFF and color))
            }
            "colDelete" -> {
                btnColDelete.setBackgroundColor(color)
                colDelete = String.format("#%06X", (0xFFFFFF and color))
            }
            "colDeleteIcon" -> {
                (btnColDelete as MaterialButton).iconTint = ColorStateList.valueOf(color)
                colDeleteIcon = String.format("#%06X", (0xFFFFFF and color))
            }
        }
        txtHex.text = String.format("%06X", (0xFFFFFF and color))
    }

    override fun onBackPressed() {
        setResult(0)
        finish()
    }
}
