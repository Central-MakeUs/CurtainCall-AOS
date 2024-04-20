package com.cmc.curtaincall.feature.home

import androidx.lifecycle.viewModelScope
import com.cmc.curtaincall.common.utility.extensions.convertDDay
import com.cmc.curtaincall.core.base.BaseViewModel
import com.cmc.curtaincall.domain.enums.ShowGenreType
import com.cmc.curtaincall.domain.repository.ChattingRepository
import com.cmc.curtaincall.domain.repository.LaunchRepository
import com.cmc.curtaincall.domain.repository.MemberRepository
import com.cmc.curtaincall.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val showRepository: ShowRepository,
    private val chattingRepository: ChattingRepository,
    private val launchRepository: LaunchRepository

) : BaseViewModel<HomeState, HomeEvent, Nothing>(
    initialState = HomeState()
) {
    private val memberID = MutableStateFlow(Int.MIN_VALUE)
    private val _user = MutableStateFlow(User())
    private val _token = MutableStateFlow("")

    init {
        checkMemberId()
        checkCostEffectiveTooltip()
        getMemberNickname()
    }

    override fun reduceState(currentState: HomeState, event: HomeEvent): HomeState =
        when (event) {
            HomeEvent.ShowTooltip -> {
                currentState.copy(isShowTooltip = true)
            }

            HomeEvent.HideTooltip -> {
                currentState.copy(isShowTooltip = false)
            }

            is HomeEvent.GetNickname -> {
                currentState.copy(nickname = event.nickname)
            }

            is HomeEvent.RequestMyRecruitment -> {
                currentState.copy(myRecruitments = event.myRecruitments)
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

            is HomeEvent.GetMemberInfo -> {
                currentState.copy(memberInfo = event.memberInfoModel)
            }

            else -> currentState
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun checkMemberId() {
        memberRepository.getMemberId()
            .onEach {
                memberID.value = it
            }.flatMapLatest {
                memberRepository.requestMemberInfo(it)
            }.onEach {
                sendAction(HomeEvent.GetMemberInfo(it))
            }.launchIn(viewModelScope)
    }

    private fun checkCostEffectiveTooltip() {
        launchRepository.isShowHomeTooltip()
            .onEach { isShow ->
                sendAction(
                    if (isShow) {
                        HomeEvent.ShowTooltip
                    } else {
                        HomeEvent.HideTooltip
                    }
                )
            }.launchIn(viewModelScope)
    }

    fun hideCostEffectiveTooltip() {
        viewModelScope.launch {
            launchRepository.stopShowHomeTooltip()
            sendAction(HomeEvent.HideTooltip)
        }
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
        memberRepository.requestMyRecruitments(
            memberId = memberID.value,
            page = 0,
            size = 5
        ).onEach {
            sendAction(HomeEvent.RequestMyRecruitment(it))
        }.launchIn(viewModelScope)
    }

    fun requestPopularShowList() {
        val type = "DAY"
        val baseDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2))
        showRepository.requestPopularShowList(
            type = type,
            genre = ShowGenreType.PLAY.name,
            baseDate = baseDate
        ).zip(
            showRepository.requestPopularShowList(
                type = type,
                genre = ShowGenreType.MUSICAL.name,
                baseDate = baseDate
            )
        ) { popular1, popular2 ->
            (popular1 + popular2).sortedBy { it.rank }.take(10)
        }.onEach {
            sendAction(
                HomeEvent.RequestPopularShowList(
                    showRanks = it
                )
            )
        }.launchIn(viewModelScope)
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
