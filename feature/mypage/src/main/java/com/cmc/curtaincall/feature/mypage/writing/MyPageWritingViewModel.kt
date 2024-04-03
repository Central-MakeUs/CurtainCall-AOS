package com.cmc.curtaincall.feature.mypage.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.cmc.curtaincall.domain.model.member.MemberReviewModel
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyPageWritingViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val showRepository: ShowRepository
) : ViewModel() {

    private val _myReviewModels = MutableStateFlow<PagingData<MemberReviewModel>>(PagingData.empty())
    val myReviewModels = _myReviewModels.asStateFlow()

    fun fetchMyReviews() {
        memberRepository.fetchMyReview()
            .onEach { _myReviewModels.value = it }
            .launchIn(viewModelScope)
    }
}
