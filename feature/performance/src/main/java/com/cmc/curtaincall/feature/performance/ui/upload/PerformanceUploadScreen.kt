package com.cmc.curtaincall.feature.performance.ui.upload

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc.curtaincall.common.design.R
import com.cmc.curtaincall.common.design.extensions.toSp
import com.cmc.curtaincall.common.design.theme.*

@Composable
internal fun PerformanceUploadScreen(
    onNavigateLostItem: () -> Unit,
    onNavigateHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_complete),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 216.dp)
                .size(70.dp, 70.dp)
        )
        Text(
            text = stringResource(R.string.performance_lost_item_upload_complete),
            modifier = Modifier.padding(top = 20.dp),
            color = Nero,
            fontSize = 20.dp.toSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = spoqahansanseeo
        )
        Text(
            text = stringResource(R.string.performance_lost_item_upload_complete_description),
            modifier = Modifier.padding(top = 22.dp),
            color = Black_Coral,
            fontSize = 15.dp.toSp(),
            fontWeight = FontWeight.Medium,
            fontFamily = spoqahansanseeo,
            textAlign = TextAlign.Center,
            lineHeight = 22.dp.toSp()
        )
        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .border(BorderStroke(1.dp, Roman_Silver), RoundedCornerShape(30.dp))
                .clickable { onNavigateLostItem() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.partymember_upload_checking_article),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 18.dp),
                color = Roman_Silver,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = spoqahansanseeo
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onNavigateHome() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 19.dp)
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Me_Pink)
        ) {
            Text(
                text = stringResource(R.string.partymember_upload_move_to_home),
                color = White,
                fontSize = 16.dp.toSp(),
                fontWeight = FontWeight.Medium,
                fontFamily = spoqahansanseeo
            )
        }
    }
}
