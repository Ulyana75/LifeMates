package ru.ulyanaab.lifemates.domain.repository

import ru.ulyanaab.lifemates.domain.model.TokensModel

interface TokensRepository {
    suspend fun put(model: TokensModel)
    suspend fun get(): TokensModel
}
