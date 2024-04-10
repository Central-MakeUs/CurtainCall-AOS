package com.cmc.curtaincall.core.network.service.show.response

import com.cmc.curtaincall.domain.model.show.CostEffectiveShowModel
import com.google.gson.annotations.SerializedName

data class CostEffectiveShowsResponse(
    @SerializedName("content")
    val shows: List<CostEffectiveShowResponse>
)

data class CostEffectiveShowResponse(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val poster: String,
    val genre: String,
    val minTicketPrice: Int
) {
    fun toModel() = CostEffectiveShowModel(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        poster = poster,
        genre = genre,
        minTicketPrice = minTicketPrice
    )
}
