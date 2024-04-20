package com.cmc.curtaincall.data.source.remote

import com.cmc.curtaincall.core.network.service.member.MemberService
import com.cmc.curtaincall.core.network.service.member.request.DeleteMemberRequest
import com.cmc.curtaincall.core.network.service.member.request.MemberCreateRequest
import com.cmc.curtaincall.core.network.service.member.request.UpdateMemberRequest
import com.cmc.curtaincall.domain.model.member.MemberInfoModel
import com.cmc.curtaincall.domain.model.member.MyParticipationModel
import com.cmc.curtaincall.domain.model.member.MyRecruitmentModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemberRemoteSource @Inject constructor(
    private val memberService: MemberService
) {
    fun checkDuplicateNickname(nickname: String): Flow<Boolean> = flow {
        emit(memberService.checkDuplicateNickname(nickname).result)
    }

    fun createMember(nickname: String): Flow<Int> = flow {
        emit(memberService.createMember(MemberCreateRequest(nickname)).id)
    }

    fun requestMemberInfo(memberId: Int): Flow<MemberInfoModel> = flow {
        emit(memberService.requestMemberInfo(memberId).toModel())
    }

    fun updateMember(
        nickname: String,
        imageId: Int?
    ): Flow<Boolean> = flow {
        emit(
            memberService.updateMember(
                updateMemberRequest = UpdateMemberRequest(
                    nickname = nickname,
                    imageId = imageId
                )
            ).isSuccessful
        )
    }

    fun requestMyRecruitments(
        memberId: Int,
        page: Int,
        size: Int
    ): Flow<List<MyRecruitmentModel>> = flow {
        emit(
            memberService.requestMyRecruitments(
                memberId = memberId,
                page = page,
                size = size
            ).recruitments.map { it.toModel() }
        )
    }

    fun requestMyParticipations(
        memberId: Int,
        page: Int,
        size: Int
    ): Flow<List<MyParticipationModel>> = flow {
        emit(
            memberService.requestMyParticipations(
                memberId = memberId,
                page = page,
                size = size
            ).participations.map { it.toModel() }
        )
    }

    fun deleteMember(
        authorization: String,
        reason: String,
        content: String
    ): Flow<Boolean> = flow {
        emit(
            memberService.deleteMember(
                authorization = "Bearer $authorization",
                deleteMemberRequest = DeleteMemberRequest(
                    reason = reason,
                    content = content
                )
            ).isSuccessful
        )
    }
}
