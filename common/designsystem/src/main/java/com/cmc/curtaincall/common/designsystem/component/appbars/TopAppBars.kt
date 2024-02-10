package com.cmc.curtaincall.common.designsystem.component.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.theme.Black
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey2
import com.cmc.curtaincall.common.designsystem.theme.Grey6
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.avenirnext

private const val TopAppBarBackIconDescription = "Go Back"
private const val TopAppBarSearchActionDescription = "Search"
private const val TopAppBarClearActionDescription = "Clear"
private val TopAppBarBackIconSize = 24.dp
private val TopAppBarHeight = 56.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurtainCallTitleTopAppBar(
    containerColor: Color = CurtainCallTheme.colors.background
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopAppBarHeight),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.default_top_appbar_title),
                modifier = Modifier.padding(start = 20.dp),
                style = CurtainCallTheme.typography.subTitle3.copy(
                    fontFamily = avenirnext
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurtainCallCenterTopAppBarWithBack(
    title: String,
    containerColor: Color = CurtainCallTheme.colors.background,
    contentColor: Color = Black,
    onBack: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopAppBarHeight),
        color = containerColor
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = TopAppBarBackIconDescription,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 14.dp)
                    .size(TopAppBarBackIconSize)
                    .clickable { onBack() },
                tint = contentColor
            )
            Text(
                text = title,
                modifier = Modifier.width(180.dp),
                style = CurtainCallTheme.typography.subTitle4.copy(
                    color = contentColor
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Stable
data class SearchAppBarState(
    var isSearchMode: MutableState<Boolean> = mutableStateOf(false),
    var searchText: MutableState<String> = mutableStateOf(""),
    var isDoneSearch: MutableState<Boolean> = mutableStateOf(false),
    val onTextChange: (String) -> Unit = {
        searchText.value = it
        isDoneSearch.value = false
    },
    val onSearch: () -> Unit = {
        isSearchMode.value = true
        isDoneSearch.value = false
    },
    val onClear: () -> Unit = {
        searchText.value = ""
        isDoneSearch.value = false
    },
    val onCancel: () -> Unit = {
        searchText.value = ""
        isSearchMode.value = false
        isDoneSearch.value = false
    },
    var onDone: (String) -> Unit = { isDoneSearch.value = true }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurtainCallSearchTitleTopAppBar(
    title: String,
    searchAppBarState: SearchAppBarState = SearchAppBarState {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopAppBarHeight)
            .background(CurtainCallTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (searchAppBarState.isSearchMode.value) {
            Row(
                modifier = Modifier
                    .padding(start = 14.dp, end = 18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .background(Grey9, RoundedCornerShape(30.dp))
                        .padding(start = 14.dp, end = 11.dp)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = searchAppBarState.searchText.value,
                        onValueChange = searchAppBarState.onTextChange,
                        modifier = Modifier
                            .weight(1f)
                            .background(Grey9, RoundedCornerShape(30.dp)),
                        textStyle = CurtainCallTheme.typography.body3.copy(fontWeight = FontWeight.SemiBold),
                        singleLine = true,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(
                            onDone = { searchAppBarState.onDone(searchAppBarState.searchText.value) }
                        )
                    ) { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchAppBarState.searchText.value.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search_appbar_hint),
                                    style = CurtainCallTheme.typography.body3.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Grey6
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                    Icon(
                        painter = painterResource(R.drawable.ic_search_close),
                        contentDescription = TopAppBarClearActionDescription,
                        modifier = Modifier
                            .padding(start = 11.dp)
                            .size(18.dp)
                            .clickable { searchAppBarState.onClear() },
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = stringResource(R.string.cancel),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable { searchAppBarState.onCancel() },
                    style = CurtainCallTheme.typography.body3.copy(
                        color = Grey2,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        } else {
            Text(
                text = title,
                modifier = Modifier.padding(start = 18.dp),
                style = CurtainCallTheme.typography.h2
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = TopAppBarSearchActionDescription,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(TopAppBarBackIconSize)
                    .clickable { searchAppBarState.onSearch() },
                tint = Color.Unspecified
            )
        }
    }
}
