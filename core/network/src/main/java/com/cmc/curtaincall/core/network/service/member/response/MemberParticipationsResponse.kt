package com.cmc.curtaincall.core.network.service.member.response

import com.cmc.curtaincall.domain.model.member.MyParticipationModel
import com.google.gson.annotations.SerializedName

data class MemberParticipationsResponse(
    @SerializedName("content") val participations: List<MemberParticipationResponse>
)

data class MemberParticipationResponse(
    val id: Int,
    val title: String,
    val createdAt: String,
    val creatorId: Int,
    val creatorImageUrl: String?,
    val creatorNickname: String,
    val curMemberNum: Int,
    val facilityId: String,
    val facilityName: String,
    val maxMemberNum: Int,
    val showAt: String,
    val showId: String,
    val showName: String,
    val showPoster: String,
    val content: String
) {
    fun toModel() = MyParticipationModel(
        id = id,
        title = title,
        content = content,
        curMemberNum = curMemberNum,
        maxMemberNum = maxMemberNum,
        showAt = showAt,
        createdAt = createdAt,
        creatorId = creatorId,
        creatorNickname = creatorNickname,
        creatorImageUrl = creatorImageUrl,
        showId = showId,
        showName = showName,
        showPoster = showPoster,
        facilityId = facilityId,
        facilityName = facilityName
    )
}
