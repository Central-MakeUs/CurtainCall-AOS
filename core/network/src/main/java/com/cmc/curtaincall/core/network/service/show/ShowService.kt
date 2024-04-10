package com.cmc.curtaincall.core.network.service.show

import com.cmc.curtaincall.core.network.service.show.response.CostEffectiveShowsResponse
import com.cmc.curtaincall.core.network.service.show.response.FacilityDetailResponse
import com.cmc.curtaincall.core.network.service.show.response.LiveTalkShowsResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowDetailResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowInfosResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowRanksResponse
import com.cmc.curtaincall.core.network.service.show.response.ShowRecommendationsResponse
import com.cmc.curtaincall.core.network.service.show.response.SimilarShowInfosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowService {

    @GET("shows")
    suspend fun requestShowList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("genre") genre: String,
        @Query("sort") sort: String?
    ): ShowInfosResponse

    @GET("search/shows")
    suspend fun searchShowList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("keyword") keyword: String
    ): ShowInfosResponse

    @GET("shows-to-open")
    suspend fun requestOpenShowList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("startDate") startDate: String
    ): ShowInfosResponse

    @GET("shows-to-end")
    suspend fun requestEndShowList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("endDate") endDate: String,
        @Query("genre") genre: String?
    ): ShowInfosResponse

    @GET("shows/{showId}")
    suspend fun requestShowDetail(
        @Path("showId") showId: String
    ): ShowDetailResponse

    @GET("box-office")
    suspend fun requestPopularShowList(
        @Query("type") type: String,
        @Query("genre") genre: String?,
        @Query("baseDate") baseDate: String
    ): ShowRanksResponse

    @GET("facilities/{facilityId}")
    suspend fun requestFacilityDetail(
        @Path("facilityId") facilityId: String
    ): FacilityDetailResponse

    @GET("facilities/{facilityId}/shows")
    suspend fun requestSimilarShowList(
        @Path("facilityId") facilityId: String,
        @Query("page") page: Int,
        @Query("size") size: Int?,
        @Query("genre") genre: String?
    ): SimilarShowInfosResponse

    @GET("livetalk-show-times")
    suspend fun requestLiveTalkShowList(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("baseDateTime") baseDateTime: String
    ): LiveTalkShowsResponse

    @GET("cost-effective-shows")
    suspend fun requestCostEffectiveShows(
        @Query("genre") genre: String?
    ): CostEffectiveShowsResponse

    @GET("show-recommendations")
    suspend fun requestShowRecommendations(): ShowRecommendationsResponse
}
