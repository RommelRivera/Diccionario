package com.rommelrivera.diccionario

class Palabra(val idPalabra : Int, var nombre : String, var idIdioma : Int, var idTipoPalabra : Int, var definicion : String) {

    val tipoPalabra : String
        get() {
            return TipoPalabra().tiposPalabras[idTipoPalabra]!!
        }
}
