package com.ak.androidstudioproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ak.androidstudioproject.ui.theme.AndroidStudioProjectTheme
import com.ak.androidstudioproject.TextEditActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        android.util.Log.d("MyApp", "MainActivity onCreate started")
        val intent = Intent(this, TextEditActivity::class.java)
        android.util.Log.d("MyApp", "TextEditActivity started")
        startActivity(intent)
        finish()
    }
}