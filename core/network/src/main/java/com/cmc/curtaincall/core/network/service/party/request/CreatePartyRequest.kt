package com.cmc.curtaincall.core.network.service.party.request

data class CreatePartyRequest(
    val showId: String?,
    val showAt: String?,
    val title: String,
    val content: String,
    val maxMemberNum: Int
)
