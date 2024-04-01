package com.cmc.curtaincall.feature.mypage.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc.curtaincall.domain.model.member.MemberReviewModel
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyPageWritingViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val showRepository: ShowRepository
) : ViewModel() {

    val myReviewModels: Flow<PagingData<MemberReviewModel>> = memberRepository
        .fetchMyReview()
        .cachedIn(viewModelScope)
}
