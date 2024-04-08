package com.cmc.curtaincall.feature.partymember.create.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallBorderText
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallMultiLineTextField
import com.cmc.curtaincall.common.designsystem.component.basic.CurtainCallSingleLineTextField
import com.cmc.curtaincall.common.designsystem.extensions.toSp
import com.cmc.curtaincall.common.designsystem.theme.Black_Pearl
import com.cmc.curtaincall.common.designsystem.theme.Cultured
import com.cmc.curtaincall.common.designsystem.theme.Me_Pink
import com.cmc.curtaincall.common.designsystem.theme.Nero
import com.cmc.curtaincall.common.designsystem.theme.Roman_Silver
import com.cmc.curtaincall.common.designsystem.theme.Silver_Sand
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.designsystem.theme.spoqahansanseeo

fun LazyGridScope.showLastStep(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onChangeTitle: (String) -> Unit,
    onChangeContent: (String) -> Unit
) {
    item(span = { GridItemSpan(3) }) {
        Column(modifier) {
            Column(Modifier.padding(top = 15.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.partymember_create_write_title),
                        modifier = Modifier.weight(1f),
                        color = Black_Pearl,
                        fontSize = 18.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = spoqahansanseeo
                    )
                    CurtainCallBorderText(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        text = stringResource(R.string.partymember_create_essential),
                        borderColor = Me_Pink,
                        contentColor = Me_Pink,
                        fontSize = 13.dp.toSp(),
                        radiusSize = 20.dp
                    )
                }
                CurtainCallSingleLineTextField(
                    value = title,
                    onValueChange = { if (it.length <= 20) onChangeTitle(it) },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .height(42.dp),
                    fontSize = 15.dp.toSp(),
                    shape = RoundedCornerShape(10.dp),
                    containerColor = Cultured,
                    contentColor = Nero,
                    contentModifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
                    placeholder = stringResource(R.string.partymember_create_write_title_placeholder),
                    placeholderColor = Silver_Sand
                )
                Text(
                    text = String.format(
                        stringResource(R.string.partymember_create_write_number_of_text),
                        title.length,
                        20
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.End),
                    color = Roman_Silver,
                    fontSize = 12.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }

            Column(Modifier.padding(top = 38.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.partymember_create_write_content),
                        modifier = Modifier.weight(1f),
                        color = Black_Pearl,
                        fontSize = 18.dp.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = spoqahansanseeo
                    )
                    CurtainCallBorderText(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        text = stringResource(R.string.partymember_create_essential),
                        borderColor = Me_Pink,
                        contentColor = Me_Pink,
                        fontSize = 13.dp.toSp(),
                        radiusSize = 20.dp
                    )
                }
                CurtainCallMultiLineTextField(
                    value = content,
                    onValueChange = {
                        if (it.length <= 500) onChangeContent(it)
                    },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .heightIn(min = 227.dp),
                    fontSize = 15.dp.toSp(),
                    shape = RoundedCornerShape(10.dp),
                    containerColor = Cultured,
                    contentColor = Nero,
                    contentModifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    placeholder = stringResource(R.string.partymember_create_write_content_placeholder),
                    placeholderColor = Silver_Sand
                )
                Text(
                    text = String.format(
                        stringResource(R.string.partymember_create_write_number_of_text),
                        content.length,
                        500
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.End),
                    color = Roman_Silver,
                    fontSize = 12.dp.toSp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }
        }
    }
}

@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    text: String,
    onChangeText: (String) -> Unit = {}
) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.partymember_create_write_content),
                color = Black_Pearl,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = spoqahansanseeo
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(40.dp, 22.dp)
                    .border(BorderStroke(1.dp, Me_Pink), RoundedCornerShape(20.dp))
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.partymember_create_essential),
                    color = Me_Pink,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = spoqahansanseeo
                )
            }
        }

        BasicTextField(
            value = text,
            onValueChange = onChangeText,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(227.dp)
                .background(Cultured, RoundedCornerShape(10.dp)),
            textStyle = TextStyle(
                color = Nero,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = spoqahansanseeo
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 12.dp)
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.partymember_create_write_content_placeholder),
                            modifier = Modifier
                                .width(284.dp)
                                .wrapContentHeight(),
                            color = Roman_Silver,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = spoqahansanseeo
                        )
                    }
                    innerTextField()
                }
            }
        )

        Text(
            text = String.format(
                stringResource(R.string.partymember_create_write_number_of_text),
                text.length,
                400
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = Roman_Silver,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = spoqahansanseeo,
            textAlign = TextAlign.End
        )
    }
}
