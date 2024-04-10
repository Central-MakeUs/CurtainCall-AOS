package com.cmc.curtaincall.data.source.remote

import com.cmc.curtaincall.core.network.service.show.ShowService
import com.cmc.curtaincall.core.network.service.show.response.CostEffectiveShowResponse
import com.cmc.curtaincall.core.network.service.show.response.FacilityDetailResponse
import com.cmc.curtaincall.core.network.service.show.response.LiveTalkShowResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowDetailResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowInfoResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowRankResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowRecommendationResponse
import com.cmc.curtaincall.core.network.service.show.response.SimilarShowInfoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShowRemoteSource @Inject constructor(
    private val showService: ShowService
) {
    fun requestShowList(
        page: Int,
        size: Int,
        genre: String,
        sort: String?
    ): Flow<List<ShowInfoResponse>> = flow {
        emit(
            showService.requestShowList(
                page = page,
                size = size,
                genre = genre,
                sort = sort
            ).showInfos
        )
    }

    fun searchShowList(
        page: Int,
        size: Int,
        keyword: String
    ): Flow<List<ShowInfoResponse>> = flow {
        emit(
            showService.searchShowList(
                page = page,
                size = size,
                keyword = keyword
            ).showInfos
        )
    }

    fun requestOpenShowList(
        page: Int,
        size: Int,
        startDate: String
    ): Flow<List<ShowInfoResponse>> = flow {
        emit(
            showService.requestOpenShowList(
                page = page,
                size = size,
                startDate = startDate
            ).showInfos
        )
    }

    fun requestEndShowList(
        page: Int,
        size: Int,
        endDate: String,
        genre: String?
    ): Flow<List<ShowInfoResponse>> = flow {
        emit(
            showService.requestEndShowList(
                page = page,
                size = size,
                endDate = endDate,
                genre = genre
            ).showInfos
        )
    }

    fun requestShowDetail(
        showId: String
    ): Flow<ShowDetailResponse> = flow {
        emit(
            showService.requestShowDetail(showId)
        )
    }

    fun requestPopularShowList(
        type: String,
        genre: String?,
        baseDate: String
    ): Flow<List<ShowRankResponse>> = flow {
        emit(
            showService.requestPopularShowList(
                type = type,
                genre = genre,
                baseDate = baseDate
            ).showRanks
        )
    }

    fun requestFacilityDetail(
        facilityId: String
    ): Flow<FacilityDetailResponse> = flow {
        emit(
            showService.requestFacilityDetail(facilityId)
        )
    }

    fun requestSimilarShowList(
        facilityId: String,
        page: Int,
        size: Int?,
        genre: String?
    ): Flow<List<SimilarShowInfoResponse>> = flow {
        emit(
            showService.requestSimilarShowList(
                facilityId = facilityId,
                page = page,
                size = size,
                genre = genre
            ).similarShowInfos
        )
    }

    fun requestLiveTalkShowList(
        page: Int?,
        size: Int?,
        baseDateTime: String
    ): Flow<List<LiveTalkShowResponse>> = flow {
        emit(
            showService.requestLiveTalkShowList(
                page = page,
                size = size,
                baseDateTime = baseDateTime
            ).liveTalks
        )
    }

    fun requestCostEffectiveShows(genre: String): Flow<List<CostEffectiveShowResponse>> = flow {
        emit(
            showService.requestCostEffectiveShows(
                genre = genre
            ).shows
        )
    }

    fun requestShowRecommendations(): Flow<List<ShowRecommendationResponse>> = flow {
        emit(showService.requestShowRecommendations().showRecommendations)
    }
}
