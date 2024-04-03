package com.cmc.curtaincall.feature.mypage.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.model.favorite.FavoriteShowModel
import com.cmc.curtaincall.domain.repository.FavoriteRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyPageFavoriteViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _memberID = MutableStateFlow(Int.MIN_VALUE)
    val memberID = _memberID.asStateFlow()

    private val _genreType = MutableStateFlow(ShowGenreType.PLAY)
    val genreType = _genreType.asStateFlow()

    private val _favoriteShows = MutableStateFlow<List<FavoriteShowModel>>(listOf())
    val favoriteShows = _favoriteShows.asStateFlow()

    init {
        checkMemberID()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun checkMemberID() {
        memberRepository.getMemberId()
            .onEach {
                _memberID.value = it
                requestFavoriteShows()
            }
            .launchIn(viewModelScope)
    }

    fun selectGenreType(genreType: ShowGenreType) {
        _genreType.value = genreType
        requestFavoriteShows()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun requestFavoriteShows() {
        favoriteRepository.requestFavoriteShows(memberID.value)
            .onEach { shows ->
                _favoriteShows.value = shows.filter { ShowGenreType.valueOf(it.genre) == genreType.value }
            }
            .filter {
                it.isNotEmpty()
            }
            .flatMapLatest { favoriteShows ->
                favoriteRepository.checkFavoriteShows(
                    favoriteShows.map { it.id }
                )
            }.onEach { checkFavoriteShows ->
                _favoriteShows.value = _favoriteShows.value.map { favoriteShow ->
                    favoriteShow.copy(
                        favorite = checkFavoriteShows.find { favoriteShow.id == it.showId }?.favorite ?: false
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun requestFavoriteShow(showId: String) {
        favoriteRepository.requestFavoriteShow(showId)
            .onEach { check ->
                if (check) {
                    requestFavoriteShows()
                }
            }.launchIn(viewModelScope)
    }

    fun deleteFavoriteShow(showId: String) {
        favoriteRepository.deleteFavoriteShow(showId)
            .onEach { check ->
                if (check) {
                    requestFavoriteShows()
                }
            }.launchIn(viewModelScope)
    }
}
