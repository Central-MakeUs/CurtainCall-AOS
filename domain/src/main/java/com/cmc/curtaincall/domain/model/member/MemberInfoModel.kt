package com.cmc.curtaincall.domain.model.member

data class MemberInfoModel(
    val id: Int = Int.MIN_VALUE,
    val nickname: String = "",
    val imageId: Int? = null,
    val imageUrl: String? = null,
    val recruitingNum: Int = 0,
    val participationNum: Int = 0
)
