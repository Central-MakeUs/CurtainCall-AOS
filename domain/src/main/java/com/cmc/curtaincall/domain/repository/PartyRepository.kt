package com.cmc.curtaincall.domain.repository

import androidx.paging.PagingData
import com.cmc.curtaincall.domain.model.party.CheckPartyModel
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
        startDate: String?,
        endDate: String?
    ): Flow<PagingData<PartyModel>>

    fun requestPartyList(
        page: Int,
        size: Int?,
        startDate: String?,
        endDate: String?
    ): Flow<List<PartyModel>>

    fun fetchSearchPartyList(
        keyword: String
    ): Flow<PagingData<PartyModel>>

    fun searchPartyList(
        page: Int,
        size: Int,
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
        maxMemberNum: Int
    ): Flow<CreatePartyModel>

    fun deleteParty(
        partyId: Int
    ): Flow<Boolean>

    fun updateParty(
        title: String,
        content: String
    ): Flow<Boolean>

    fun participateParty(
        partyId: Int
    ): Flow<Boolean>

    fun cancelParty(
        partyId: Int
    ): Flow<Boolean>

    fun checkParty(
        partyId: Int
    ): Flow<CheckPartyModel>
}
