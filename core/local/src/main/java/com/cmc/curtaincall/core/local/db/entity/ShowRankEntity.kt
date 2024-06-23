package com.cmc.curtaincall.core.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cmc.curtaincall.domain.model.show.ShowRankModel

@Entity
data class ShowRankEntity(
    @PrimaryKey val id: String,
    val rank: Int,
    val name: String,
    val poster: String,
    val genre: String
) {
    fun toModel() = ShowRankModel(
        id = id,
        rank = rank,
        name = name,
        poster = poster,
        genre = genre
    )
}
