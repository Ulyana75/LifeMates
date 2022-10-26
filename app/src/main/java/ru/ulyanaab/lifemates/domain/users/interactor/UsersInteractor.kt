package ru.ulyanaab.lifemates.domain.users.interactor

import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import ru.ulyanaab.lifemates.domain.users.model.FeedModel
import ru.ulyanaab.lifemates.domain.users.model.OtherUserModel
import ru.ulyanaab.lifemates.domain.users.repository.UsersRepository
import javax.inject.Inject

class UsersInteractor @Inject constructor(
    private val usersRepository: UsersRepository,
    private val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
) {

    suspend fun getFeed(count: Int): FeedModel? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                usersRepository.getUsers(count)
            },
            onTokensRefreshedSuccessfully = {
                getFeed(count)
            }
        )
    }

    suspend fun getSingleUser(id: Long): OtherUserModel? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                usersRepository.getSingleUser(id)
            },
            onTokensRefreshedSuccessfully = {
                getSingleUser(id)
            }
        )
    }

    suspend fun like(id: Long): Boolean? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                usersRepository.likeUser(id)
            },
            onTokensRefreshedSuccessfully = {
                like(id)
            }
        )
    }

    fun dislike(id: Long) {
        resultProcessorWithTokensRefreshing.proceed(
            resultProducer = {
                usersRepository.dislikeUser(id)
            },
            onTokensRefreshedSuccessfully = {
                dislike(id)
            }
        )
    }
}
