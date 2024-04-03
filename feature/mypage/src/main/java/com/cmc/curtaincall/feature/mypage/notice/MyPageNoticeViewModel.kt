package com.cmc.curtaincall.feature.mypage.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc.curtaincall.domain.model.notice.NoticeDetailModel
import com.cmc.curtaincall.domain.model.notice.NoticeModel
import com.cmc.curtaincall.domain.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyPageNoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    val notices: Flow<PagingData<NoticeModel>> = noticeRepository
        .requestNoticeList()
        .cachedIn(viewModelScope)

    private var _noticeDetail = MutableStateFlow(NoticeDetailModel())
    val noticeDetail = _noticeDetail.asStateFlow()

    fun requestNoticeDetail(noticeId: Int) {
        noticeRepository.requestNoticeDetail(noticeId)
            .onEach { _noticeDetail.value = it }
            .launchIn(viewModelScope)
    }
}
