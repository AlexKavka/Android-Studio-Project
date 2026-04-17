package com.ak.androidstudioproject.AppDetailes.Presentation.UI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ak.androidstudioproject.R

@Composable
internal fun Toolbar(
    state: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onWishClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isInWishlist = state
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
                tint = Color.Blue
            )
        }
        Row () {
            IconButton(onClick = onWishClick) {
                Icon(
                    painter = painterResource(id = if (isInWishlist) R.drawable.inwishlist else R.drawable.notinwishlist),
                    contentDescription = null,
                    modifier = Modifier.height(24.dp),
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = Color.Blue
                )
            }
        }
    }
}