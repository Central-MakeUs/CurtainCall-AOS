package com.cmc.curtaincall.feature.show.review.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.appbars.CurtainCallCenterTopAppBarWithBack
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.component.chips.CurtainCallBasicChip
import com.cmc.curtaincall.common.designsystem.custom.CurtainCallRatingBar
import com.cmc.curtaincall.common.designsystem.dimension.Paddings
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey3
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.Red
import com.cmc.curtaincall.common.navigation.destination.DEFAULT_REVIEW_ID
import com.cmc.curtaincall.domain.enums.translateShowGenreType
import com.cmc.curtaincall.feature.show.detail.ShowDetailViewModel
import com.cmc.curtaincall.feature.show.review.ShowReviewSideEffect
import com.cmc.curtaincall.feature.show.review.ShowReviewViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun ShowReviewCreateScreen(
    showDetailViewModel: ShowDetailViewModel = hiltViewModel(),
    showReviewViewModel: ShowReviewViewModel = hiltViewModel(),
    showId: String?,
    reviewId: Int?,
    onBack: () -> Unit
) {
    requireNotNull(showId)
    requireNotNull(reviewId)

    val editMode = reviewId != DEFAULT_REVIEW_ID
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }

    if (editMode) {
        showDetailViewModel.requestShowDetail(showId)
    }

    LaunchedEffect(showReviewViewModel) {
        showReviewViewModel.effects.collectLatest { effect ->
            when (effect) {
                is ShowReviewSideEffect.CreateMyReview -> onBack()
                is ShowReviewSideEffect.RefreshShowReview -> onBack()
                else -> Unit
            }
        }
    }

    SystemUiStatusBar(Grey9)
    Scaffold(
        topBar = {
            CurtainCallCenterTopAppBarWithBack(
                title = stringResource(if (editMode) R.string.show_review_edit_title else R.string.show_review_title),
                containerColor = Grey9,
                contentColor = Black,
                onBack = onBack
            )
        },
        floatingActionButton = {
            CurtainCallFilledButton(
                text = stringResource(R.string.write_completed),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                textStyle = CurtainCallTheme.typography.body2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                onClick = {
                    if (reviewText.isNotEmpty()) {
                        if (editMode) {
                            showReviewViewModel.updateShowReview(reviewId, reviewText, rating)
                        } else {
                            showReviewViewModel.createShowReview(showId, rating, reviewText)
                        }
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        ShowReviewCreateContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Grey9),
            showDetailViewModel = showDetailViewModel,
            rating = rating,
            reviewText = reviewText,
            onRatingChange = { rating = it },
            onReviewTextChange = { reviewText = it }
        )
    }
}

@Composable
private fun ShowReviewCreateContent(
    modifier: Modifier = Modifier,
    showDetailViewModel: ShowDetailViewModel,
    rating: Int,
    reviewText: String,
    onRatingChange: (Int) -> Unit = {},
    onReviewTextChange: (String) -> Unit = {}
) {
    val showDetailUiState by showDetailViewModel.uiState.collectAsStateWithLifecycle()
    val showDetailModel = showDetailUiState.showDetailModel
    val scrollState = rememberScrollState()
    var showRestrict by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(top = 10.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
        ) {
            AsyncImage(
                model = showDetailModel.poster,
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp, 257.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.FillBounds
            )
        }
        CurtainCallBasicChip(
            modifier = Modifier.padding(top = 20.dp),
            text = translateShowGenreType(showDetailModel.genre).value,
            textStyle = CurtainCallTheme.typography.body4,
            isSelect = true
        )
        Text(
            text = showDetailModel.name,
            modifier = Modifier.padding(Paddings.medium),
            style = CurtainCallTheme.typography.subTitle1
        )
        Text(
            text = stringResource(R.string.show_review_create_guide),
            modifier = Modifier.padding(top = 20.dp),
            style = CurtainCallTheme.typography.subTitle4.copy(
                color = CurtainCallTheme.colors.primary
            )
        )
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(CurtainCallTheme.colors.background, RoundedCornerShape(10.dp))
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            CurtainCallRatingBar(
                modifier = Modifier.size(206.dp, 38.dp),
                rating = rating,
                onClick = { onRatingChange(it) }
            )
        }
        Text(
            text = stringResource(R.string.show_review_create_description),
            modifier = Modifier.padding(top = 30.dp),
            style = CurtainCallTheme.typography.subTitle4.copy(
                color = CurtainCallTheme.colors.primary
            )
        )
        BasicTextField(
            value = reviewText,
            onValueChange = { text ->
                if (text.length <= 500) {
                    onReviewTextChange(text)
                    showRestrict = text.length == 500
                }
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(
                    color = CurtainCallTheme.colors.background,
                    shape = RoundedCornerShape(10.dp)
                )
                .heightIn(min = 118.dp),
            textStyle = CurtainCallTheme.typography.body3.copy(
                color = Grey3
            )
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (reviewText.isEmpty()) {
                    Text(
                        text = stringResource(R.string.show_review_create_text_placeholder),
                        style = CurtainCallTheme.typography.body3.copy(
                            color = Grey6
                        )
                    )
                }
                innerTextField()
            }
        }
        if (showRestrict) {
            Text(
                text = stringResource(R.string.show_review_create_max_text_restrict),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 8.dp, start = 38.dp),
                style = CurtainCallTheme.typography.body4.copy(
                    color = Red
                )
            )
        }
        Spacer(Modifier.size(121.dp))
    }
}
