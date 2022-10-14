package ru.ulyanaab.lifemates.domain.repository

import ru.ulyanaab.lifemates.domain.model.TokensModel

interface TokensStorage {
    suspend fun put(model: TokensModel)
    suspend fun get(): TokensModel
}
