package com.ak.androidstudioproject.AppDetailes.Presentation.UI

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Developer(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = "Разработчик", fontWeight = FontWeight.Bold) //stringResource(R.string.app_details_developer))
        }
        IconButton(
            onClick = {
                Toast.makeText(context, "В разработке", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
            )
        }
    }
}