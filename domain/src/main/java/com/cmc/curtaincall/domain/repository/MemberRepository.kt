package com.cmc.curtaincall.domain.repository

import androidx.paging.PagingData
import com.cmc.curtaincall.domain.model.member.MemberInfoModel
import com.cmc.curtaincall.domain.model.member.MemberLostItemModel
import com.cmc.curtaincall.domain.model.member.MemberReviewModel
import com.cmc.curtaincall.domain.model.member.MyParticipationModel
import com.cmc.curtaincall.domain.model.member.MyRecruitmentModel
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun checkDuplicateNickname(nickname: String): Flow<Boolean>
    fun createMember(nickname: String): Flow<Int>
    fun requestMemberInfo(memberId: Int): Flow<MemberInfoModel>

    fun updateMember(
        nickname: String,
        imageId: Int?
    ): Flow<Boolean>

    fun requestMyRecruitments(
        memberId: Int,
        page: Int,
        size: Int
    ): Flow<List<MyRecruitmentModel>>

    fun fetchMyRecruitments(
        memberId: Int
    ): Flow<PagingData<MyRecruitmentModel>>

    fun requestMyParticipations(
        memberId: Int,
        page: Int,
        size: Int
    ): Flow<List<MyParticipationModel>>

    fun fetchMyParticipations(
        memberId: Int
    ): Flow<PagingData<MyParticipationModel>>

    fun fetchMyReview(): Flow<PagingData<MemberReviewModel>>

    fun fetchMyLostItems(): Flow<PagingData<MemberLostItemModel>>

    fun deleteMember(
        authorization: String,
        reason: String,
        content: String
    ): Flow<Boolean>

    suspend fun saveMemberId(id: Int)

    suspend fun saveMemberNickname(nickname: String)

    fun getMemberId(): Flow<Int>

    fun getMemberNickname(): Flow<String>
}
