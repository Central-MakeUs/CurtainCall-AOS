package com.cmc.curtaincall.feature.partymember.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PartyMemberEditViewModel @Inject constructor(
    private val partyRepository: PartyRepository
) : ViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _isSuccessUpdate = MutableSharedFlow<Boolean>()
    val isSuccessUpdate = _isSuccessUpdate.asSharedFlow()

    fun changeTitle(title: String) {
        _title.value = title
    }

    fun changeContent(content: String) {
        _content.value = content
    }

    fun editParty(partyId: Int) {
        partyRepository.updateParty(
            partyId = partyId,
            title = title.value,
            content = content.value
        ).onEach { check ->
            _isSuccessUpdate.emit(check)
        }.launchIn(viewModelScope)
    }
}
