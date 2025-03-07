package com.cmc.curtaincall.core.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val SERVER_URL = stringPreferencesKey("SERVER_URL")

    val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
    val ACCESS_TOKEN_EXPIRESAT = stringPreferencesKey("ACCESS_TOKEN_EXPIRESAT")
    val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    val REFRESH_TOKEN_EXPIRESAT = stringPreferencesKey("REFRESH_TOKEN_EXPIRESAT")

    val MEMBER_ID = intPreferencesKey("MEMBER_ID")
    val MEMBER_NICKNAME = stringPreferencesKey("MEMBER_NICKNAME")

    val IS_FIRST_ENTRY_ONBOARDING = booleanPreferencesKey("IS_FIRST_ENTRY_ONBOARDING")
    val IS_FIRST_ENTRY_SHOW_LIST = booleanPreferencesKey("IS_FIRST_ENTRY_SHOW_LIST")
    val IS_SHOW_PARTY_TOOLTIPS = booleanPreferencesKey("IS_SHOW_PARTY_TOOLTIPS")
    val IS_SHOW_PARTY_SORT_TOOLTIP = booleanPreferencesKey("IS_SHOW_PARTY_SORT_TOOLTIP")
    val IS_SHOW_HOME_TOOLTIP = booleanPreferencesKey("IS_SHOW_HOME_TOOLTIP")

    val SHOW_RANKS_CACHE_TIME = longPreferencesKey("SHOW_RANKS_CACHE_TIME")
}
