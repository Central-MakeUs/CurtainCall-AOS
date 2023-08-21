package com.cmc.curtaincall.domain.model

data class LoginResultModel(
    val memberId: Int? = null,
    val accessToken: String = "",
    val accessTokenExpiresAt: String = "",
    val refreshToken: String = "",
    val refreshTokenExpiresAt: String = ""
)
