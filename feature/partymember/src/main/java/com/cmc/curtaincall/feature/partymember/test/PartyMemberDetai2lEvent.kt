package com.cmc.curtaincall.feature.partymember.test

import com.cmc.curtaincall.core.base.BaseEvent
import com.cmc.curtaincall.domain.model.party.PartyDetailModel
import io.getstream.chat.android.client.models.User

sealed class PartyMemberDetai2lEvent : BaseEvent {
    data class RequestPartyDetail(
        val partyDetailModel: PartyDetailModel
    ) : PartyMemberDetai2lEvent()

    data class GetMemberInfo(
        val user: User
    ) : PartyMemberDetai2lEvent()

    data class ChangeParticipation(
        val isParticipation: Boolean
    ) : PartyMemberDetai2lEvent()
}
