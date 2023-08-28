package com.cmc.curtaincall.domain.repository

import androidx.paging.PagingData
import com.cmc.curtaincall.domain.model.party.CreatePartyModel
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import com.cmc.curtaincall.domain.model.party.PartyModel
import com.cmc.curtaincall.domain.model.party.PartySearchWordModel
import kotlinx.coroutines.flow.Flow

interface PartyRepository {

    fun getPartySearchWordList(): Flow<List<PartySearchWordModel>>

    suspend fun insertPartySearchWord(partySearchWordModel: PartySearchWordModel)

    suspend fun deletePartySearchWord(partySearchWordModel: PartySearchWordModel)

    suspend fun deletePartySearchWordList()

    fun fetchPartyList(
        category: String
    ): Flow<PagingData<PartyModel>>

    fun requestPartyList(
        page: Int,
        size: Int,
        category: String
    ): Flow<List<PartyModel>>

    fun fetchSearchPartyList(
        category: String,
        keyword: String
    ): Flow<PagingData<PartyModel>>

    fun searchPartyList(
        page: Int,
        size: Int,
        category: String,
        keyword: String
    ): Flow<List<PartyModel>>

    fun requestPartyDetail(
        partyId: Int
    ): Flow<PartyDetailModel>

    fun createParty(
        showId: String?,
        showAt: String?,
        title: String,
        content: String,
        maxMemberNum: Int,
        category: String
    ): Flow<CreatePartyModel>

    fun deleteParty(
        partyId: Int
    ): Flow<Boolean>

    fun updateParty(
        title: String,
        content: String
    ): Flow<Boolean>

    fun participateParty(
        partyId: String
    ): Flow<Boolean>
}
