package com.cmc.curtaincall.feature.performance.review

sealed class PerformanceReviewSideEffect {
    object CreateSuccess : PerformanceReviewSideEffect()
    object UpdateSuccess : PerformanceReviewSideEffect()

    object DeleteSuccess : PerformanceReviewSideEffect()
}
