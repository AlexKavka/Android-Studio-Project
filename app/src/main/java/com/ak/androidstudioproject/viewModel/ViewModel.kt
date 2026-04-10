package com.ak.androidstudioproject.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.androidstudioproject.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PreCardState {
    data object Initial : PreCardState
    data object Loading : PreCardState
    data class Success(
        val preCard: PreCardInfo
    ) : PreCardState
    data class Error(
        val state : Boolean
    ) : PreCardState
}

sealed interface FullCardState {
    data object Initial : FullCardState
    data object Loading : FullCardState
    data class Success(
        val fullCard: FullCardInfo
    ) : FullCardState
    data class Error(
        val state : Boolean
    ) : FullCardState
}

sealed interface ListState {
    data object Initial : ListState
    data object Loading : ListState
    data class Success(
        val list: AppUrls
    ) : ListState
    data class Error(
        val state : Boolean
    ) : ListState
}

class ListViewModel (
    private val rep : AppsRepository
) : ViewModel () {

    private val _listState = MutableStateFlow<ListState>(ListState.Initial)
    val listState : StateFlow<ListState> = _listState.asStateFlow()

    private val _cardsStates = MutableStateFlow<Map<String, PreCardState>>(emptyMap())
    private val cardState: StateFlow<Map<String, PreCardState>> = _cardsStates.asStateFlow()

    init {
        if (_listState.value is ListState.Initial || _listState.value is ListState.Error) {
            loadAppsUrls()
        }
    }

    private fun loadAppsUrls() {
        viewModelScope.launch {

            val currentState = _listState.value
            if (currentState is ListState.Loading || currentState is ListState.Success) {
                return@launch
            }

            _listState.value = ListState.Loading
            runCatching {
                val appUrls = rep.getAppUrls()
                if (appUrls != null && appUrls.urls.isNotEmpty()) {
                    _listState.value = ListState.Success(appUrls)
                }
                else {
                    _listState.value = ListState.Error(true)
                }
            }.onFailure {
                _listState.value = ListState.Error(true)
            }
        }
    }

    fun retry() = loadAppsUrls()
}

class PreCardViewModel(
    private val packageName: String,
    private val rep: AppsRepository
) : ViewModel() {

    private val _preCardState = MutableStateFlow<PreCardState>(PreCardState.Initial)
    val preCardState: StateFlow<PreCardState> = _preCardState.asStateFlow()

    init {
        println("=== PreCardViewModel создан для $packageName ===")
        println("Текущее состояние: ${_preCardState.value}")
        if (_preCardState.value is PreCardState.Initial || _preCardState.value is PreCardState.Error) {
            println("Загружаем данные для $packageName")
            loadPreCard()
        } else {
            println("Данные уже есть, не загружаем")
        }
    }

    private fun loadPreCard() {
        viewModelScope.launch {

            val currentState = _preCardState.value
            if (currentState is PreCardState.Loading || currentState is PreCardState.Success) {
                return@launch
            }

            _preCardState.value = PreCardState.Loading
            runCatching {
                val preCard = rep.getAppPreCard(packageName)
                if (preCard != null) {
                    _preCardState.value = PreCardState.Success(preCard)
                }
                else {
                    _preCardState.value = PreCardState.Error(true)
                }
            }.onFailure {
                _preCardState.value = PreCardState.Error(true)
            }
        }
    }

    fun retry() = loadPreCard()
}

class FullCardViewModel (
    private val packageName: String,
    private val rep : AppsRepository
) : ViewModel () {

    private val _fullCardState = MutableStateFlow<FullCardState>(FullCardState.Initial)
    val fullCardState: StateFlow<FullCardState> = _fullCardState.asStateFlow()

    init {
        if (_fullCardState.value is FullCardState.Initial || _fullCardState.value is FullCardState.Error) {
            loadFullCard()
        }
    }

    private fun loadFullCard() {
        viewModelScope.launch {

            val currentState = _fullCardState.value
            if (currentState is FullCardState.Loading || currentState is FullCardState.Success) {
                return@launch
            }

            _fullCardState.value = FullCardState.Loading
            runCatching {

                val fullCard = rep.getFullAppInfo(packageName)

                if (fullCard != null) {
                    _fullCardState.value = FullCardState.Success(fullCard)
                }
                else {
                    _fullCardState.value = FullCardState.Error(true)
                }
            }.onFailure { e ->
                _fullCardState.value = FullCardState.Error(true)
            }
        }
    }

    fun retry() = loadFullCard()
}