package com.cmc.curtaincall.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmc.curtaincall.core.network.service.member.MemberService
import com.cmc.curtaincall.core.network.service.member.response.MemberParticipationResponse
import javax.inject.Inject

private const val PARTICIPATION_STARTING_KEY = 0
const val PARTICIPATION_PAGE_SIZE = 20

class MyParticipationPagingSource @Inject constructor(
    private val memberService: MemberService,
    private val memberId: Int
) : PagingSource<Int, MemberParticipationResponse>() {
    override fun getRefreshKey(state: PagingState<Int, MemberParticipationResponse>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MemberParticipationResponse> {
        try {
            val pageKey = params.key ?: PARTICIPATION_STARTING_KEY
            val response = memberService.requestMyParticipations(
                page = pageKey,
                size = PARTICIPATION_PAGE_SIZE,
                memberId = memberId
            )
            return LoadResult.Page(
                response.participations,
                prevKey = null,
                nextKey = if (response.participations.isEmpty()) null else pageKey + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
