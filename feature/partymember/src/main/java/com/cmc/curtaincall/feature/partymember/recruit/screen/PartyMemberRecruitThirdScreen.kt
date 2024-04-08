package com.cmc.curtaincall.feature.partymember.recruit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey1
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.Red
import com.cmc.curtaincall.feature.partymember.recruit.PartyMemberRecruitViewModel

@Composable
fun ColumnScope.PartyMemberRecruitThirdScreen(
    partyMemberRecruitViewModel: PartyMemberRecruitViewModel = hiltViewModel(),
    title: String = "",
    content: String = ""
) {
    Text(
        text = stringResource(R.string.party_member_show_time_guide),
        modifier = Modifier.padding(top = 30.dp, start = 20.dp),
        style = CurtainCallTheme.typography.subTitle4
    )
    BasicTextField(
        value = title,
        onValueChange = { partyMemberRecruitViewModel.changePartyTitle(it) },
        modifier = Modifier
            .padding(top = 12.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(
                color = Grey9,
                shape = RoundedCornerShape(10.dp)
            )
            .height(44.dp),
        textStyle = CurtainCallTheme.typography.body3.copy(
            color = Grey1
        )
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (title.isEmpty()) {
                Text(
                    text = stringResource(R.string.party_member_show_title_placeholder),
                    style = CurtainCallTheme.typography.body3.copy(
                        color = Grey6
                    ),
                    maxLines = 1
                )
            }
            innerTextField()
        }
    }
    Text(
        text = stringResource(R.string.party_member_show_content_guide),
        modifier = Modifier.padding(top = 40.dp, start = 20.dp),
        style = CurtainCallTheme.typography.subTitle4
    )
    BasicTextField(
        value = content,
        onValueChange = { partyMemberRecruitViewModel.changePartyContent(it) },
        modifier = Modifier
            .padding(top = 12.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(
                color = Grey9,
                shape = RoundedCornerShape(10.dp)
            )
            .heightIn(min = 130.dp),
        textStyle = CurtainCallTheme.typography.body3.copy(
            color = Grey1
        )
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            if (content.isEmpty()) {
                Text(
                    text = stringResource(R.string.party_member_show_content_placeholder),
                    style = CurtainCallTheme.typography.body3.copy(
                        color = Grey6
                    )
                )
            }
            innerTextField()
        }
    }
    if (content.length > 500) {
        Text(
            text = stringResource(R.string.party_member_show_content_restrict),
            modifier = Modifier.padding(top = 8.dp, start = 34.dp),
            style = CurtainCallTheme.typography.body4.copy(
                color = Red
            )
        )
    }
}
