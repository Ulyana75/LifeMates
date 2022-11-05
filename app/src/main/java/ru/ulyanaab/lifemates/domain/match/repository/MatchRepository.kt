package ru.ulyanaab.lifemates.domain.match.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel

interface MatchRepository {
    suspend fun getMatches(offset: Int, limit: Int): Result<List<OtherUserModel>?>
}
