package ru.ulyanaab.lifemates.domain.users.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.users.model.FeedModel
import ru.ulyanaab.lifemates.domain.users.model.LikeModel
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel

interface UsersRepository {
    suspend fun getUsers(count: Int, offset: Int): Result<FeedModel?>
    suspend fun getSingleUser(id: Long): Result<OtherUserModel?>
    suspend fun likeUser(id: Long): Result<LikeModel?>
    suspend fun dislikeUser(id: Long): Result<Unit>
}
