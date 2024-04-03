package com.cmc.curtaincall.domain.model.member

import com.cmc.curtaincall.domain.enums.ShowGenreType

data class MemberReviewModel(
    val id: Int = 0,
    val showId: String = "",
    val showName: String = "",
    val grade: Int = 0,
    val content: String = "",
    val createdAt: String = "",
    val likeCount: Int = 0,
    val genreType: ShowGenreType = ShowGenreType.MUSICAL
)
