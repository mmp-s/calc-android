package br.ufpe.cin.android.calculadora

import android.R.attr.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // declare EditText and TextView elements
        val text_calc = findViewById<EditText>(R.id.text_calc)
        val text_info = findViewById<TextView>(R.id.text_info)

        // declare each button and set its click listener to add the value to EditText element
        val btn_0 = findViewById<Button>(R.id.btn_0)
        btn_0.setOnClickListener {
            text_calc.append("0")
        }

        val btn_1 = findViewById<Button>(R.id.btn_1)
        btn_1.setOnClickListener {
            text_calc.append("1")
        }

        val btn_2 = findViewById<Button>(R.id.btn_2)
        btn_2.setOnClickListener {
            text_calc.append("2")
        }

        val btn_3 = findViewById<Button>(R.id.btn_3)
        btn_3.setOnClickListener {
            text_calc.append("3")
        }

        val btn_4 = findViewById<Button>(R.id.btn_4)
        btn_4.setOnClickListener {
            text_calc.append("4")
        }

        val btn_5 = findViewById<Button>(R.id.btn_5)
        btn_5.setOnClickListener {
            text_calc.append("5")
        }

        val btn_6 = findViewById<Button>(R.id.btn_6)
        btn_6.setOnClickListener {
            text_calc.append("6")
        }

        val btn_7 = findViewById<Button>(R.id.btn_7)
        btn_7.setOnClickListener {
            text_calc.append("7")
        }

        val btn_8 = findViewById<Button>(R.id.btn_8)
        btn_8.setOnClickListener {
            text_calc.append("8")
        }

        val btn_9 = findViewById<Button>(R.id.btn_9)
        btn_9.setOnClickListener {
            text_calc.append("9")
        }

        val btn_Divide = findViewById<Button>(R.id.btn_Divide)
        btn_Divide.setOnClickListener {
            val new_text = text_calc.text.toString() + btn_Divide.text.toString()
            text_calc.setText(new_text)
        }

        val btn_Multiply = findViewById<Button>(R.id.btn_Multiply)
        btn_Multiply.setOnClickListener {
            val new_text = text_calc.text.toString() + btn_Multiply.text.toString()
            text_calc.setText(new_text)
        }

        val btn_Subtract = findViewById<Button>(R.id.btn_Subtract)
        btn_Subtract.setOnClickListener {
            val new_text = text_calc.text.toString() + btn_Subtract.text.toString()
            text_calc.setText(new_text)
        }

        val btn_Dot = findViewById<Button>(R.id.btn_Dot)
        btn_Dot.setOnClickListener {
            text_calc.append(".")
        }

        val btn_Add = findViewById<Button>(R.id.btn_Add)
        btn_Add.setOnClickListener {
            val new_text = text_calc.text.toString() + btn_Add.text.toString()
            text_calc.setText(new_text)
        }

        val btn_LParen = findViewById<Button>(R.id.btn_LParen)
        btn_LParen.setOnClickListener {
            val new_text = text_calc.text.toString() + btn_LParen.text.toString()
            text_calc.setText(new_text)
        }

        val btn_RParen = findViewById<Button>(R.id.btn_RParen)
        btn_RParen.setOnClickListener {
            val new_text = text_calc.text.toString() + btn_RParen.text.toString()
            text_calc.setText(new_text)
        }

        val btn_Clear = findViewById<Button>(R.id.btn_Clear)
        btn_Clear.setOnClickListener {
            text_calc.setText("")
        }

        // verify expression and calculate result when equal is clicked
        val btn_Equal = findViewById<Button>(R.id.btn_Equal)
        btn_Equal.setOnClickListener {
            val expression = text_calc.text.toString()
            if (expression.endsWith("/0")) {
                val toast = Toast.makeText(applicationContext, "Cannot divide by 0", Toast.LENGTH_LONG)
                toast.show()
            } else if (expression == ""){
                val toast = Toast.makeText(applicationContext, "Expression cannot be empty", Toast.LENGTH_LONG)
                toast.show()
            } else if (expression.endsWith("+") || expression.endsWith("-")
                || expression.endsWith("/") || expression.endsWith("*")){
                val toast = Toast.makeText(applicationContext, "Incomplete expression!", Toast.LENGTH_LONG)
                toast.show()
            } else {
                val result = eval(text_calc.text.toString())
                text_info.text = expression.plus("=").plus(result)
                text_calc.setText("")
            }
        }
    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
