package ru.ulyanaab.lifemates.domain.match.model

import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel

data class MatchesModel(
    val matches: List<MatchModel>,
    val count: Int,
)

data class MatchModel(
    val id: Long,
    val user: OtherUserModel,
    val isSeen: Boolean,
)
