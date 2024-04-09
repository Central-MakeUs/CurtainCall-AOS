package com.cmc.curtaincall.feature.show.detail.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.custom.show.ShowReviewItemContent
import com.cmc.curtaincall.common.designsystem.custom.show.ShowReviewEmptyContent
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.domain.model.review.ShowReviewModel

@Composable
fun ShowReviewTabContent(
    modifier: Modifier = Modifier,
    showReviews: List<ShowReviewModel> = listOf(),
    reviewCount: Int = 0,
    showId: String = "",
    onNavigateToReview: (String) -> Unit = {},
    onNavigateToReviewCreate: () -> Unit = {}
) {
    Column(modifier.heightIn(min = 285.dp)) {
        if (reviewCount == 0) {
            Spacer(Modifier.weight(58f))
            ShowReviewEmptyContent(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.weight(60f))
            CurtainCallFilledButton(
                text = stringResource(R.string.write_review),
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                onClick = { onNavigateToReviewCreate() }
            )
        } else {
            Text(
                text = String.format(stringResource(R.string.show_review_count_format), reviewCount),
                modifier = Modifier
                    .padding(top = 38.dp)
                    .padding(horizontal = 20.dp),
                style = CurtainCallTheme.typography.subTitle4
            )
            showReviews.forEachIndexed { index, showReview ->
                ShowReviewItemContent(
                    modifier = Modifier
                        .padding(top = if (index == 0) 10.dp else 12.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    showReviewModel = showReview
                )
            }
            CurtainCallFilledButton(
                text = stringResource(R.string.review_all_view),
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 30.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                onClick = { onNavigateToReview(showId) }
            )
        }
    }
}
