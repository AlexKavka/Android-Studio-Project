package com.ak.androidstudioproject.AppList.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.androidstudioproject.AppList.Domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

sealed interface ListState {
    data object Initial : ListState
    data object Loading : ListState
    data class Success(
        val list: AppList
    ) : ListState
    data class Error(
        val state : Boolean
    ) : ListState
}

@HiltViewModel
class ListViewModel @Inject constructor(
    private val rep : AppsListRepository
) : ViewModel () {

    private val _listState = MutableStateFlow<ListState>(ListState.Initial)
    val listState : StateFlow<ListState> = _listState.asStateFlow()

    private val _refreshTrigger = MutableSharedFlow<Unit>()
    val refreshTrigger: SharedFlow<Unit> = _refreshTrigger.asSharedFlow()

    private val _cardsStates = MutableStateFlow<Map<String, PreCardState>>(emptyMap())
    private val cardState: StateFlow<Map<String, PreCardState>> = _cardsStates.asStateFlow()

    private fun loadAppsUrls() {
        viewModelScope.launch(Dispatchers.IO) {

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

    init {
        viewModelScope.launch {
            delay(500L)
            if (_listState.value is ListState.Initial || _listState.value is ListState.Error) {
                loadAppsUrls()
            }
        }
    }

    fun retry() {
        viewModelScope.launch {
            _refreshTrigger.emit(Unit)
            loadAppsUrls()
        }
    }
}

@HiltViewModel
class PreCardViewModel @Inject constructor(
    private val rep: AppsListRepository
) : ViewModel() {

    private val _preCardState = MutableStateFlow<PreCardState>(PreCardState.Initial)
    val preCardState: StateFlow<PreCardState> = _preCardState.asStateFlow()
    private var currentPackageName: String = ""

    fun observeRefreshTrigger(refreshTrigger: Flow<Unit>) {
        viewModelScope.launch {
            refreshTrigger.collect {
                if (currentPackageName.isNotEmpty()) {
                    forceLoadPreCard()
                }
            }
        }
    }

    fun init(packageName: String) {
        if (currentPackageName == packageName &&
            (_preCardState.value is PreCardState.Loading ||
                    _preCardState.value is PreCardState.Success)) {
            return
        }

        currentPackageName = packageName
        loadPreCard()
    }

    private fun forceLoadPreCard() {
        viewModelScope.launch(Dispatchers.IO) {
            _preCardState.value = PreCardState.Loading
            runCatching {
                val preCard = rep.getAppPreCard(currentPackageName)
                if (preCard != null) {
                    _preCardState.value = PreCardState.Success(preCard)
                } else {
                    _preCardState.value = PreCardState.Error(true)
                }
            }.onFailure {
                _preCardState.value = PreCardState.Error(true)
            }
        }
    }

    private fun loadPreCard() {
        viewModelScope.launch(Dispatchers.IO) {

            val currentState = _preCardState.value
            if (currentState is PreCardState.Loading || currentState is PreCardState.Success) {
                return@launch
            }

            _preCardState.value = PreCardState.Loading
            runCatching {
                val preCard = rep.getAppPreCard(currentPackageName)
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