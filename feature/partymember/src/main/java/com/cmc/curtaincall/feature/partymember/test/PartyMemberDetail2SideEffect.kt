package com.cmc.curtaincall.feature.partymember.test

import com.cmc.curtaincall.core.base.BaseSideEffect

sealed class PartyMemberDetail2SideEffect : BaseSideEffect {
    object SuccessDelete : PartyMemberDetail2SideEffect()
    object SuccessParticipation : PartyMemberDetail2SideEffect()
}
