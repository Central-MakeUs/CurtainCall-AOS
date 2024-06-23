package com.cmc.curtaincall.core.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cmc.curtaincall.core.local.db.entity.ShowRankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowRankDao {
    @Query("SELECT * FROM ShowRankEntity ORDER BY rank ASC")
    fun getAll(): Flow<List<ShowRankEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(showRanks: List<ShowRankEntity>)

    @Query("DELETE FROM ShowRankEntity")
    suspend fun deleteAll()

    @Transaction
    suspend fun insertShowRanks(showRanks: List<ShowRankEntity>) {
        deleteAll()
        insertAll(showRanks)
    }
}
