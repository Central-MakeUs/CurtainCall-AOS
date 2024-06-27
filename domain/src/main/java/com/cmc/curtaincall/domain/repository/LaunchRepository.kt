package com.cmc.curtaincall.domain.repository

import kotlinx.coroutines.flow.Flow

interface LaunchRepository {

    suspend fun setServerUrl(url: String)

    fun getIsFirstEntryOnBoarding(): Flow<Boolean>

    suspend fun setIsFirstEntryOnBoarding()

    fun getIsFirstEntryShowList(): Flow<Boolean>
    suspend fun setIsFirstEntryShowList()

    fun isShowPartyTooltip(): Flow<Boolean>

    suspend fun stopShowPartyTooltip()

    fun isShowPartySortTooltip(): Flow<Boolean>

    suspend fun stopShowPartySortTooltip()

    fun isShowHomeTooltip(): Flow<Boolean>

    suspend fun stopShowHomeTooltip()
}
