package com.cmc.curtaincall.domain.repository

import kotlinx.coroutines.flow.Flow

interface ReportRepository {

    fun requestReport(
        idToReport: Int,
        type: String,
        reason: String,
        content: String
    ): Flow<Boolean>
}
