package ru.ulyanaab.lifemates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.location.LocationServices
import ru.ulyanaab.lifemates.domain.common.state_holders.AuthStateHolder
import ru.ulyanaab.lifemates.ui.auth.AuthViewModel
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.general.GeneralNavGraph
import ru.ulyanaab.lifemates.ui.common.theme.LifeMatesTheme
import ru.ulyanaab.lifemates.ui.common.utils.OnExitClearHelper
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.interests.InterestsRepository
import ru.ulyanaab.lifemates.ui.interests.InterestsViewModel
import ru.ulyanaab.lifemates.ui.loading.LoadingViewModel
import ru.ulyanaab.lifemates.ui.match.MatchViewModel
import ru.ulyanaab.lifemates.ui.other_profile.OtherProfileViewModelFactory
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel
import ru.ulyanaab.lifemates.ui.register.RegisterViewModel
import ru.ulyanaab.lifemates.ui.single_chat.di.SingleChatDependencies
import javax.inject.Inject

@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authStateHolder: AuthStateHolder

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var registerViewModel: RegisterViewModel

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var feedViewModel: FeedViewModel

    @Inject
    lateinit var loadingViewModel: LoadingViewModel

    @Inject
    lateinit var matchViewModel: MatchViewModel

    @Inject
    lateinit var chatsViewModel: ChatsViewModel

    @Inject
    lateinit var interestsViewModel: InterestsViewModel

    @Inject
    lateinit var singleChatDependencies: SingleChatDependencies

    @Inject
    lateinit var otherProfileViewModelFactory: OtherProfileViewModelFactory

    @Inject
    lateinit var interestsRepository: InterestsRepository

    lateinit var onExitClearHelper: OnExitClearHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        // TODO refactor
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        feedViewModel.setFusedLocationClient(fusedLocationClient)

        onExitClearHelper = OnExitClearHelper(
            profileViewModel,
            feedViewModel,
            matchViewModel,
            chatsViewModel,
            authStateHolder,
            registerViewModel,
            interestsRepository
        )
        onExitClearHelper.observeAuthState()

        setContent {
            LifeMatesTheme {
                val navController = rememberNavController()

                GeneralNavGraph(
                    navController = navController,
                    feedViewModel = feedViewModel,
                    profileViewModel = profileViewModel,
                    authViewModel = authViewModel,
                    registerViewModel = registerViewModel,
                    loadingViewModel = loadingViewModel,
                    authStateHolder = authStateHolder,
                    matchViewModel = matchViewModel,
                    chatsViewModel = chatsViewModel,
                    interestsViewModel = interestsViewModel,
                    singleChatDependencies = singleChatDependencies,
                    otherProfileViewModelFactory = otherProfileViewModelFactory,
                )
            }
        }
    }
}
