package com.cmc.curtaincall.feature.mypage.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.custom.poster.CurtainCallFavoritePoster
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.domain.enums.ShowGenreType

@Composable
internal fun MyPageFavoriteScreen(
    onNavigateToShowDetail: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    SystemUiStatusBar(White)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(R.string.mypage_favorite_show),
                onBack = onBack
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        MyPageFavoriteContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CurtainCallTheme.colors.background),
            onNavigateToShowDetail = onNavigateToShowDetail
        )
    }
}

@Composable
private fun MyPageFavoriteContent(
    modifier: Modifier = Modifier,
    myPageFavoriteViewModel: MyPageFavoriteViewModel = hiltViewModel(),
    onNavigateToShowDetail: (String) -> Unit = {}
) {
    LaunchedEffect(true) {
        myPageFavoriteViewModel.requestFavoriteShows()
    }

    val genreType by myPageFavoriteViewModel.genreType.collectAsStateWithLifecycle()
    val favoriteShows by myPageFavoriteViewModel.favoriteShows.collectAsStateWithLifecycle()
    Column(modifier) {
        Row(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
            CurtainCallBasicChip(
                text = ShowGenreType.PLAY.value,
                isSelect = genreType == ShowGenreType.PLAY,
                onClick = { myPageFavoriteViewModel.selectGenreType(ShowGenreType.PLAY) }
            )
            CurtainCallBasicChip(
                text = ShowGenreType.MUSICAL.value,
                modifier = Modifier.padding(start = 8.dp),
                isSelect = genreType == ShowGenreType.MUSICAL,
                onClick = { myPageFavoriteViewModel.selectGenreType(ShowGenreType.MUSICAL) }
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(top = 18.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(favoriteShows) {
                CurtainCallFavoritePoster(
                    model = it.poster,
                    title = it.name,
                    isFavorite = it.favorite,
                    onFavoriteClick = {
                        if (it.favorite) {
                            myPageFavoriteViewModel.deleteFavoriteShow(it.id)
                        } else {
                            myPageFavoriteViewModel.requestFavoriteShow(it.id)
                        }
                    },
                    onClick = {
                        onNavigateToShowDetail(it.id)
                    }
                )
            }
        }
    }
}
