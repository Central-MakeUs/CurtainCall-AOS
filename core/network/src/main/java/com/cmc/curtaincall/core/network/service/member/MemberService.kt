package com.cmc.curtaincall.core.network.service.member

import com.cmc.curtaincall.core.network.BuildConfig
import com.cmc.curtaincall.core.network.service.member.request.DeleteMemberRequest
import com.cmc.curtaincall.core.network.service.member.request.MemberCreateRequest
import com.cmc.curtaincall.core.network.service.member.request.UpdateMemberRequest
import com.cmc.curtaincall.core.network.service.member.response.MemberCreateResponse
import com.cmc.curtaincall.core.network.service.member.response.MemberDuplicateNickNameResponse
import com.cmc.curtaincall.core.network.service.member.response.MemberInfoResponse
import com.cmc.curtaincall.core.network.service.member.response.MemberLostItemsResponse
import com.cmc.curtaincall.core.network.service.member.response.MemberParticipationsResponse
import com.cmc.curtaincall.core.network.service.member.response.MemberRecruitmentsResponse
import com.cmc.curtaincall.core.network.service.member.response.MemberReviewsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberService {
    @GET("members/duplicate/nickname")
    suspend fun checkDuplicateNickname(
        @Query("nickname") nickname: String
    ): MemberDuplicateNickNameResponse

    @POST("signup")
    suspend fun createMember(
        @Body memberCreateRequest: MemberCreateRequest
    ): MemberCreateResponse

    @GET("members/{memberId}")
    suspend fun requestMemberInfo(
        @Path("memberId") memberId: Int
    ): MemberInfoResponse

    @PATCH("member")
    suspend fun updateMember(
        @Body updateMemberRequest: UpdateMemberRequest
    ): Response<Unit>

    @GET("members/{memberId}/recruitments")
    suspend fun requestMyRecruitments(
        @Path("memberId") memberId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): MemberRecruitmentsResponse

    @GET("members/{memberId}/participations")
    suspend fun requestMyParticipations(
        @Path("memberId") memberId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): MemberParticipationsResponse

    @GET("member/reviews")
    suspend fun requestMyReviews(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): MemberReviewsResponse

    @GET("member/lostItems")
    suspend fun requestMyLostItems(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): MemberLostItemsResponse

    @HTTP(method = "DELETE", path = "${BuildConfig.CURTAIN_CALL_BASE_URL}/member", hasBody = true)
    suspend fun deleteMember(
        @Header("Authorization") authorization: String,
        @Body deleteMemberRequest: DeleteMemberRequest
    ): Response<Unit>
}
