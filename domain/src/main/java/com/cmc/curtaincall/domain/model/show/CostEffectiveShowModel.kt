package com.cmc.curtaincall.domain.model.show

data class CostEffectiveShowModel(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val poster: String,
    val genre: String,
    val minTicketPrice: Int
)
