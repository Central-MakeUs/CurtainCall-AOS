package com.cmc.curtaincall.feature.mypage.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.model.auth.LoginResultModel
import com.cmc.curtaincall.domain.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageSettingViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private var _logoutComplete = MutableStateFlow(false)
    val logoutComplete = _logoutComplete.asStateFlow()

    fun memberLogout() {
        viewModelScope.launch {
            tokenRepository.saveToken(LoginResultModel())
            _logoutComplete.value = true
        }
    }
}
