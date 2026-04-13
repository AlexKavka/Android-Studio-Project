package com.ak.androidstudioproject.AppList.Presentation.UI

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.ak.androidstudioproject.AppList.Presentation.ViewModel.*
import com.ak.androidstudioproject.AppList.Domain.*
import com.ak.androidstudioproject.CommonUtils.getCategoryText
import com.ak.androidstudioproject.R

@Composable
fun preCardLoading () {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(Color.White)
    )
    {
        Box(modifier = Modifier.background(Color.LightGray).size(90.dp).clip(RoundedCornerShape(16.dp)))
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                "...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun preCardSuccess (
    preCardState: PreCardState.Success,
    onRetry : () -> Unit,
    onClick: (String?) -> Unit
) {
    val state = preCardState.preCard

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick(state.url) })
    {
        AsyncImage(
            model = state.iconUrl,
            contentDescription = state.appName,
            modifier = Modifier.size(90.dp).clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column (modifier = Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(state.appName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(state.shortDescription,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                getCategoryText( state.categories),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun preCardError () {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(Color.White)
    )
    {
        Box(modifier = Modifier.background(Color.LightGray).size(90.dp).clip(RoundedCornerShape(16.dp)))
        Spacer(modifier = Modifier.width(10.dp))
        Column (modifier = Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("Ошибка",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AppPreCard (
    packageName : String,
    rep: AppsListRepository,
    onClick: (String?) -> Unit
) {
    val viewModel : PreCardViewModel = viewModel(packageName) {
        PreCardViewModel(packageName, rep)
    }

    val state by viewModel.preCardState.collectAsState()

    when (state) {
        is PreCardState.Initial -> {

        }
        is PreCardState.Loading -> {
            preCardLoading()
        }
        is PreCardState.Success -> {
            preCardSuccess(state as PreCardState.Success, onRetry = { viewModel.retry() }, onClick = onClick)
        }
        is PreCardState.Error -> {
            preCardError()
        }
    }
}

@Composable
fun listLoading () {
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
fun listSucces (
    state: ListState.Success,
    rep: AppsListRepository,
    onRetry : () -> Unit,
    onNavigateToDetail: (String?) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box( modifier = Modifier.fillMaxWidth()
        .background(Color.Blue))
    {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 56.dp, start = 8.dp, end = 8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.rustore_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .height(32.dp)
                    .clickable {

                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Обновить список?",
                                    actionLabel = "Да"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onRetry()
                                }
                            }

                        }

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.someicon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.height(32.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 110.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(5.dp)

        ) {
            items(state.list.urls.size) { index ->
                AppPreCard(
                    packageName = state.list.urls[index],
                    rep = rep,
                    onClick = { appName ->
                        onNavigateToDetail(appName ?: "")
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
fun listError() {

    Box( modifier = Modifier.fillMaxWidth()
        .background(Color.Blue))
    {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 56.dp, start = 8.dp, end = 8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.rustore_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.height(32.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.someicon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.height(32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 110.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(5.dp)

        ) {
            val context = LocalContext.current
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun ListOfApps(
    rep: AppsListRepository,
    onNavigateToDetail: (String?) -> Unit
) {
    val viewModel : ListViewModel = viewModel {
        ListViewModel(rep)
    }

    val state by viewModel.listState.collectAsState()

    when (state) {
        is ListState.Initial -> {

        }
        is ListState.Loading -> {
            listLoading()
        }
        is ListState.Success -> {
            listSucces(state as ListState.Success, rep, onRetry = { viewModel.retry() }, onNavigateToDetail = onNavigateToDetail)
        }
        is ListState.Error -> {
            listError()
        }
    }
}

@Composable
inline fun <reified VM : ViewModel> viewModel(
    key: String? = null,
    crossinline factory: () -> VM
): VM {
    val factoryWrapper = remember(key) {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return factory() as T
            }
        }
    }
    return viewModel(key = key, factory = factoryWrapper)
}