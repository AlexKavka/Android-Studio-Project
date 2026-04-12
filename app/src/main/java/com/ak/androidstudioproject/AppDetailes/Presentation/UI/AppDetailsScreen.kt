package com.ak.androidstudioproject.AppDetailes.Presentation.UI

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ak.androidstudioproject.model.AppsRepository
import com.ak.androidstudioproject.viewModel.FullCardState
import com.ak.androidstudioproject.viewModel.FullCardViewModel

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
        // Верхняя панель (тулбар)
        Toolbar(
            onBackClick = onBackClick,
            onShareClick = { onShareClick(state.fullCard.url) }
        )

        Spacer(Modifier.height(8.dp))

        // Шапка с иконкой, названием, рейтингом
        AppDetailsHeader(
            app = app,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        // Кнопка установки
        InstallButton(
            onClick = {
                Toast.makeText(context, underDevelopmentText, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        // Скриншоты
        ScreenshotsList(
            screenshotUrlList = app.screenshots,
            contentPadding = PaddingValues(horizontal = 16.dp),
        )

        Spacer(Modifier.height(12.dp))

        // Описание
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

        // Разделитель
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        Spacer(Modifier.height(12.dp))

        // Информация о разработчике
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
fun fullCardError() {
    val context = LocalContext.current
    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
}

@Composable
fun AppDetailsScreen(
    packageName : String,
    rep : AppsRepository,
    modifier: Modifier = Modifier,
    onBackClick : () -> Unit = {},
    onShareClick : (String) -> Unit = {},
    onDeveloperClick : (String) -> Unit = {}
) {
    val viewModel = remember(packageName) {
        FullCardViewModel(packageName, rep)
    }

    val state by viewModel.fullCardState.collectAsState()

    val context = LocalContext.current
    val underDevelopmentText =
        "R.string.under_developement" //stringResource(R.string.under_developement)


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
            //может обавить отладку сюда
            fullCardError()

        }
    }
}