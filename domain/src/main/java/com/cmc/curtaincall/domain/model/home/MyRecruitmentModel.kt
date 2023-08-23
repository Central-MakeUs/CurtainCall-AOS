package com.cmc.curtaincall.domain.model.home

data class MyRecruitmentModel(
    val id: Int = Int.MIN_VALUE,
    val title: String = "",
    val curMemberNum: Int = 0,
    val maxMemberNum: Int = 0,
    val showAt: String = "",
    val createdAt: String = "",
    val category: String = "",
    val creatorId: Int = 0,
    val creatorNickname: String = "",
    val creatorImageUrl: String? = null,
    val showId: String = "",
    val showName: String = "",
    val showPoster: String = "",
    val facilityId: String = "",
    val facilityName: String = ""
)
