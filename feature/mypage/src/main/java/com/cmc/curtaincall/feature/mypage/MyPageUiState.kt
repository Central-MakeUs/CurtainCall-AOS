package com.cmc.curtaincall.feature.mypage

import com.cmc.curtaincall.core.base.BaseState
import com.cmc.curtaincall.domain.model.member.MemberInfoModel

data class MyPageUiState(
    val memberInfoModel: MemberInfoModel = MemberInfoModel()
) : BaseState
