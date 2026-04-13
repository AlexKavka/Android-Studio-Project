package com.ak.androidstudioproject.AppDetailes.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

class FullCardViewModel (
    private val packageName: String,
    private val rep : AppDetailsRepository
) : ViewModel () {

    private val _fullCardState = MutableStateFlow<FullCardState>(FullCardState.Initial)
    val fullCardState: StateFlow<FullCardState> = _fullCardState.asStateFlow()

    init {
        if (_fullCardState.value is FullCardState.Initial || _fullCardState.value is FullCardState.Error) {
            loadFullCard()
        }
    }

    private fun loadFullCard() {
        viewModelScope.launch(Dispatchers.IO) {

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