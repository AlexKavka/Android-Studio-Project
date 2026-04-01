package com.ak.androidstudioproject

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ShowText : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_show_text)

        val bundle = intent.extras
        val text = bundle?.getString("TEXT") ?: "Текст не передан"

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = text
    }
}