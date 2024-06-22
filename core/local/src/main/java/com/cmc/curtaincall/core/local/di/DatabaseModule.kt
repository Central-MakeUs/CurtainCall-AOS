package com.cmc.curtaincall.core.local.di

import android.content.Context
import androidx.room.Room
import com.cmc.curtaincall.core.local.db.CurtainCallDatabase
import com.cmc.curtaincall.core.local.db.MIGRATION_4_5
import com.cmc.curtaincall.core.local.db.dao.LostPropertySearchDao
import com.cmc.curtaincall.core.local.db.dao.PartySearchDao
import com.cmc.curtaincall.core.local.db.dao.ShowRankDao
import com.cmc.curtaincall.core.local.db.dao.ShowSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val CURTAINCALL_DB = "CURTAINCALL_DB.db"

    @Provides
    @Singleton
    fun provideCurtainCallDatabase(
        @ApplicationContext context: Context
    ): CurtainCallDatabase =
        Room.databaseBuilder(
            context,
            CurtainCallDatabase::class.java,
            CURTAINCALL_DB
        ).addMigrations(MIGRATION_4_5).build()

    @Provides
    @Singleton
    fun provideShowSearchDao(
        curtainCallDatabase: CurtainCallDatabase
    ): ShowSearchDao =
        curtainCallDatabase.showSearchDao()

    @Provides
    @Singleton
    fun provideShowRankDao(
        curtainCallDatabase: CurtainCallDatabase
    ): ShowRankDao =
        curtainCallDatabase.showRankDao()

    @Provides
    @Singleton
    fun provideLostPropertySearchDao(
        curtainCallDatabase: CurtainCallDatabase
    ): LostPropertySearchDao =
        curtainCallDatabase.lostItemSearchDao()

    @Provides
    @Singleton
    fun providePartySearchDao(
        curtainCallDatabase: CurtainCallDatabase
    ): PartySearchDao =
        curtainCallDatabase.partySearchDao()
}
