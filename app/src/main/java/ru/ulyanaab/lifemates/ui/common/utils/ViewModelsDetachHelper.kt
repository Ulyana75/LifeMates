package ru.ulyanaab.lifemates.ui.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthState
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.match.MatchViewModel
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterViewModel
import javax.inject.Inject

class ViewModelsDetachHelper constructor(
    private val profileViewModel: ProfileViewModel,
    private val feedViewModel: FeedViewModel,
    private val matchViewModel: MatchViewModel,
    private val chatsViewModel: ChatsViewModel,
    private val authStateHolder: AuthStateHolder,
    private val registerViewModel: RegisterViewModel,
) {

    fun observeAuthState() {
        authStateHolder.authStateFlow
            .onEach {
                if (it == AuthState.UNAUTHORIZED) {
                    detachAll()
                }
            }
            .launchIn(CoroutineScope(Dispatchers.Default))
    }

    private fun detachAll() {
        profileViewModel.detach()
        feedViewModel.detach()
        matchViewModel.detach()
        chatsViewModel.detach()
        registerViewModel.detach()
    }
}
