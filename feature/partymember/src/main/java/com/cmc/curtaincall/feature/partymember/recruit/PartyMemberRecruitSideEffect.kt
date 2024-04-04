package com.cmc.curtaincall.feature.partymember.recruit

import com.cmc.curtaincall.core.base.BaseSideEffect

sealed class PartyMemberRecruitSideEffect : BaseSideEffect {
    object CreateParty : PartyMemberRecruitSideEffect()
    object FailedCreateParty : PartyMemberRecruitSideEffect()
}
