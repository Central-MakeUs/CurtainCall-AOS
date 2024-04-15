package com.cmc.curtaincall.core.network.service.show.response

import com.cmc.curtaincall.domain.model.show.ShowRankModel
import com.google.gson.annotations.SerializedName

data class ShowRanksResponse(
    @SerializedName("content") val showRanks: List<ShowRankResponse>
)

data class ShowRankResponse(
    val rank: Int,
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val facilityName: String,
    val poster: String,
    val genre: String,
    val runtime: String,
    val reviewCount: Int,
    val reviewGradeSum: Int,
    val reviewGradeAvg: Float
) {
    fun toModel() = ShowRankModel(
        rank = rank,
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        facilityName = facilityName,
        poster = poster,
        genre = genre,
        runtime = runtime,
        reviewCount = reviewCount,
        reviewGradeSum = reviewGradeSum,
        reviewGradeAvg = reviewGradeAvg
    )
}
