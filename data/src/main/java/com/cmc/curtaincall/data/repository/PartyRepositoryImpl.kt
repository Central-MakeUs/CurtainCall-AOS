package com.cmc.curtaincall.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc.curtaincall.core.network.service.party.PartyService
import com.cmc.curtaincall.data.source.local.PartyLocalSource
import com.cmc.curtaincall.data.source.paging.PARTY_PAGE_SIZE
import com.cmc.curtaincall.data.source.paging.PARTY_SEARCH_PAGE_SIZE
import com.cmc.curtaincall.data.source.paging.PartyPagingSource
import com.cmc.curtaincall.data.source.paging.PartySearchPagingSource
import com.cmc.curtaincall.data.source.remote.PartyRemoteSource
import com.cmc.curtaincall.domain.model.party.CheckPartyModel
import com.cmc.curtaincall.domain.model.party.CreatePartyModel
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import com.cmc.curtaincall.domain.model.party.PartyModel
import com.cmc.curtaincall.domain.model.party.PartySearchWordModel
import com.cmc.curtaincall.domain.repository.PartyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyRemoteSource: PartyRemoteSource,
    private val partyService: PartyService,
    private val partyLocalSource: PartyLocalSource
) : PartyRepository {

    override fun getPartySearchWordList(): Flow<List<PartySearchWordModel>> =
        partyLocalSource.getPartySearchWordList().map { partySearchWordEntityList ->
            partySearchWordEntityList.map { it.toModel() }
        }

    override suspend fun insertPartySearchWord(partySearchWordModel: PartySearchWordModel) {
        partyLocalSource.insertPartySearchWord(partySearchWordModel)
    }

    override suspend fun deletePartySearchWord(partySearchWordModel: PartySearchWordModel) {
        partyLocalSource.deletePartySearchWord(partySearchWordModel)
    }

    override suspend fun deletePartySearchWordList() {
        partyLocalSource.deletePartySearchWordList()
    }

    override fun fetchPartyList(
        startDate: String?,
        endDate: String?
    ): Flow<PagingData<PartyModel>> {
        return Pager(
            config = PagingConfig(pageSize = PARTY_PAGE_SIZE),
            pagingSourceFactory = { PartyPagingSource(partyService, startDate, endDate) }
        ).flow
    }

    override fun requestPartyList(
        page: Int,
        size: Int?,
        startDate: String?,
        endDate: String?
    ): Flow<List<PartyModel>> =
        partyRemoteSource.requestPartyList(page, size, startDate, endDate).map { parties ->
            parties.map { it.toModel() }
        }

    override fun fetchSearchPartyList(keyword: String): Flow<PagingData<PartyModel>> {
        return Pager(
            config = PagingConfig(pageSize = PARTY_SEARCH_PAGE_SIZE),
            pagingSourceFactory = { PartySearchPagingSource(partyService, keyword) }
        ).flow
    }

    override fun searchPartyList(page: Int, size: Int, keyword: String): Flow<List<PartyModel>> =
        partyRemoteSource.searchPartyList(page, size, keyword).map { parties ->
            parties.map { it.toModel() }
        }

    override fun requestPartyDetail(partyId: Int): Flow<PartyDetailModel> =
        partyRemoteSource.requestPartyDetail(partyId).map { it.toModel() }

    override fun createParty(
        showId: String?,
        showAt: String?,
        title: String,
        content: String,
        maxMemberNum: Int
    ): Flow<CreatePartyModel> =
        partyRemoteSource.createParty(
            showId = showId,
            showAt = showAt,
            title = title,
            content = content,
            maxMemberNum = maxMemberNum
        ).map { it.toModel() }

    override fun deleteParty(partyId: Int): Flow<Boolean> =
        partyRemoteSource.deleteParty(partyId)

    override fun updateParty(title: String, content: String): Flow<Boolean> =
        partyRemoteSource.updateParty(title, content)

    override fun participateParty(partyId: Int): Flow<Boolean> =
        partyRemoteSource.participateParty(partyId)

    override fun checkParty(partyId: Int): Flow<CheckPartyModel> =
        partyRemoteSource.checkParty(partyId).map { it.toModel() }
}
