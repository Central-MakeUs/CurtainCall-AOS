package com.cmc.curtaincall.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.cmc.curtaincall.core.network.service.party.PartyService
import com.cmc.curtaincall.data.source.paging.PARTY_PAGE_SIZE
import com.cmc.curtaincall.data.source.paging.PartyPagingSource
import com.cmc.curtaincall.data.source.remote.PartyRemoteSource
import com.cmc.curtaincall.domain.model.party.CreatePartyModel
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import com.cmc.curtaincall.domain.model.party.PartyModel
import com.cmc.curtaincall.domain.repository.PartyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyRemoteSource: PartyRemoteSource,
    private val partyService: PartyService
) : PartyRepository {

    override fun fetchPartyList(category: String): Flow<PagingData<PartyModel>> {
        return Pager(
            config = PagingConfig(pageSize = PARTY_PAGE_SIZE),
            pagingSourceFactory = { PartyPagingSource(partyService, category) }
        ).flow
            .map { pagingData ->
                pagingData.map { response ->
                    response.toModel()
                }
            }
    }

    override fun requestPartyList(page: Int, size: Int, category: String): Flow<List<PartyModel>> =
        partyRemoteSource.requestPartyList(page, size, category).map { parties ->
            parties.map { it.toModel() }
        }

    override fun searchPartyList(page: Int, size: Int, category: String, keyword: String): Flow<List<PartyModel>> =
        partyRemoteSource.searchPartyList(page, size, category, keyword).map { parties ->
            parties.map { it.toModel() }
        }

    override fun requestPartyDetail(partyId: String): Flow<PartyDetailModel> =
        partyRemoteSource.requestPartyDetail(partyId).map { it.toModel() }

    override fun createParty(showId: String, showAt: String, title: String, content: String, maxMemberNum: Int, category: String): Flow<CreatePartyModel> =
        partyRemoteSource.createParty(
            showId = showId,
            showAt = showAt,
            title = title,
            content = content,
            maxMemberNum = maxMemberNum,
            category = category
        ).map { it.toModel() }

    override fun deleteParty(partyId: String): Flow<Boolean> =
        partyRemoteSource.deleteParty(partyId)

    override fun updateParty(title: String, content: String): Flow<Boolean> =
        partyRemoteSource.updateParty(title, content)

    override fun participateParty(partyId: String): Flow<Boolean> =
        partyRemoteSource.participateParty(partyId)
}
