package com.cmc.curtaincall.feature.show.review

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.enums.ReviewSortType
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ShowReviewViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val reviewRepository: ReviewRepository
) : BaseViewModel<ShowReviewUiState, ShowReviewEvent, ShowReviewSideEffect>(
    initialState = ShowReviewUiState()
) {
    init {
        getMemberId()
    }

    override fun reduceState(currentState: ShowReviewUiState, event: ShowReviewEvent): ShowReviewUiState =
        when (event) {
            is ShowReviewEvent.GetMemberID -> {
                currentState.copy(memberId = event.memberId)
            }

            is ShowReviewEvent.FetchShowReviewList -> {
                currentState.copy(showReviewModels = event.showReviewModels)
            }

            is ShowReviewEvent.CheckMyReview -> {
                currentState.copy(hasMyReview = event.hasMyReview)
            }

            is ShowReviewEvent.SelectSortType -> {
                currentState.copy(sortType = event.sortType)
            }
        }

    private fun getMemberId() {
        memberRepository.getMemberId()
            .onEach { sendAction(ShowReviewEvent.GetMemberID(it)) }
            .launchIn(viewModelScope)
    }

    fun fetchShowReviewList(showId: String) {
        sendAction(
            ShowReviewEvent.FetchShowReviewList(
                showReviewModels = reviewRepository.fetchShowReviewList(showId, uiState.value.sortType)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
            )
        )
    }

    fun selectLikeReview(
        reviewId: Int,
        isFavorite: Boolean
    ) {
        if (isFavorite) {
            reviewRepository.requestLikeReview(reviewId)
                .onEach { sendSideEffect(ShowReviewSideEffect.RefreshShowReview) }
                .launchIn(viewModelScope)
        } else {
            reviewRepository.requestDislikeReview(reviewId)
                .onEach { sendSideEffect(ShowReviewSideEffect.RefreshShowReview) }
                .launchIn(viewModelScope)
        }
    }

    fun createShowReview(
        showId: String,
        grade: Int,
        content: String
    ) {
        reviewRepository.createShowReview(showId, grade, content)
            .onEach { sendSideEffect(ShowReviewSideEffect.CreateMyReview) }
            .launchIn(viewModelScope)
    }

    fun checkMyReview(showId: String) {
        reviewRepository.checkCreatedReview(showId)
            .catch { sendAction(ShowReviewEvent.CheckMyReview(false)) }
            .onEach { sendAction(ShowReviewEvent.CheckMyReview(true)) }
            .launchIn(viewModelScope)
    }

    fun deleteShowReview(
        showId: String,
        reviewId: Int
    ) {
        reviewRepository.deleteShowReview(reviewId)
            .onEach { sendSideEffect(ShowReviewSideEffect.DeleteMyReview) }
            .onCompletion { checkMyReview(showId) }
            .launchIn(viewModelScope)
    }

    fun updateShowReview(
        reviewId: Int,
        content: String,
        grade: Int
    ) {
        reviewRepository.updateShowReview(reviewId, content, grade)
            .onEach { sendSideEffect(ShowReviewSideEffect.RefreshShowReview) }
            .launchIn(viewModelScope)
    }

    fun selectSortType(
        showId: String,
        sortType: ReviewSortType
    ) {
        sendAction(ShowReviewEvent.SelectSortType(sortType = sortType))
        fetchShowReviewList(showId)
    }
}
