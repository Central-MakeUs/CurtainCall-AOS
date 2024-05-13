package com.cmc.curtaincall.feature.mypage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.SystemUiStatusBar
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey4
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Grey7
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMyParty: () -> Unit = {},
    onNavigateToWriting: () -> Unit = {},
    onNavigateToFavorite: () -> Unit = {},
    onNavigateToNotice: () -> Unit = {},
    onNavigateToFAQ: () -> Unit = {},
    onNavigateToSetting: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val memberInfoModel by myPageViewModel.memberInfoModel.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        myPageViewModel.requestMemberInfo()
    }

    SystemUiStatusBar(White)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurtainCallTheme.colors.background)
            .verticalScroll(scrollState)
    ) {
        MyPageProfile(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .fillMaxWidth(),
            imageUrl = memberInfoModel.imageUrl,
            nickname = memberInfoModel.nickname,
            onNavigateToProfile = onNavigateToProfile
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .background(Grey9)
        )
        MyPageActivity(
            modifier = Modifier.fillMaxWidth(),
            onNavigateToMyParty = onNavigateToMyParty,
            onNavigateToWriting = onNavigateToWriting,
            onNavigateToFavorite = onNavigateToFavorite
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .background(Grey9)
        )
        MyPageService(
            modifier = Modifier.fillMaxWidth(),
            onNavigateToNotice = onNavigateToNotice,
            onNavigateToFAQ = onNavigateToFAQ,
            onNavigateToSetting = onNavigateToSetting
        )
        MyPageInformation(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MyPageInformation(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .background(Grey9)
            .padding(top = 30.dp, bottom = 40.dp)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.mypage_profile_curtaincall_customer_service_center),
            style = CurtainCallTheme.typography.body4.copy(
                fontWeight = FontWeight.SemiBold,
                color = Grey4
            )
        )
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(36.dp)
                .border(1.dp, Grey7, RoundedCornerShape(5.dp))
                .clickable {
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.setData(Uri.parse("mailto:"))

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.mypage_prifile_curtaincall_email)))
                        putExtra(Intent.EXTRA_SUBJECT, "문의하기")
                        putExtra(Intent.EXTRA_TEXT, "")
                        selector = emailIntent
                    }

                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(Intent.createChooser(intent, "Send Email"))
                    }
                }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_mail),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.mypage_prifile_curtaincall_email),
                modifier = Modifier.padding(start = 6.dp),
                style = CurtainCallTheme.typography.body3.copy(
                    color = Grey5
                )
            )
        }
        Text(
            text = stringResource(R.string.mypage_profile_curtaincall_show_copyright),
            modifier = Modifier.padding(top = 30.dp),
            style = CurtainCallTheme.typography.body4.copy(
                fontWeight = FontWeight.SemiBold,
                color = Grey4
            )
        )
        Text(
            text = stringResource(R.string.mypage_profile_curtaincall_show_copyright_name),
            modifier = Modifier.padding(top = 8.dp),
            style = CurtainCallTheme.typography.body5.copy(
                color = Grey5
            )
        )
        Text(
            text = stringResource(R.string.mypage_profile_curtaincall_show_copyright_date),
            modifier = Modifier.padding(top = 2.dp),
            style = CurtainCallTheme.typography.body5.copy(
                color = Grey5
            )
        )
    }
}

@Composable
private fun MyPageService(
    modifier: Modifier = Modifier,
    onNavigateToNotice: () -> Unit = {},
    onNavigateToFAQ: () -> Unit = {},
    onNavigateToSetting: () -> Unit = {}
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.mypage_profile_service),
            modifier = Modifier.padding(start = 20.dp, top = 30.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        Row(
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth()
                .height(51.dp)
                .clickable { onNavigateToSetting() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mypage_profile_setting),
                style = CurtainCallTheme.typography.body2
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right_pink),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Black
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .clickable { onNavigateToNotice() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mypage_profile_notice),
                style = CurtainCallTheme.typography.body2
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right_pink),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Black
            )
        }
        Row(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxWidth()
                .height(51.dp)
                .clickable { onNavigateToFAQ() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mypage_the_most_frequently_question),
                style = CurtainCallTheme.typography.body2
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right_pink),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Black
            )
        }
    }
}

@Composable
private fun MyPageActivity(
    modifier: Modifier = Modifier,
    onNavigateToMyParty: () -> Unit = {},
    onNavigateToWriting: () -> Unit = {},
    onNavigateToFavorite: () -> Unit = {}
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.mypage_profile_activity),
            modifier = Modifier.padding(top = 30.dp, start = 20.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        Row(
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth()
                .height(51.dp)
                .clickable { onNavigateToMyParty() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_party_activity),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.mypage_profile_party_activity),
                modifier = Modifier.padding(start = 12.dp),
                style = CurtainCallTheme.typography.body2
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .clickable { onNavigateToWriting() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_write_activity),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.mypage_profile_write_activity),
                modifier = Modifier.padding(start = 12.dp),
                style = CurtainCallTheme.typography.body2
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
                .height(51.dp)
                .clickable { onNavigateToFavorite() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_like_activity),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.mypage_profile_like_activity),
                modifier = Modifier.padding(start = 12.dp),
                style = CurtainCallTheme.typography.body2
            )
        }
    }
}

@Composable
private fun MyPageProfile(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    nickname: String,
    onNavigateToProfile: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            error = painterResource(R.drawable.ic_default_profile),
            placeholder = painterResource(R.drawable.ic_default_profile),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = String.format("%s님", nickname),
            modifier = Modifier.padding(start = 14.dp),
            style = CurtainCallTheme.typography.subTitle4
        )
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(1.dp, Grey5, RoundedCornerShape(30.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .clickable { onNavigateToProfile() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.mypage_profile_edit),
                style = CurtainCallTheme.typography.body5.copy(
                    color = Grey5
                )
            )
        }
    }
}
