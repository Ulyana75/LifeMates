package ru.ulyanaab.lifemates.ui.single_chat.di

import ru.ulyanaab.lifemates.data.api.ChatsApi
import ru.ulyanaab.lifemates.domain.chats.repository.ChatsRepository
import ru.ulyanaab.lifemates.domain.common.interactor.TokenInteractor
import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import ru.ulyanaab.lifemates.domain.report.interactor.ReportsInteractor
import javax.inject.Inject

class SingleChatDependencies @Inject constructor(
    val chatsApi: ChatsApi,
    val chatsRepository: ChatsRepository,
    val tokenInteractor: TokenInteractor,
    val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
    val reportsInteractor: ReportsInteractor,
)
