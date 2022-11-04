package ru.ulyanaab.lifemates.domain.common.repository

import ru.ulyanaab.lifemates.domain.common.model.TokensModel

interface TokensStorage {
    suspend fun put(model: TokensModel)
    suspend fun get(): TokensModel
    suspend fun clear()
}
