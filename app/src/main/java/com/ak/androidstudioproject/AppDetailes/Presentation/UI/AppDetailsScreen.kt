package com.ak.androidstudioproject.AppDetailes.Presentation.UI

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ak.androidstudioproject.AppDetailes.Domain.*
import com.ak.androidstudioproject.AppDetailes.Presentation.ViewModel.*

@Composable
fun fullCardLoading() {
    Box( modifier = Modifier
        .fillMaxWidth()
        .background(Color.Blue),
        contentAlignment = Alignment.Center
    )
    {
        CircularProgressIndicator()
    }
}

@Composable
fun fullCardSucces(
    state: FullCardState.Success,
    modifier: Modifier,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onDeveloperClick: (String) -> Unit
) {
    val app = state.fullCard
    val context = LocalContext.current
    val underDevelopmentText = "R.string.under_developement" //stringResource(R.string.under_developement)
    var descriptionCollapsed by remember { mutableStateOf(false) }

    Column(modifier) {
        Toolbar(
            onBackClick = onBackClick,
            onShareClick = { onShareClick(state.fullCard.url) }
        )

        Spacer(Modifier.height(8.dp))

        AppDetailsHeader(
            app = app,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        InstallButton(
            onClick = {
                Toast.makeText(context, underDevelopmentText, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        ScreenshotsList(
            screenshotUrlList = app.screenshots,
            contentPadding = PaddingValues(horizontal = 16.dp),
        )

        Spacer(Modifier.height(12.dp))

        AppDescription(
            description = app.shortDescription,
            collapsed = descriptionCollapsed,
            onReadMoreClick = {
                descriptionCollapsed = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(12.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        Spacer(Modifier.height(12.dp))

        Developer(
            name = app.developer,
            onClick = { onDeveloperClick(app.developer) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        )
    }
}

@Composable
fun fullCardError(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Toolbar(
            onBackClick = onBackClick,
            onShareClick = { onShareClick("А куда?") }
        )

        Spacer(Modifier.height(80.dp))

        Text(
            text = "Ошибка загрузки",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Не удалось загрузить информацию о приложении",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Попробовать снова")
        }
    }

    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
}

@Composable
fun AppDetailsScreen(
    packageName : String,
    modifier: Modifier = Modifier,
    onBackClick : () -> Unit = {},
    onShareClick : (String) -> Unit = {},
    onDeveloperClick : (String) -> Unit = {}
) {
    val viewModel : FullCardViewModel = hiltViewModel()

    val state by viewModel.fullCardState.collectAsState()

    LaunchedEffect(packageName) {
        viewModel.init(packageName)
    }

    when (state) {
        is FullCardState.Initial -> {

        }

        is FullCardState.Loading -> {
            fullCardLoading()
        }

        is FullCardState.Success -> {
            fullCardSucces(state as FullCardState.Success, modifier, onBackClick, onShareClick, onDeveloperClick)
        }

        is FullCardState.Error -> {
            fullCardError(modifier, onBackClick, onShareClick, onRetry = { viewModel.retry() })

        }
    }
}