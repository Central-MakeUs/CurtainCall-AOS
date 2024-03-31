package com.cmc.curtaincall.common.designsystem.component.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallFilledButton
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Chinese_Black
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey5
import com.cmc.curtaincall.common.designsystem.theme.Me_Pink
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo
import com.cmc.curtaincall.domain.enums.ProfileEditImageMode
import com.cmc.curtaincall.domain.enums.ShowSortType
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog

@Composable
fun SelectSortTypeBottomSheet(
    sortType: ShowSortType,
    onSelectSortType: (ShowSortType) -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    BottomSheetDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp)
                .background(White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(vertical = 15.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelectSortType(ShowSortType.REVIEW_GRADE) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ShowSortType.REVIEW_GRADE.value,
                    modifier = Modifier.weight(1f),
                    color = if (sortType == ShowSortType.REVIEW_GRADE) Me_Pink else Nero,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
                if (sortType == ShowSortType.REVIEW_GRADE) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Me_Pink
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelectSortType(ShowSortType.END_DATE) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ShowSortType.END_DATE.value,
                    modifier = Modifier.weight(1f),
                    color = if (sortType == ShowSortType.END_DATE) Me_Pink else Nero,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
                if (sortType == ShowSortType.END_DATE) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Me_Pink
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelectSortType(ShowSortType.NAME) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ShowSortType.NAME.value,
                    modifier = Modifier.weight(1f),
                    color = if (sortType == ShowSortType.NAME) Me_Pink else Nero,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
                if (sortType == ShowSortType.NAME) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Me_Pink
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileImageEditBottomSheet(
    onSelectEditMode: (ProfileEditImageMode) -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    var mode by remember { mutableStateOf(ProfileEditImageMode.NONE) }
    BottomSheetDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
                .background(White, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 30.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { mode = ProfileEditImageMode.ALBUM },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ProfileEditImageMode.ALBUM.value,
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (mode == ProfileEditImageMode.ALBUM) {
                            CurtainCallTheme.colors.primary
                        } else {
                            Grey5
                        }
                    )
                )
                Spacer(Modifier.weight(1f))
                if (mode == ProfileEditImageMode.ALBUM) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = CurtainCallTheme.colors.secondary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { mode = ProfileEditImageMode.DEFAULT },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ProfileEditImageMode.DEFAULT.value,
                    style = CurtainCallTheme.typography.body2.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (mode == ProfileEditImageMode.DEFAULT) {
                            CurtainCallTheme.colors.primary
                        } else {
                            Grey5
                        }
                    )
                )
                Spacer(Modifier.weight(1f))
                if (mode == ProfileEditImageMode.DEFAULT) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = CurtainCallTheme.colors.secondary
                    )
                }
            }
            CurtainCallFilledButton(
                text = stringResource(R.string.complete),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(46.dp),
                onClick = { onSelectEditMode(mode) }
            )
        }
    }
}

@Composable
fun EditBottomSheet(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    BottomSheetDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(187.dp)
                .background(White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp, bottom = 35.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(Cultured, RoundedCornerShape(12.dp))
                    .clickable { onEdit() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_correct),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(R.string.mypage_writing_edit),
                    modifier = Modifier.padding(start = 8.dp),
                    color = Chinese_Black,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(Cultured, RoundedCornerShape(12.dp))
                    .clickable { onDelete() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(R.string.mypage_writing_remove),
                    modifier = Modifier.padding(start = 8.dp),
                    color = Chinese_Black,
                    fontSize = 16.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectSortTypeBottomSheetPreview() {
    SelectSortTypeBottomSheet(
        sortType = ShowSortType.REVIEW_GRADE,
        onSelectSortType = {}
    )
}
