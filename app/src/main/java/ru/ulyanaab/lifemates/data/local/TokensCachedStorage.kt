package ru.ulyanaab.lifemates.data.local

import ru.ulyanaab.lifemates.domain.common.model.TokensModel
import ru.ulyanaab.lifemates.domain.common.repository.TokensStorage
import javax.inject.Inject

class TokensCachedStorage @Inject constructor(
    private val tokenDataStore: TokenDataStore,
) : TokensStorage {

    private var cachedTokensModel: TokensModel? = null

    override suspend fun put(model: TokensModel) {
        cachedTokensModel = model
        tokenDataStore.put(model)
    }

    override suspend fun get(): TokensModel {
        return cachedTokensModel ?: tokenDataStore.get()
    }
}
