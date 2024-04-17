package com.cmc.curtaincall.domain.model.member

data class MyRecruitmentModel(
    val id: Int = Int.MIN_VALUE,
    val title: String = "",
    val content: String = "",
    val curMemberNum: Int = 0,
    val maxMemberNum: Int = 0,
    val showAt: String? = null,
    val createdAt: String = "",
    val showId: String? = null,
    val showName: String? = null,
    val showPoster: String? = null,
    val facilityId: String? = null,
    val facilityName: String? = null
)
