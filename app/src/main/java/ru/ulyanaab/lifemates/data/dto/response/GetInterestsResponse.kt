package ru.ulyanaab.lifemates.data.dto.response

import ru.ulyanaab.lifemates.data.dto.common.InterestDto

data class GetInterestsResponse(
    val interests: List<InterestDto>?,
)
