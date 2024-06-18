package com.cmc.curtaincall.core.network.service.member.response

import com.cmc.curtaincall.domain.model.member.MemberInfoModel

data class MemberInfoResponse(
    val id: Int,
    val nickname: String,
    val imageId: Int?,
    val imageUrl: String?,
    val recruitingNum: Int,
    val participationNum: Int
) {
    fun toModel() = MemberInfoModel(
        id = id,
        nickname = nickname,
        imageId = imageId,
        imageUrl = imageUrl,
        recruitingNum = recruitingNum,
        participationNum = participationNum
    )
}
