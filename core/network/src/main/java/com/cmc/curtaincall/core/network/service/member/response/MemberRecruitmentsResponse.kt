package com.cmc.curtaincall.core.network.service.member.response

import com.cmc.curtaincall.domain.model.member.MyRecruitmentModel
import com.google.gson.annotations.SerializedName

data class MemberRecruitmentsResponse(
    @SerializedName("content") val recruitments: List<MemberRecruitmentResponse>
)

data class MemberRecruitmentResponse(
    val id: Int,
    val title: String,
    val content: String,
    val curMemberNum: Int,
    val maxMemberNum: Int,
    val showAt: String?,
    val createdAt: String,
    val showId: String?,
    val showName: String?,
    val showPoster: String?,
    val facilityId: String?,
    val facilityName: String?,
) {
    fun toModel() = MyRecruitmentModel(
        id = id,
        title = title,
        content = content,
        curMemberNum = curMemberNum,
        maxMemberNum = maxMemberNum,
        showAt = showAt,
        createdAt = createdAt,
        showId = showId,
        showName = showName,
        showPoster = showPoster,
        facilityId = facilityId,
        facilityName = facilityName
    )
}
