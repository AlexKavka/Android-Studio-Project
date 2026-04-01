package com.ak.androidstudioproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun String.textIsPhone(): Boolean {
    val cleaned = this.replace(Regex("[^\\d+]"), "")

    return cleaned.length == 11
}

class TextEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_edit)

        val editText = findViewById<EditText>(R.id.editTextText)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)

        button.setOnClickListener {
            val text = editText.text.toString()

            val bundle = Bundle()
            bundle.putString("TEXT", text)

            val intent = Intent(this, ShowText::class.java).apply {
                putExtras(bundle)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            startActivity(intent)
        }
        button2.setOnClickListener {
            val text = editText.text.toString()

            if (text.textIsPhone()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$text")
                }

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Нет приложения для звонков", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show()
            }
        }
        button3.setOnClickListener {
            val text = editText.text.toString()

            if (text.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                }

                val chooserIntent = Intent.createChooser(
                    intent,
                    "Поделиться через..."
                )

                try {
                    startActivity(chooserIntent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Нет приложения для отправки", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Введите текст для отправки", Toast.LENGTH_SHORT).show()
            }
        }
    }
}