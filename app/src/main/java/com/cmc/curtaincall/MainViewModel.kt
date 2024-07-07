package com.cmc.curtaincall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.repository.LaunchRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val launchRepository: LaunchRepository
) : ViewModel() {

    fun fetchRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val serverUrl = remoteConfig.getString("serverUrl")
                    viewModelScope.launch {
                        launchRepository.setServerUrl(serverUrl)
                    }
                    Timber.d("Firebase Remote Config fetch and Active success serverUrl: $serverUrl")
                } else {
                    Timber.d("Firebase Remote Config fetch and Active fail")
                }
            }
    }
}
