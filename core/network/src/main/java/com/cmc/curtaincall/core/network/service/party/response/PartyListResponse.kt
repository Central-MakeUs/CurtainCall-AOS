package com.cmc.curtaincall.core.network.service.party.response

import com.cmc.curtaincall.domain.model.party.PartyModel
import com.google.gson.annotations.SerializedName

data class PartyListResponse(
    @SerializedName("content") val parties: List<PartyResponse>
)

data class PartyResponse(
    val content: String?,
    val createdAt: String,
    val creatorId: Int,
    val creatorImageUrl: String?,
    val creatorNickname: String,
    val curMemberNum: Int,
    val facilityId: String?,
    val facilityName: String?,
    val id: Int,
    val maxMemberNum: Int,
    val showAt: String?,
    val showId: String?,
    val showName: String?,
    val showPoster: String?,
    val title: String
) {
    fun toModel() = PartyModel(
        content = content ?: "",
        createdAt = createdAt,
        creatorId = creatorId,
        creatorImageUrl = creatorImageUrl,
        creatorNickname = creatorNickname,
        curMemberNum = curMemberNum,
        facilityId = facilityId,
        facilityName = facilityName,
        id = id,
        maxMemberNum = maxMemberNum,
        showAt = showAt,
        showId = showId,
        showName = showName,
        showPoster = showPoster,
        title = title
    )
}
