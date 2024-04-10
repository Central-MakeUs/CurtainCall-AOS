package com.cmc.curtaincall.feature.home

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.common.utility.extensions.convertDDay
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.repository.ChattingRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val showRepository: ShowRepository,
    private val chattingRepository: ChattingRepository,
) : BaseViewModel<HomeState, HomeEvent, Nothing>(
    initialState = HomeState()
) {
    private val _user = MutableStateFlow(User())
    private val _token = MutableStateFlow("")

    init {
        getMemberNickname()
    }

    override fun reduceState(currentState: HomeState, event: HomeEvent): HomeState =
        when (event) {
            is HomeEvent.GetNickname -> {
                currentState.copy(nickname = event.nickname)
            }

            is HomeEvent.RequestMyRecruitment -> {
                currentState.copy(myRecruitments = event.myRecruitments)
            }

            is HomeEvent.RequestMyParticipations -> {
                currentState.copy(myParticipations = event.myParticipations)
            }

            is HomeEvent.RequestPopularShowList -> {
                currentState.copy(showRanks = event.showRanks)
            }

            is HomeEvent.RequestOpenShowList -> {
                currentState.copy(openShowInfos = event.openShowInfos)
            }

            is HomeEvent.RequestEndShowList -> {
                currentState.copy(endShowInfos = event.endShowInfos)
            }

            is HomeEvent.RequestShowSearchWords -> {
                currentState.copy(searchWords = event.searchWords)
            }

            is HomeEvent.RequestLiveTalk -> {
                currentState.copy(liveTalks = event.liveTalks)
            }

            is HomeEvent.RequestShowRecommendations -> {
                currentState.copy(showRecommendations = event.showRecommendations)
            }

            is HomeEvent.RequestCostEffectiveShows -> {
                currentState.copy(costEffectiveShows = event.costEffectiveShows)
            }

            else -> currentState
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getMemberNickname() {
        memberRepository.getMemberId()
            .flatMapLatest {
                memberRepository.requestMemberInfo(it)
            }.onEach {
                sendAction(HomeEvent.GetNickname(it.nickname))
            }.launchIn(viewModelScope)
    }

    fun requestLiveTalks() {
        showRepository.requestLiveTalkShowList(null, null, SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(Calendar.getInstance().time))
            .onEach { sendAction(HomeEvent.RequestLiveTalk(it)) }
            .launchIn(viewModelScope)
    }

    fun requestMyRecruitments() {
        viewModelScope.launch {
            val memberId = memberRepository.getMemberId().first()
            memberRepository.requestMyRecruitments(
                memberId = memberId,
                page = 0,
                size = 2,
                category = null
            ).onEach { recruitments ->
                sendAction(
                    HomeEvent.RequestMyRecruitment(
                        recruitments
                            .sortedByDescending { it.createdAt }
                            .take(2)
                    )
                )
            }.launchIn(this)
        }
    }

    fun requestMyParticipations() {
        viewModelScope.launch {
            val memberId = memberRepository.getMemberId().first()
            memberRepository.requestMyParticipations(
                memberId = memberId,
                page = 0,
                size = 2,
                category = null
            ).onEach { participations ->
                sendAction(
                    HomeEvent.RequestMyParticipations(
                        participations
                            .sortedByDescending { it.createdAt }
                            .take(2)
                    )
                )
            }.launchIn(this)
        }
    }

    fun requestPopularShowList() {
        val today = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        showRepository.requestPopularShowList(type = "WEEK", genre = "ALL", baseDate = today)
            .onEach {
                sendAction(HomeEvent.RequestPopularShowList(it.sortedBy { it.rank }.take(10)))
            }
            .launchIn(viewModelScope)
    }

    fun requestOpenShowList() {
        val today = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        showRepository.requestOpenShowList(page = 0, size = 10, startDate = today)
            .onEach {
                sendAction(HomeEvent.RequestOpenShowList(it.shuffled().take(10).sortedByDescending { it.startDate.convertDDay() }))
            }
            .launchIn(viewModelScope)
    }

    fun requestEndShowList() {
        val today = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        showRepository.requestEndShowList(page = 0, size = 10, endDate = today, genre = null)
            .onEach {
                sendAction(HomeEvent.RequestEndShowList(it.shuffled().take(10).sortedByDescending { it.endDate.convertDDay() }))
            }
            .launchIn(viewModelScope)
    }

    fun requestCostEffectiveShows() {
        showRepository.requestCostEffectiveShows(genre = ShowGenreType.PLAY.name)
            .zip(showRepository.requestCostEffectiveShows(genre = ShowGenreType.MUSICAL.name)) { plays, musicals ->
                (plays + musicals)
                    .sortedWith(compareBy({ it.minTicketPrice }, { it.endDate }))
                    .take(10)
            }.onEach {
                sendAction(
                    HomeEvent.RequestCostEffectiveShows(
                        costEffectiveShows = it
                    )
                )
            }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun connectChattingClient(chatClient: ChatClient) {
        memberRepository.getMemberId().flatMapLatest {
            memberRepository.requestMemberInfo(it)
        }.onEach {
            _user.value = User(
                id = it.id.toString(),
                name = it.nickname,
                image = it.imageUrl.toString()
            )
        }.flatMapLatest {
            chattingRepository.requestChattingToken()
        }.onEach {
            _token.value = it.value
            chatClient.connectUser(
                user = _user.value,
                token = it.value
            ).enqueue {
                Timber.d("chatClient connect User ${it.isSuccess}")
            }
        }.launchIn(viewModelScope)
    }

    fun requestShowRecommendations() {
        showRepository.requestShowRecommendation()
            .onEach { sendAction(HomeEvent.RequestShowRecommendations(it)) }
            .launchIn(viewModelScope)
    }
}
