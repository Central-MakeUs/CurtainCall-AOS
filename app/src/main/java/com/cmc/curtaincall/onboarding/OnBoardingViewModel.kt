package com.cmc.curtaincall.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.repository.LaunchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val launchRepository: LaunchRepository
) : ViewModel() {
    fun setIsFirstEntryOnBoarding() {
        viewModelScope.launch {
            launchRepository.setIsFirstEntryOnBoarding()
        }
    }
}
