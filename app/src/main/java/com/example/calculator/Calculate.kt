package com.example.calculator

import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.regex.Pattern

object Calculate {
    const val E = 2.7182818285
    const val PI = 3.1415926536
    var isRad = false
    @Throws(NumberFormatException::class, RuntimeException::class)
    fun getResult(s: String): String {
        var s = s
        while (s.contains("(")) {
            var end = s.indexOf(")")
            var start = s.lastIndexOf("(", end)
            val s1 = getResult(s.substring(++start, end))
            val s2 = s.substring(--start, ++end)
            println(s2)
            s = s.replace(s2, s1)
        }
        return calculation(s)
    }

    @Throws(NumberFormatException::class, RuntimeException::class)
    private fun calculation(s: String): String {
        var s = s
        var boo = false
        if (s.isEmpty()) {
            throw NumberFormatException()
        }
        if (s.toCharArray()[0] == '-') {
            s = s.substring(1)
            boo = true
        }
        val pattern = Pattern.compile("[\\^\u00d7+/-]")
        val strings = pattern.split(s)
        if (boo) {
            strings[0] = "-" + strings[0]
        }
        val numbers = getNumbers(strings)
        val pattern2 = Pattern.compile("\\d\\.\\d|\\d|sin|cos|tg|log|lg|ln|\u221A|!|E|:")
        val znaki = pattern2.split(s)
        val listznaki = deleteEmpty(znaki)
        val operations = getPosishonList(listznaki)
        for (i in operations.indices) {
            if (numbers.size > 1) {
                val pos = operations[i].posishion
                val pos2 = pos + 1
                val a = numbers[pos]
                val b = numbers[pos2]
                val c = doOperation(operations[i].operation, a, b)
                numbers[pos] = c
                numbers.removeAt(pos2)
                for (j in i until operations.size) {
                    if (operations[j].posishion >= pos) {
                        operations[j].posishion--
                    }
                }
            }
        }
        val symbols = DecimalFormatSymbols.getInstance()
        symbols.decimalSeparator = '.'
        val d = numbers[0] as Double
        val format = DecimalFormat("#.##########", symbols)
        return format.format(d)
    }

    @Throws(NumberFormatException::class)
    private fun getNumbers(strings: Array<String>): MutableList<Number> {
        val list: MutableList<Number> = ArrayList()
        for (s in strings) {
            list.add(doOperationMore(s))
        }
        return list
    }

    private fun getPosishonList(strings: List<String>): List<PosishionHelper> {
        val list: MutableList<PosishionHelper> = ArrayList()
        for (i in strings.indices) {
            list.add(PosishionHelper(strings[i], i))
        }
        Collections.sort(list)
        return list
    }

    private fun deleteEmpty(strings: Array<String>): List<String> {
        val list: MutableList<String> = ArrayList()
        for (i in strings.indices) {
            if (!strings[i].isEmpty()) {
                list.add(strings[i])
            }
        }
        return list
    }

    @Throws(RuntimeException::class)
    private fun doOperation(s: String?, a: Number, b: Number): Number {
        var c = 0.0
        when (s) {
            "+" -> c = a as Double + b as Double
            "-" -> c = a as Double - b as Double
            "\u00d7" -> c = a as Double * b as Double
            "/" -> {
                if (b as Double == 0.0) {
                    throw RuntimeException("Деление на ноль")
                }
                c = a as Double / b
            }
            "^" -> c = Math.pow(a as Double, b as Double)
        }
        return c
    }

    @Throws(NumberFormatException::class)
    private fun doOperationMore(s: String): Double {
        var s = s
        val pattern = Pattern.compile("sin|cos|tg|log|lg|ln|\u221A|!")
        val matcher = pattern.matcher(s)
        return if (matcher.find()) {
            val end = matcher.end()
            val str = s.substring(end)
            if (str.isEmpty()) {
                val a = s.substring(0, end - 1)
                factorial(a.toDouble())
            } else {
                s = s.substring(0, end)
                doFunctions(s, str)
            }
        } else {
            s.toDouble()
        }
    }

    private fun doFunctions(s: String, d: String): Double {
        var c = 0.0
        when (s) {
            "sin" -> c = if (isRad) {
                Math.sin(d.toDouble())
            } else {
                Math.sin(Math.toRadians(d.toDouble()))
            }
            "cos" -> c = if (isRad) {
                Math.cos(d.toDouble())
            } else {
                Math.cos(Math.toRadians(d.toDouble()))
            }
            "tg" -> c = if (isRad) {
                Math.tan(d.toDouble())
            } else {
                Math.tan(Math.toRadians(d.toDouble()))
            }
            "lg" -> c = Math.log10(d.toDouble())
            "ln" -> c = Math.log(d.toDouble())
            "log" -> {
                val points = d.indexOf(':')
                if (points == -1) {
                    throw NumberFormatException()
                }
                val number = d.substring(0, points).toDouble()
                val base = d.substring(points + 1).toDouble()
                c = Math.log10(number) / Math.log10(base)
            }
            "\u221A" -> c = Math.sqrt(d.toDouble())
        }
        return c
    }

    private fun factorial(d: Double): Double {
        var n = BigInteger("1")
        var i = 1
        while (i <= d) {
            n = n.multiply(BigInteger(i.toString()))
            i++
        }
        return n.toDouble()
    }

    @Throws(NumberFormatException::class)
    fun toRadix(s: String?, radix: Int): String {
        val a = s!!.toLong()
        return java.lang.Long.toString(a, radix).uppercase(Locale.getDefault())
    }
}