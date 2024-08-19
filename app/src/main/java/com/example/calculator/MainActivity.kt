package com.example.calculator

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var countBracket = 0
    private var isRad = false
    private val off = false
    private var radix = 0
    private var isVisibleHistory = false
    private val db: SQLiteDatabase? = null
    private val cursor: Cursor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<View>(R.id.edit) as EditText
        editText.showSoftInputOnFocus = false
        editText.requestFocus()
        if (savedInstanceState != null) {
            val result = findViewById<View>(R.id.result) as TextView
            val button = findViewById<View>(R.id.buttonBinary) as Button
            val statusBinary = findViewById<View>(R.id.statusBinary) as TextView
            val resultBinary = findViewById<View>(R.id.resultBinary) as TextView
            editText.setText(savedInstanceState.getString("Formula"))
            result.text = savedInstanceState.getString("Result")
            val start = savedInstanceState.getInt("Start")
            val end = savedInstanceState.getInt("End")
            if (start == end) {
                editText.setSelection(start)
            } else {
                editText.setSelection(start, end)
            }
            radix = savedInstanceState.getInt("Radix")
            statusBinary.text = savedInstanceState.getString("StatusBinary")
            resultBinary.text = savedInstanceState.getString("ResultBinary")
            button.text = savedInstanceState.getString("Button")
        }
        if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            val handler = Handler()
            handler.postDelayed({
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }, 5000)
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val editText = findViewById<View>(R.id.edit) as EditText
        val result = findViewById<View>(R.id.result) as TextView
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val button = findViewById<View>(R.id.buttonBinary) as Button
        val statusBinary = findViewById<View>(R.id.statusBinary) as TextView
        val resultBinary = findViewById<View>(R.id.resultBinary) as TextView
        savedInstanceState.putString("Result", result.text.toString())
        savedInstanceState.putString("Formula", editText.text.toString())
        savedInstanceState.putInt("Start", start)
        savedInstanceState.putInt("End", end)
        savedInstanceState.putInt("Radix", radix)
        savedInstanceState.putString("StatusBinary", statusBinary.text.toString())
        savedInstanceState.putString("ResultBinary", resultBinary.text.toString())
        savedInstanceState.putString("Button", button.text.toString())
    }

    fun onClickOne(view: View?) {
        addDigit('1')
    }

    fun onClickTwo(view: View?) {
        addDigit('2')
    }

    fun onClickThree(view: View?) {
        addDigit('3')
    }

    fun onClickFour(view: View?) {
        addDigit('4')
    }

    fun onClickFive(view: View?) {
        addDigit('5')
    }

    fun onClickSix(view: View?) {
        addDigit('6')
    }

    fun onClickSeven(view: View?) {
        addDigit('7')
    }

    fun onClickEight(view: View?) {
        addDigit('8')
    }

    fun onClickNine(view: View?) {
        addDigit('9')
    }

    fun onClickZero(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        var start = editText.selectionStart
        val end = editText.selectionEnd
        val sb = StringBuilder(editText.text)
        val counts = numberLength(sb, start)
        if (counts[0] >= 15) {
            val toast = Toast.makeText(this, "Не больше 15 цифр!", Toast.LENGTH_SHORT)
            toast.show()
            return
        }
        if (counts[1] >= 10) {
            if (start > counts[2]) {
                val toast =
                    Toast.makeText(this, "Не больше 10 цифр после точки!", Toast.LENGTH_SHORT)
                toast.show()
                return
            }
        }
        if (sb.length == 1 && sb[start - 1] == '0') {
            show()
            return
        } else if (start == 0) {
            if (isPointInNumber(sb, start)) {
                show()
                return
            } else {
                if (start == end) {
                    sb.insert(start, '0')
                    sb.insert(++start, '.')
                } else {
                    sb.delete(start, end)
                    sb.insert(start, '0')
                    sb.insert(++start, '.')
                }
                editText.setText(sb)
                editText.setSelection(++start)
            }
        } else if (sb.length > 1 && sb[start - 1] == '0' && isZnak(sb[start - 2])) {
            if (start == end) {
                sb.insert(start, '.')
            } else {
                sb.delete(start, end)
                sb.insert(start, ".")
            }
            editText.setText(sb)
            editText.setSelection(++start)
        } else {
            if (start == end) {
                sb.insert(start, '0')
            } else {
                sb.delete(start, end)
                sb.insert(start, "0")
            }
            editText.setText(sb)
            editText.setSelection(++start)
        }
    }

    fun onClickPoint(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        var start = editText.selectionStart
        if (sb.length == 0 || start == 0) {
            sb.insert(start, '0')
            sb.insert(++start, '.')
            editText.setText(sb)
            editText.setSelection(2)
        } else {
            val end = editText.selectionEnd
            val znak = sb[start - 1]
            if (!isPointInNumber(sb, start)) {
                if (isZnak(znak)) {
                    sb.insert(start, '0')
                    sb.insert(++start, '.')
                } else if (isPointInNumber(sb, start) && start == end) {
                    sb.insert(start, '.')
                } else {
                    sb.delete(start, end)
                    sb.insert(start, '.')
                }
                editText.setText(sb)
                editText.setSelection(++start)
            } else {
                show()
            }
        }
    }

    fun onClickUp(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val textView = findViewById<View>(R.id.result) as TextView
        val s = textView.text.toString()
        if (s != "Result") {
            editText.setText(s)
            editText.setSelection(s.length)
        }
    }

    fun onClickDelete(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        var start = editText.selectionStart
        if (sb.length == 0 || start == 0) {
            return
        }
        val end = editText.selectionEnd
        val znak = sb[start - 1]
        if (znak == ')') {
            countBracket++
        } else if (znak == '(') {
            countBracket--
        }
        if (start == end) {
            val s = sb.deleteCharAt(--start)
            if (s.toString().contains(".")) {
            }
        } else {
            sb.delete(start, end)
        }
        editText.setText(sb)
        editText.setSelection(start)
    }

    fun onClickPlus(view: View?) {
        addOperation('+')
    }

    fun onClickMinus(view: View?) {
        addOperation('-')
    }

    fun onClickMultiply(view: View?) {
        addOperation('\u00d7')
    }

    fun onClickDivide(view: View?) {
        addOperation('/')
    }

    fun onClickReset(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        editText.setText("")
        val textView = findViewById<View>(R.id.result) as TextView
        textView.text = ""
        val resultBinary = findViewById<View>(R.id.resultBinary) as TextView
        resultBinary.text = ""
        countBracket = 0
    }

    fun onClickRad(view: View?) {
        val rad = findViewById<View>(R.id.rad) as TextView
        if (isRad) {
            rad.text = ""
            isRad = false
            Calculate.isRad = false
        } else {
            rad.text = "Rad"
            isRad = true
            Calculate.isRad = true
        }
    }

    fun onClick2x(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "2^(")
        } else {
            sb.delete(start, end)
            sb.insert(start, "2^(")
        }
        countBracket++
        editText.setText(sb)
        editText.setSelection(start + 3)
    }

    fun onClickX2(view: View?) {
        addOperation('^')
        addDigit('2')
    }

    fun onClickX3(view: View?) {
        addOperation('^')
        addDigit('3')
    }

    fun onClickXy(view: View?) {
        addOperation('^')
    }

    fun onClickSin(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "sin(")
        } else {
            sb.delete(start, end)
            sb.insert(start, "sin(")
        }
        countBracket++
        editText.setText(sb)
        editText.setSelection(start + 4)
    }

    fun onClickCos(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "cos(")
        } else {
            sb.delete(start, end)
            sb.insert(start, "cos(")
        }
        countBracket++
        editText.setText(sb)
        editText.setSelection(start + 4)
    }

    fun onClickTg(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "tg(")
        } else {
            sb.delete(start, end)
            sb.insert(start, "tg(")
        }
        countBracket++
        editText.setText(sb)
        editText.setSelection(start + 3)
    }

    fun onClickSqrt(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "\u221A(")
        } else {
            sb.delete(start, end)
            sb.insert(start, "\u221A(")
        }
        countBracket++
        editText.setText(sb)
        editText.setSelection(start + 2)
    }

    fun onClickLn(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "ln(")
        } else {
            sb.delete(start, end)
            sb.insert(start, "ln(")
        }
        countBracket++
        editText.setText(sb)
        editText.setSelection(start + 3)
    }

    fun onClickLg(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "lg(")
        } else {
            sb.delete(start, end)
            sb.insert(start, "lg(")
        }
        countBracket++
        editText.setText(sb)
        editText.setSelection(start + 3)
    }

    fun onClickLogxY(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "log:")
        } else {
            sb.delete(start, end)
            sb.insert(start, "log:")
        }
        editText.setText(sb)
        editText.setSelection(start + 3)
    }

    fun onClickXfac(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        var start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, "!")
        } else {
            sb.delete(start, end)
            sb.insert(start, "!")
        }
        editText.setText(sb)
        editText.setSelection(++start)
    }

    fun onClickPi(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, Calculate.PI)
        } else {
            sb.delete(start, end)
            sb.insert(start, Calculate.PI)
        }
        editText.setText(sb)
        editText.setSelection(start + 12)
    }

    fun onClickE(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        val start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == end) {
            sb.insert(start, Calculate.E)
        } else {
            sb.delete(start, end)
            sb.insert(start, Calculate.E)
        }
        editText.setText(sb)
        editText.setSelection(start + 12)
    }

    fun addDigit(c: Char) {
        val editText = findViewById<View>(R.id.edit) as EditText
        var start = editText.selectionStart
        val end = editText.selectionEnd
        val sb = StringBuilder(editText.text)
        val counts = numberLength(sb, start)
        if (counts[0] >= 15) {
            val toast = Toast.makeText(this, "Не больше 15 цифр!", Toast.LENGTH_SHORT)
            toast.show()
            return
        }
        if (counts[1] >= 10) {
            if (start > counts[2]) {
                val toast =
                    Toast.makeText(this, "Не больше 10 цифр после точки!", Toast.LENGTH_SHORT)
                toast.show()
                return
            }
        }
        if (sb.length > 1 && start != 0 && sb[start - 1] == ')') {
            show()
            return
        }
        if (start == 0) {
            if (start == end) {
                sb.insert(start, c)
            } else {
                sb.delete(start, end)
                sb.insert(start, c)
            }
            editText.setText(sb)
            editText.setSelection(++start)
            return
        }
        if (sb.length == 1 && sb[start - 1] == '0') {
            if (start == end) {
                sb.insert(start, '.')
                sb.insert(++start, c)
            } else {
                sb.delete(start, end)
                sb.insert(start, ".")
                sb.insert(++start, c)
            }
            editText.setText(sb)
            editText.setSelection(++start)
        } else if (sb.length > 1 && sb[start - 1] == '0' && isZnak(sb[start - 2])) {
            if (start == end) {
                sb.insert(start, '.')
                sb.insert(++start, c)
            } else {
                sb.delete(start, end)
                sb.insert(start, ".")
                sb.insert(++start, c)
            }
            editText.setText(sb)
            editText.setSelection(++start)
        } else {
            if (start == end) {
                sb.insert(start, c)
            } else {
                sb.delete(start, end)
                sb.insert(start, c)
            }
            editText.setText(sb)
            editText.setSelection(++start)
        }
    }

    fun addOperation(c: Char) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        var start = editText.selectionStart
        val end = editText.selectionEnd
        if (start == 0 && c == '-') {
            sb.insert(start, c)
            editText.setText(sb)
            editText.setSelection(++start)
        } else if (start == 0) {
            show()
            /*  } else if (sb.length() > 2 && sb.charAt(start - 2) == '(') {
            show();
            return;*/
        } else {
            val znak = sb[start - 1]
            if (isZnak(znak) || znak == '.') {
                if (start == 1) {
                    show()
                    return
                } else {
                    sb.deleteCharAt(--start)
                    sb.insert(start, c)
                }
            } else if (znak == '(' && c != '-') {
                show()
                return
            } else if (start == end) {
                sb.insert(start, c)
            } else {
                sb.delete(start, end)
                sb.insert(start, c)
            }
            editText.setText(sb)
            editText.setSelection(++start)
        }
    }

    fun onClickBrackets(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        var start = editText.selectionStart
        if (sb.length == 0 || start == 0) {
            sb.insert(0, '(')
            editText.setText(sb)
            editText.setSelection(1)
            countBracket++
        } else {
            val end = editText.selectionEnd
            val znak = sb[start - 1]
            if (znak == '(' || isZnak(znak)) {
                if (start == end) {
                    sb.insert(start, '(')
                } else {
                    sb.delete(start, end)
                    sb.insert(start, '(')
                }
                editText.setText(sb)
                editText.setSelection(++start)
                countBracket++
            } else if (countBracket != 0 && !isZnak(znak)) {
                if (start == end) {
                    sb.insert(start, ')')
                } else {
                    sb.delete(start, end)
                    sb.insert(start, ')')
                }
                editText.setText(sb)
                editText.setSelection(++start)
                countBracket--
            } else {
                show()
            }
        }
    }

    fun onClickEnter(view: View?) {
        val editText = findViewById<View>(R.id.edit) as EditText
        val sb = StringBuilder(editText.text)
        if (sb.length == 0) {
            return
        }
        if (sb.length == 1 && sb[0] == '-') {
            show()
            return
        }
        val end = sb.length - 1
        val znak = sb[end]
        if (isZnak(znak) || znak == '.') {
            sb.deleteCharAt(end)
        }
        if (countBracket != 0) {
            while (countBracket != 0) {
                sb.append(')')
                countBracket--
            }
        }
        val result = findViewById<View>(R.id.result) as TextView
        try {
            val res = Calculate.getResult(sb.toString())
            result.text = res
            editText.setText(sb)
            editText.setSelection(sb.length)
            if (radix != 0) {
                val resultBinary = findViewById<View>(R.id.resultBinary) as TextView
                val statusBinary = findViewById<View>(R.id.statusBinary) as TextView
                val button = findViewById<View>(R.id.buttonBinary) as Button
                try {
                    resultBinary.text = Calculate.toRadix(res, radix)
                } catch (e: NumberFormatException) {
                    radix = 0
                    statusBinary.text = ""
                    resultBinary.text = ""
                    button.text = "->x₂"
                    show("Только с целыми числами")
                }
            }
        } catch (e: NumberFormatException) {
            show()
            editText.setText("")
            result.text = ""
            if (radix != 0) {
                val resultBinary = findViewById<View>(R.id.resultBinary) as TextView
                resultBinary.text = ""
            }
        } catch (e: RuntimeException) {
            show(e.message)
            editText.setText("")
            result.text = ""
            if (radix != 0) {
                val resultBinary = findViewById<View>(R.id.resultBinary) as TextView
                resultBinary.text = ""
            }
        }
    }

    fun onClickOrientation(view: View?) {
        requestedOrientation =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }
    }

    fun onClickHistory(view: View?) {
        val scrollView = findViewById<View>(R.id.scroll) as ScrollView
        val gridLayout = findViewById<View>(R.id.grid) as GridLayout
        if (isVisibleHistory) {
            gridLayout.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
            isVisibleHistory = false
        } else {
            gridLayout.visibility = View.INVISIBLE
            scrollView.visibility = View.VISIBLE
            isVisibleHistory = true
        }
    }

    fun onClickBinary(view: View?) {
        val result = findViewById<View>(R.id.result) as TextView
        val s = result.text.toString()
        val button = findViewById<View>(R.id.buttonBinary) as Button
        val statusBinary = findViewById<View>(R.id.statusBinary) as TextView
        val resultBinary = findViewById<View>(R.id.resultBinary) as TextView
        var resultBinaryString: String? = ""
        when (radix) {
            0 -> {
                radix = 2
                statusBinary.text = "x₁₀->x₂:"
                button.text = "->x₈"
            }
            2 -> {
                radix = 8
                statusBinary.text = "x₁₀->x₈:"
                button.text = "->x₁₆"
            }
            8 -> {
                radix = 16
                statusBinary.text = "x₁₀->x₁₆:"
                button.text = "Off"
            }
            16 -> {
                radix = 0
                statusBinary.text = ""
                resultBinary.text = ""
                button.text = "->x₂"
                return
            }
        }
        if (s.isEmpty()) {
            return
        }
        try {
            resultBinaryString = Calculate.toRadix(s, radix)
            resultBinary.text = resultBinaryString
        } catch (e: NumberFormatException) {
            radix = 0
            statusBinary.text = ""
            resultBinary.text = ""
            button.text = "->x₂"
            show("Только для целых чисел")
        }
    }

    fun isPointInNumber(sb: StringBuilder, point: Int): Boolean {
        val chars = sb.toString().toCharArray()
        var b = false
        for (i in point until chars.size) {
            if (isZnak(chars[i])) {
                break
            }
            if (chars[i] == '.') {
                b = true
                break
            }
        }
        for (i in point - 1 downTo 0) {
            if (isZnak(chars[i])) {
                break
            }
            if (chars[i] == '.') {
                b = true
                break
            }
        }
        return b
    }

    fun onClickErase(view: View?) {}
    fun numberLength(sb: StringBuilder, point: Int): IntArray {
        val chars = sb.toString().toCharArray()
        val counts = intArrayOf(0, 0, 0)
        var countn = 0
        var end = 0
        var countp = 0
        var pointPosishion = 0
        var b = false
        for (i in point until chars.size) {
            if (isZnak(chars[i])) {
                break
            }
            if (chars[i] == '.') {
                pointPosishion = i
                b = true
                countn--
            }
            countn++
            end = i
        }
        if (end == 0) {
            end = sb.length - 1
        }
        for (i in point - 1 downTo 0) {
            if (isZnak(chars[i])) {
                break
            }
            if (chars[i] == '.') {
                pointPosishion = i
                b = true
                countn--
            }
            countn++
        }
        if (b) {
            for (i in end downTo 0) {
                if (chars[i] == '.') {
                    break
                }
                countp++
            }
        }
        counts[0] = countn
        counts[1] = countp
        counts[2] = pointPosishion
        return counts
    }

    fun isZnak(c: Char): Boolean {
        return if (c == '+' || c == '-' || c == '\u00d7' || c == '/' || c == '^') {
            true
        } else {
            false
        }
    }

    fun show() {
        val toast = Toast.makeText(this, "Неверный формат!", Toast.LENGTH_SHORT)
        toast.show()
    }

    fun show(s: String?) {
        val toast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        toast.show()
    }
}