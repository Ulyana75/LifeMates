package ru.ulyanaab.lifemates.domain.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.common.interactor.TokenInteractor
import javax.inject.Inject

class ResultProcessorWithTokensRefreshing @Inject constructor(
    private val tokenInteractor: TokenInteractor,
) {

    suspend fun <T> proceedAndReturn(
        resultProducer: suspend () -> Result<T>,
        onTokensRefreshedSuccessfully: suspend () -> T?
    ): T? {
        return when (val result = resultProducer.invoke()) {
            is Result.Success -> result.data
            is Result.Failure -> when (result.error) {
                is Error.Unauthorized -> if (tokenInteractor.getAndSaveNewTokens()) {
                    onTokensRefreshedSuccessfully.invoke()
                } else null
                else -> null
            }
        }
    }

    fun proceed(
        resultProducer: suspend () -> Result<Unit>,
        onTokensRefreshedSuccessfully: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = resultProducer.invoke()
            if (result is Result.Failure && result.error is Error.Unauthorized) {
                tokenInteractor.getAndSaveNewTokens {
                    onTokensRefreshedSuccessfully.invoke()
                }
            }
        }
    }
}
