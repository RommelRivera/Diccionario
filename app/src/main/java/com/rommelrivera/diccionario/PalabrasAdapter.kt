package com.rommelrivera.diccionario

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.shape.MaterialShapeDrawable

class PalabrasAdapter(private var context : Context, private var palabras : Array<Palabra>, private var colTexto : String) : RecyclerView.Adapter<PalabrasAdapter.PalabrasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PalabrasHolder {
        var view : View = LayoutInflater.from(context).inflate(R.layout.lista, parent, false)
        return PalabrasHolder(view)
    }

    override fun onBindViewHolder(holder: PalabrasHolder, position: Int) {
        holder.init(position)
    }

    override fun getItemCount(): Int {
        return palabras.size
    }

    inner class PalabrasHolder(itemView : View) : ViewHolder(itemView) {
        private lateinit var item : LinearLayout
        private lateinit var lblPalabra : TextView
        private lateinit var lblTipo : TextView
        private lateinit var lblIdPalabra : TextView

        init {
            item = itemView.findViewById(R.id.item)
            lblPalabra = itemView.findViewById(R.id.lblPalabra)
            lblTipo = itemView.findViewById(R.id.lblTipo)
            lblIdPalabra = itemView.findViewById(R.id.lblIdPalabra)
        }

        fun init(position: Int) {
            val borderPalabra : MaterialShapeDrawable = MaterialShapeDrawable()
            borderPalabra.fillColor = ColorStateList.valueOf(Color.TRANSPARENT)
            borderPalabra.setStroke(5.0f, Color.parseColor(colTexto))

            val borderTipo : MaterialShapeDrawable = MaterialShapeDrawable()
            borderTipo.fillColor = ColorStateList.valueOf(Color.TRANSPARENT)
            borderTipo.setStroke(5.0f, Color.parseColor(colTexto))

            lblPalabra.text = palabras[position].nombre
            lblPalabra.setTextColor(Color.parseColor(colTexto))
            lblPalabra.background = borderPalabra
            lblTipo.text = palabras[position].tipoPalabra
            lblTipo.setTextColor(Color.parseColor(colTexto))
            lblTipo.background = borderTipo
            lblIdPalabra.text = palabras[position].idPalabra.toString()

            item.setOnClickListener {
                var intent : Intent = Intent(context, Definicion::class.java)
                intent.putExtra("idPalabra", lblIdPalabra.text.toString().toInt())
                context.startActivity(intent)
            }
        }
    }
}
