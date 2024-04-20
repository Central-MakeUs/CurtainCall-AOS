package com.cmc.curtaincall.core.network.service.party

import com.cmc.curtaincall.core.network.service.party.request.CreatePartyRequest
import com.cmc.curtaincall.core.network.service.party.request.UpdatePartyRequest
import com.cmc.curtaincall.core.network.service.party.response.CheckPartiesResponse
import com.cmc.curtaincall.core.network.service.party.response.CreatePartyResponse
import com.cmc.curtaincall.core.network.service.party.response.PartyDetailResponse
import com.cmc.curtaincall.core.network.service.party.response.PartyListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PartyService {

    @GET("parties")
    suspend fun requestPartyList(
        @Query("page") page: Int,
        @Query("size") size: Int?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): PartyListResponse

    @GET("search/party")
    suspend fun searchPartyList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("keyword") keyword: String
    ): PartyListResponse

    @GET("parties/{partyId}")
    suspend fun requestPartyDetail(
        @Path("partyId") partyId: Int
    ): PartyDetailResponse

    @POST("parties")
    suspend fun createParty(
        @Body createPartyRequest: CreatePartyRequest
    ): CreatePartyResponse

    @DELETE("parties/{partyId}")
    suspend fun deleteParty(
        @Path("partyId") partyId: Int
    ): Response<Unit>

    @PATCH("parties/{partyId}")
    suspend fun updateParty(
        @Path("partyId") partyId: Int,
        @Body updatePartyRequest: UpdatePartyRequest
    ): Response<Unit>

    @PUT("member/parties/{partyId}")
    suspend fun participateParty(
        @Path("partyId") partyId: Int
    ): Response<Unit>

    @DELETE("member/parties/{partyId}")
    suspend fun cancelParty(
        @Path("partyId") partyId: Int
    ): Response<Unit>

    @GET("member/participated")
    suspend fun checkParties(
        @Query("partyIds") partyIds: List<Int>
    ): CheckPartiesResponse
}
