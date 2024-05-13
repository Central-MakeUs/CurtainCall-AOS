package com.cmc.curtaincall.feature.show.detail.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc.curtaincall.common.designsystem.R
import com.cmc.curtaincall.common.designsystem.component.buttons.common.CurtainCallOutlinedButton
import com.cmc.curtaincall.common.designsystem.component.divider.HorizontalDivider
import com.cmc.curtaincall.common.designsystem.custom.show.ShowInfoContent
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.Grey9
import com.cmc.curtaincall.common.designsystem.theme.White
import com.cmc.curtaincall.common.utility.extensions.convertDefaultDate
import com.cmc.curtaincall.common.utility.extensions.toRunningTime
import com.cmc.curtaincall.domain.enums.getShowTimes
import com.cmc.curtaincall.domain.model.show.FacilityDetailModel
import com.cmc.curtaincall.domain.model.show.ShowDetailModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap

@Composable
fun ShowDetailMenuTabContent(
    modifier: Modifier = Modifier,
    showDetailModel: ShowDetailModel = ShowDetailModel(),
    facilityDetailModel: FacilityDetailModel = FacilityDetailModel()
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    Column(modifier) {
        ShowDetailInfoScreen(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 37.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            showDetailModel = showDetailModel
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            height = 6.dp,
            background = Grey9
        )
        ShowFacilityInfoScreen(
            modifier = Modifier
                .padding(vertical = 37.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            facilityDetailModel = facilityDetailModel
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            height = 6.dp,
            background = Grey9
        )
        if (showDetailModel.introductionImages.isNotEmpty()) {
            Column(Modifier.fillMaxWidth()) {
                val introductionImage = showDetailModel.introductionImages.first()
                    .replace("<styurl>", "")
                    .replace("</styurl>", " ")
                    .split(" ")
                    .first()

                val brush = Brush.verticalGradient(listOf(White.copy(0f), White.copy(1f)))
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .allowHardware(false)
                            .data(introductionImage)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 37.dp)
                            .fillMaxWidth()
                            .then(if (isExpanded) Modifier else Modifier.aspectRatio(360 / 300f)),
                        error = painterResource(R.drawable.ic_error_introduction),
                        alignment = Alignment.TopCenter,
                        contentScale = ContentScale.FillWidth
                    )
                    if (isExpanded.not()) {
                        Canvas(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .aspectRatio(360 / 100f),
                            onDraw = {
                                drawRect(brush)
                            }
                        )
                    }
                }
                CurtainCallOutlinedButton(
                    modifier = Modifier
                        .padding(top = 40.dp, bottom = 30.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    text = stringResource(
                        if (isExpanded) {
                            R.string.close_show_introducation_image
                        } else {
                            R.string.more_view_show_introduction_image
                        }
                    ),
                    onClick = { isExpanded = isExpanded.not() }
                )
            }
        }
    }
}

@Composable
fun ShowDetailInfoScreen(
    modifier: Modifier = Modifier,
    showDetailModel: ShowDetailModel = ShowDetailModel()
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.show_info),
            style = CurtainCallTheme.typography.subTitle4
        )
        ShowInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            title = stringResource(R.string.show_info),
            content = String.format(
                "%s - %s",
                showDetailModel.startDate.convertDefaultDate(),
                showDetailModel.endDate.convertDefaultDate()
            )
        )
        ShowInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            title = stringResource(R.string.show_date),
            content = showDetailModel.showTimes.getShowTimes()
        )
        ShowInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            title = stringResource(R.string.show_runtime),
            content = String.format(
                "%d분",
                showDetailModel.runtime.toRunningTime()
            )
        )
        ShowInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            title = stringResource(R.string.show_age),
            content = showDetailModel.age
        )
        ShowInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            title = stringResource(R.string.show_ticket_price),
            content = showDetailModel.ticketPrice.replace("|", "\n")
        )
        ShowInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            title = stringResource(R.string.show_facility_name),
            content = showDetailModel.facilityName
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ShowFacilityInfoScreen(
    modifier: Modifier = Modifier,
    facilityDetailModel: FacilityDetailModel = FacilityDetailModel()
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.facility_info),
            style = CurtainCallTheme.typography.subTitle4
        )
        ShowInfoContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            title = stringResource(R.string.facility_address),
            content = facilityDetailModel.address
        )
        if (facilityDetailModel.phone.isNotEmpty()) {
            ShowInfoContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                title = stringResource(R.string.facility_phone),
                content = facilityDetailModel.phone
            )
        }
        if (facilityDetailModel.homepage.isNotEmpty()) {
            ShowInfoContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                title = stringResource(R.string.facility_website),
                content = facilityDetailModel.homepage
            )
        }
        NaverMap(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(10.dp)),
            cameraPositionState = CameraPositionState(
                CameraPosition(LatLng(facilityDetailModel.latitude, facilityDetailModel.longitude), 12.0)
            ),
            uiSettings = MapUiSettings(
                isScrollGesturesEnabled = false,
                isTiltGesturesEnabled = false,
                isRotateGesturesEnabled = false,
                isStopGesturesEnabled = false,
                scrollGesturesFriction = 1f,
                isZoomGesturesEnabled = false,
                isZoomControlEnabled = false,
                isLogoClickEnabled = false
            )
        ) {
            Marker(MarkerState(LatLng(facilityDetailModel.latitude, facilityDetailModel.longitude)))
        }
    }
}
