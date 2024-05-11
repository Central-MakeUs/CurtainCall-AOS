package com.cmc.curtaincall.feature.mypage.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.model.member.MemberInfoModel
import com.cmc.curtaincall.domain.repository.ImageRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyPageProfileViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private var _memberID = MutableStateFlow(Int.MIN_VALUE)
    val memberID = _memberID.asStateFlow()

    private var _memberInfoModel = MutableStateFlow(MemberInfoModel())
    val memberInfoModel = _memberInfoModel.asStateFlow()

    private val _validationCheckState = MutableStateFlow(ValidationCheckState.None)
    val validationCheckState = _validationCheckState.asStateFlow()

    private val _updateEffect = MutableSharedFlow<Boolean>()
    val updateEffect = _updateEffect.asSharedFlow()

    private val _isDefaultProfile = MutableStateFlow(false)
    val isDefaultProfile = _isDefaultProfile.asStateFlow()

    init {
        checkMemberID()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun checkMemberID() {
        memberRepository.getMemberId()
            .onEach { _memberID.value = it }
            .flatMapLatest { memberRepository.requestMemberInfo(it) }
            .onEach { _memberInfoModel.value = it }
            .launchIn(viewModelScope)
    }

    fun requestMemberInfo() {
        memberRepository.requestMemberInfo(memberID.value)
            .onEach { _memberInfoModel.value = it }
            .launchIn(viewModelScope)
    }

    fun clearValidationState() {
        _validationCheckState.value = ValidationCheckState.None
    }

    fun checkDuplicateNickname(nickname: String) {
        memberRepository.checkDuplicateNickname(nickname)
            .onEach { isDuplicate ->
                _validationCheckState.value = if (isDuplicate) {
                    ValidationCheckState.Duplicate
                } else {
                    ValidationCheckState.Validate
                }
            }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun updateMember(
        context: Context,
        profileUri: Uri?,
        nickname: String
    ) {
        if (profileUri == null) {
            memberRepository.updateMember(
                nickname = nickname,
                imageId = null
            )
                .onEach { _updateEffect.emit(it) }
                .launchIn(viewModelScope)
        } else {
            context.contentResolver.openInputStream(profileUri)?.let { inputStream ->
                imageRepository.uploadGalleryImage(inputStream)
                    .flatMapLatest {
                        memberRepository.updateMember(
                            nickname = nickname,
                            imageId = it.id
                        )
                    }.onEach { _updateEffect.emit(it) }
                    .launchIn(viewModelScope)
            }
        }
    }

    fun changeDefaultProfile(isDefault: Boolean) {
        _isDefaultProfile.value = isDefault
    }
}
