package com.example.calculator

class PosishionHelper internal constructor(var operation: String, posishion: Int) :
    Comparable<Any?> {
    var priorety = 0
    var posishion: Int

    init {
        when (operation) {
            "+" -> priorety = 3
            "-" -> priorety = 3
            "\u00d7" -> priorety = 2
            "/" -> priorety = 2
            "^" -> priorety = 1
        }
        this.posishion = posishion
    }

    override fun compareTo(o: Any?): Int {
        return priorety - (o as PosishionHelper?)!!.priorety
    }

    override fun toString(): String {
        return "$operation позиция первого элемента: $posishion"
    }
}