package com.cmc.curtaincall.feature.mypage.faq

import androidx.lifecycle.ViewModel
import com.cmc.curtaincall.domain.enums.FAQMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageFAQViewModel @Inject constructor() : ViewModel() {
    private val _menu = MutableStateFlow(FAQMenu.SHOW)
    val menu = _menu.asStateFlow()

    fun selectFAQMenu(menu: FAQMenu) {
        _menu.value = menu
    }
}
