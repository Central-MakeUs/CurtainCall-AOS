package com.cmc.curtaincall.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmc.curtaincall.core.network.service.member.MemberService
import com.cmc.curtaincall.core.network.service.member.response.MemberRecruitmentResponse
import javax.inject.Inject

private const val RECRUITMENT_STARTING_KEY = 0
const val RECRUITMENT_PAGE_SIZE = 20

class MyRecruitmentPagingSource @Inject constructor(
    private val memberService: MemberService,
    private val memberId: Int
) : PagingSource<Int, MemberRecruitmentResponse>() {
    override fun getRefreshKey(state: PagingState<Int, MemberRecruitmentResponse>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MemberRecruitmentResponse> {
        try {
            val pageKey = params.key ?: RECRUITMENT_STARTING_KEY
            val response = memberService.requestMyRecruitments(
                page = pageKey,
                size = RECRUITMENT_PAGE_SIZE,
                memberId = memberId
            )
            return LoadResult.Page(
                response.recruitments,
                prevKey = null,
                nextKey = if (response.recruitments.isEmpty()) null else pageKey + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
