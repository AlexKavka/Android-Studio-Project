package com.ak.androidstudioproject.AppDetailes.Presentation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class FullCardViewModel @Inject constructor(
    private val rep : AppDetailsRepository
) : ViewModel () {

    private val _fullCardState = MutableStateFlow<FullCardState>(FullCardState.Initial)
    val fullCardState: StateFlow<FullCardState> = _fullCardState.asStateFlow()
    private var currentPackageName: String = ""
    private var observeJob: Job? = null

    fun init(packageName: String) {
        if (currentPackageName == packageName &&
            (_fullCardState.value is FullCardState.Loading || _fullCardState.value is FullCardState.Success)) {
            return
        }

        currentPackageName = packageName
        observeJob?.cancel()
        loadFullCard()
        observeAppDetails()
    }

    private fun forceLoadFullCard() {
        if (currentPackageName.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            _fullCardState.value = FullCardState.Loading
            runCatching {
                val fullCard = rep.getFullAppInfo(currentPackageName)
                if (fullCard != null) {
                    _fullCardState.value = FullCardState.Success(fullCard)
                } else {
                    _fullCardState.value = FullCardState.Error(true)
                }
            }.onFailure {
                _fullCardState.value = FullCardState.Error(true)
            }
        }
    }

    private fun loadFullCard() {
        if (currentPackageName.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {

            val currentState = _fullCardState.value
            if (currentState is FullCardState.Loading || currentState is FullCardState.Success) {
                return@launch
            }

            _fullCardState.value = FullCardState.Loading
            runCatching {

                val fullCard = rep.getFullAppInfo(currentPackageName)

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

    private fun observeAppDetails() {
        if (currentPackageName.isEmpty()) return

        observeJob = viewModelScope.launch {
            rep.observeAppDetails(currentPackageName)
                .onStart {
                    if (_fullCardState.value is FullCardState.Initial) {
                        _fullCardState.value = FullCardState.Loading
                    }
                }
                .catch { error ->
                    Log.e("FullCardVM", "Error in observeAppDetails", error)
                    _fullCardState.value = FullCardState.Error(true)
                }
                .collect { fullCard ->

                    if (fullCard.url.isNotEmpty() && fullCard.appName.isNotEmpty()) {
                        _fullCardState.value = FullCardState.Success(fullCard)
                    } else if (_fullCardState.value is FullCardState.Loading) {}
                }
        }
    }

    fun toggleWishlist() {
        viewModelScope.launch(Dispatchers.IO) {
            rep.toggleWishlist(currentPackageName)
        }
    }

    fun retry() {
        forceLoadFullCard()
    }
}