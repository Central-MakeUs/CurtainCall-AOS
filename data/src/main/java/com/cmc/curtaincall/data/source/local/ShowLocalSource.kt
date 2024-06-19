package com.cmc.curtaincall.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.cmc.curtaincall.core.local.PreferenceKeys.SHOW_RANKS_CACHE_TIME
import com.cmc.curtaincall.core.local.db.dao.ShowSearchDao
import com.cmc.curtaincall.core.local.db.entity.ShowSearchWordEntity
import com.cmc.curtaincall.core.local.qualifiers.LaunchDataStore
import com.cmc.curtaincall.domain.model.show.ShowSearchWordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShowLocalSource @Inject constructor(
    private val showSearchDao: ShowSearchDao,
    @LaunchDataStore private val dataStore: DataStore<Preferences>
) {
    fun getShowSearchWordList(): Flow<List<ShowSearchWordEntity>> = showSearchDao.getAll()

    suspend fun insertShowSearchWord(showSearchWordModel: ShowSearchWordModel) =
        showSearchDao.insertShowSearch(
            showSearchWordEntity = ShowSearchWordEntity(
                showSearchWordModel.word,
                showSearchWordModel.searchAt
            )
        )

    suspend fun deleteShowSearchWord(showSearchWordModel: ShowSearchWordModel) =
        showSearchDao.deleteShowSearch(
            showSearchWordEntity = ShowSearchWordEntity(
                showSearchWordModel.word,
                showSearchWordModel.searchAt
            )
        )

    suspend fun deleteShowSearchWordList() = showSearchDao.deleteAll()

    fun getShowRankCacheTime(): Flow<Long> =
        dataStore.data.map { preferences ->
            preferences[SHOW_RANKS_CACHE_TIME] ?: 0L
        }

    suspend fun saveShowRankCacheTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[SHOW_RANKS_CACHE_TIME] = time
        }
    }
}
