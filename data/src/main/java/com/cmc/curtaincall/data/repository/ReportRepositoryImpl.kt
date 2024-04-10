package com.cmc.curtaincall.data.repository

import com.cmc.curtaincall.data.source.remote.ReportRemoteSource
import com.cmc.curtaincall.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteSource: ReportRemoteSource
) : ReportRepository {
    override fun requestReport(idToReport: Int, type: String, reason: String, content: String): Flow<Boolean> =
        reportRemoteSource.requestReport(
            idToReport = idToReport,
            type = type,
            reason = reason,
            content = content
        )
}
