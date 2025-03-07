package com.cmc.curtaincall.domain.model.party

data class PartyDetailModel(
    val content: String = "",
    val createdAt: String = "",
    val creatorId: Int = Int.MIN_VALUE,
    val creatorImageUrl: String? = null,
    val creatorNickname: String = "",
    val curMemberNum: Int = 0,
    val facilityId: String? = null,
    val facilityName: String = "",
    val id: Int = Int.MIN_VALUE,
    val maxMemberNum: Int = 0,
    val showAt: String = "",
    val showId: String? = null,
    val showPoster: String? = null,
    val showName: String = "",
    val title: String = ""
)
