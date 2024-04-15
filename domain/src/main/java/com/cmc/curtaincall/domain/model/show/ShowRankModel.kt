package com.cmc.curtaincall.domain.model.show

data class ShowRankModel(
    val rank: Int = 1,
    val id: String = "",
    val name: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val facilityName: String = "",
    val poster: String = "",
    val genre: String = "",
    val runtime: String = "",
    val reviewCount: Int = 0,
    val reviewGradeSum: Int = 0,
    val reviewGradeAvg: Float = 0f,
    val favorite: Boolean = false
)
