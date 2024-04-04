package com.cmc.curtaincall.feature.partymember.detail

import com.cmc.curtaincall.core.base.BaseEvent
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import io.getstream.chat.android.client.models.User

sealed class PartyMemberDetailEvent : BaseEvent {
    data class RequestPartyDetail(
        val partyDetailModel: PartyDetailModel
    ) : PartyMemberDetailEvent()

    data class GetMemberInfo(
        val user: User
    ) : PartyMemberDetailEvent()

    data class ChangeParticipation(
        val isParticipation: Boolean
    ) : PartyMemberDetailEvent()
}
