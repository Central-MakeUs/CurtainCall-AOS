package com.cmc.curtaincall.ui

import com.cmc.curtaincall.core.base.BaseSideEffect

sealed class SplashSideEffect : BaseSideEffect {
    object AutoLogin : SplashSideEffect()
    object NeedLogin : SplashSideEffect()
}
