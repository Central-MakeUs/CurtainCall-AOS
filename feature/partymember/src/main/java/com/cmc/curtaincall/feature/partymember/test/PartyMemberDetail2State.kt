package com.cmc.curtaincall.feature.partymember.test

import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import io.getstream.chat.android.client.models.User

data class PartyMemberDetail2State(
    val partyDetailModel: PartyDetailModel = PartyDetailModel(),
    val user: User = User(),
    val isParticipation: Boolean = false
) : BaseState
