package ru.ulyanaab.lifemates.data.local

import ru.ulyanaab.lifemates.domain.model.TokensModel
import ru.ulyanaab.lifemates.domain.repository.TokensRepository
import javax.inject.Inject

class TokensCachedStorage @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : TokensRepository {

    private var cachedTokensModel: TokensModel? = null

    override suspend fun put(model: TokensModel) {
        cachedTokensModel = model
        tokenDataStore.put(model)
    }

    override suspend fun get(): TokensModel {
        return cachedTokensModel ?: tokenDataStore.get()
    }
}
