package com.cmc.curtaincall.common.designsystem.component.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme

@Composable
fun LiveTalkImageDialog(
    onSelectImage: () -> Unit = {},
    onTakePicture: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF38405C))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.livetalk_popup_title),
                    style = CurtainCallTheme.typography.subTitle4.copy(
                        color = CurtainCallTheme.colors.secondary
                    )
                )
                Row(
                    modifier = Modifier.padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_livetalk_image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(56.dp)
                                .clickable { onSelectImage() },
                            tint = Color.Unspecified
                        )
                        Text(
                            text = stringResource(R.string.livetalk_popup_select_image),
                            modifier = Modifier.padding(top = 10.dp),
                            style = CurtainCallTheme.typography.body3.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = CurtainCallTheme.colors.secondary
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_livetalk_camera),
                            contentDescription = null,
                            modifier = Modifier
                                .size(56.dp)
                                .clickable { onTakePicture() },
                            tint = Color.Unspecified
                        )
                        Text(
                            text = stringResource(R.string.livetalk_popup_take_picture),
                            modifier = Modifier.padding(top = 10.dp),
                            style = CurtainCallTheme.typography.body3.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = CurtainCallTheme.colors.secondary
                            )
                        )
                    }
                }
            }
        }
    }
}
