package com.cmc.curtaincall.feature.show.search

import com.cmc.curtaincall.core.base.BaseSideEffect

sealed class ShowSearchEffect : BaseSideEffect {

    object RefreshShowList : ShowSearchEffect()

    object ScrollFirstInList : ShowSearchEffect()
}
