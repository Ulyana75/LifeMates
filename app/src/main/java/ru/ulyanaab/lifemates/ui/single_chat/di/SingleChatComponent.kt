package ru.ulyanaab.lifemates.ui.single_chat.di

import dagger.BindsInstance
import dagger.Component
import ru.ulyanaab.lifemates.domain.chats.interactor.CHAT_ID
import ru.ulyanaab.lifemates.ui.single_chat.SingleChatViewModel
import javax.inject.Named

const val WITH_USER_ID = "user_id"

@Component(dependencies = [SingleChatDependencies::class])
interface SingleChatComponent {

    val singleChatViewModel: SingleChatViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Named(CHAT_ID) chatId: Long,
            @BindsInstance @Named(WITH_USER_ID) userId: Long,
            dependencies: SingleChatDependencies
        ): SingleChatComponent
    }
}
