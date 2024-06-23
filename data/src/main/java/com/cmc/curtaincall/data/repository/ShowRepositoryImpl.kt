package com.cmc.curtaincall.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc.curtaincall.core.network.service.favorite.FavoriteService
import com.cmc.curtaincall.core.network.service.show.ShowService
import com.cmc.curtaincall.data.source.local.ShowLocalSource
import com.cmc.curtaincall.data.source.paging.SHOW_PAGE_SIZE
import com.cmc.curtaincall.data.source.paging.SHOW_SEARCH_PAGE_SIZE
import com.cmc.curtaincall.data.source.paging.ShowPagingSource
import com.cmc.curtaincall.data.source.paging.ShowSearchPagingSource
import com.cmc.curtaincall.data.source.remote.ShowRemoteSource
import com.cmc.curtaincall.domain.model.show.CostEffectiveShowModel
import com.cmc.curtaincall.domain.model.show.FacilityDetailModel
import com.cmc.curtaincall.domain.model.show.LiveTalkShowModel
import com.cmc.curtaincall.domain.model.show.ShowDetailModel
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import com.cmc.curtaincall.domain.model.show.ShowRankModel
import com.cmc.curtaincall.domain.model.show.ShowRecommendationModel
import com.cmc.curtaincall.domain.model.show.ShowSearchWordModel
import com.cmc.curtaincall.domain.model.show.SimilarShowInfoModel
import com.cmc.curtaincall.domain.repository.ShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
    private val showRemoteSource: ShowRemoteSource,
    private val showLocalSource: ShowLocalSource,
    private val showService: ShowService,
    private val favoriteService: FavoriteService
) : ShowRepository {

    override fun getShowSearchWordList(): Flow<List<ShowSearchWordModel>> =
        showLocalSource.getShowSearchWordList().map { showSearchWordEntityList ->
            showSearchWordEntityList.map { it.toModel() }
        }

    override suspend fun insertShowSearchWord(showSearchWordModel: ShowSearchWordModel) =
        showLocalSource.insertShowSearchWord(showSearchWordModel)

    override suspend fun deleteShowSearchWord(showSearchWordModel: ShowSearchWordModel) =
        showLocalSource.deleteShowSearchWord(showSearchWordModel)

    override suspend fun deleteShowSearchWordList() =
        showLocalSource.deleteShowSearchWordList()

    override fun getShowRankCacheTime(): Flow<Long> =
        showLocalSource.getShowRankCacheTime()

    override suspend fun saveShowRankCacheTime(time: Long) {
        showLocalSource.saveShowRankCacheTime(time)
    }

    override fun getShowRankList(): Flow<List<ShowRankModel>> =
        showLocalSource.getShowRankList().map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun saveShowRankList(showRanks: List<ShowRankModel>) {
        showLocalSource.saveShowRankList(showRanks = showRanks)
    }

    override fun fetchShowList(
        genre: String,
        sort: String?
    ): Flow<PagingData<ShowInfoModel>> {
        return Pager(
            config = PagingConfig(pageSize = SHOW_PAGE_SIZE),
            pagingSourceFactory = { ShowPagingSource(showService, favoriteService, genre, sort) }
        ).flow
    }

    override fun requestShowList(page: Int, size: Int, genre: String, sort: String?): Flow<List<ShowInfoModel>> =
        showRemoteSource.requestShowList(page, size, genre, sort).map { showInfoResponses ->
            showInfoResponses.map { it.toModel() }
        }

    override fun fetchSearchShowList(keyword: String): Flow<PagingData<ShowInfoModel>> {
        return Pager(
            config = PagingConfig(pageSize = SHOW_SEARCH_PAGE_SIZE),
            pagingSourceFactory = { ShowSearchPagingSource(showService, favoriteService, keyword) }
        ).flow
    }

    override fun searchShowList(page: Int, size: Int, keyword: String): Flow<List<ShowInfoModel>> =
        showRemoteSource.searchShowList(page, size, keyword).map { showInfoResponse ->
            showInfoResponse.map { it.toModel() }
        }

    override fun requestOpenShowList(page: Int, size: Int, startDate: String): Flow<List<ShowInfoModel>> =
        showRemoteSource.requestOpenShowList(page, size, startDate).map { showInfoResponse ->
            showInfoResponse.map { it.toModel() }
        }

    override fun requestEndShowList(page: Int, size: Int, endDate: String, genre: String?): Flow<List<ShowInfoModel>> =
        showRemoteSource.requestEndShowList(page, size, endDate, genre).map { showInfoResponse ->
            showInfoResponse.map { it.toModel() }
        }

    override fun requestShowDetail(showId: String): Flow<ShowDetailModel> =
        showRemoteSource.requestShowDetail(showId).map { it.toModel() }

    override fun requestPopularShowList(type: String, genre: String?, baseDate: String): Flow<List<ShowRankModel>> =
        showRemoteSource.requestPopularShowList(type, genre, baseDate).map { showRankResponse ->
            showRankResponse.map { it.toModel() }
        }

    override fun requestFacilityDetail(facilityId: String): Flow<FacilityDetailModel> =
        showRemoteSource.requestFacilityDetail(facilityId).map { it.toModel() }

    override fun requestSimilarShowList(facilityId: String, page: Int, size: Int?, genre: String?): Flow<List<SimilarShowInfoModel>> =
        showRemoteSource.requestSimilarShowList(
            facilityId = facilityId,
            page = page,
            size = size,
            genre = genre
        ).map { similarShowInfo ->
            similarShowInfo.map { it.toModel() }
        }

    override fun requestLiveTalkShowList(page: Int?, size: Int?, baseDateTime: String): Flow<List<LiveTalkShowModel>> {
        return showRemoteSource.requestLiveTalkShowList(
            page = page,
            size = size,
            baseDateTime = baseDateTime
        ).map { response ->
            response.map { it.toModel() }
        }
    }

    override fun requestCostEffectiveShows(genre: String): Flow<List<CostEffectiveShowModel>> {
        return showRemoteSource.requestCostEffectiveShows(genre)
            .map { response ->
                response.map { it.toModel() }
            }
    }

    override fun requestShowRecommendation(): Flow<List<ShowRecommendationModel>> {
        return showRemoteSource.requestShowRecommendations().map { response ->
            response.map { it.toModel() }
        }
    }
}
