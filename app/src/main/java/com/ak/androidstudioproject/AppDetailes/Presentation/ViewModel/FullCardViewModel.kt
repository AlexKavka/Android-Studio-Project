package com.ak.androidstudioproject.AppDetailes.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun init(packageName: String) {
        if (currentPackageName == packageName &&
            (_fullCardState.value is FullCardState.Loading || _fullCardState.value is FullCardState.Success)) {
            return
        }

        currentPackageName = packageName
        loadFullCard()
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

    fun retry() {
        forceLoadFullCard()
    }
}