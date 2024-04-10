package com.cmc.curtaincall.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmc.curtaincall.core.network.service.favorite.FavoriteService
import com.cmc.curtaincall.core.network.service.show.ShowService
import com.cmc.curtaincall.domain.model.show.ShowInfoModel
import timber.log.Timber
import javax.inject.Inject

private const val SHOW_STARTING_KEY = 0
const val SHOW_PAGE_SIZE = 20

class ShowPagingSource @Inject constructor(
    private val showService: ShowService,
    private val favoriteService: FavoriteService,
    private val genre: String,
    private val sort: String?
) : PagingSource<Int, ShowInfoModel>() {
    override fun getRefreshKey(state: PagingState<Int, ShowInfoModel>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShowInfoModel> {
        try {
            val pageKey = params.key ?: SHOW_STARTING_KEY
            val response = showService.requestShowList(
                page = pageKey,
                size = SHOW_PAGE_SIZE,
                genre = genre,
                sort = sort
            )

            val favoriteShows = favoriteService.checkFavoriteShows(response.showInfos.map { it.id })
            val result = response.showInfos.map { showInfo ->
                showInfo.toModel().copy(
                    favorite = favoriteShows.checkFavoriteShows.find {
                        it.showId == showInfo.id
                    }?.favorite ?: false
                )
            }

            Timber.d("ShowPagingSource $pageKey")
            return LoadResult.Page(
                result,
                prevKey = if (pageKey == SHOW_STARTING_KEY) null else pageKey - 1,
                nextKey = if (result.isEmpty()) null else pageKey + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
