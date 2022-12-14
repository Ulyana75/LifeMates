package ru.ulyanaab.lifemates.ui.common.navigation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.ulyanaab.lifemates.ui.chats.ChatsViewModel
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem.Companion.CHAT_ID_ARGUMENT
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem.Companion.USER_ID_ARGUMENT
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem.Companion.USER_IMAGE_URL_ARGUMENT
import ru.ulyanaab.lifemates.ui.common.navigation.main.MainNavItem.Companion.USER_NAME_ARGUMENT
import ru.ulyanaab.lifemates.ui.feed.FeedViewModel
import ru.ulyanaab.lifemates.ui.interests.InterestsScreen
import ru.ulyanaab.lifemates.ui.interests.InterestsViewModel
import ru.ulyanaab.lifemates.ui.main.MainScreen
import ru.ulyanaab.lifemates.ui.match.MatchScreen
import ru.ulyanaab.lifemates.ui.match.MatchViewModel
import ru.ulyanaab.lifemates.ui.other_profile.OtherProfileScreen
import ru.ulyanaab.lifemates.ui.other_profile.OtherProfileViewModelFactory
import ru.ulyanaab.lifemates.ui.profile.ProfileViewModel
import ru.ulyanaab.lifemates.ui.single_chat.SingleChatScreen
import ru.ulyanaab.lifemates.ui.single_chat.SingleChatScreenConfig
import ru.ulyanaab.lifemates.ui.single_chat.di.DaggerSingleChatComponent
import ru.ulyanaab.lifemates.ui.single_chat.di.SingleChatDependencies

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@Composable
fun MainNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    feedViewModel: FeedViewModel,
    matchViewModel: MatchViewModel,
    chatsViewModel: ChatsViewModel,
    interestsViewModel: InterestsViewModel,
    singleChatDependencies: SingleChatDependencies,
    otherProfileViewModelFactory: OtherProfileViewModelFactory,
) {
    NavHost(
        navController = navController,
        startDestination = MainNavItem.MainScreen.screenRoute
    ) {
        composable(MainNavItem.MainScreen.screenRoute) {
            val bottomNavController = rememberNavController()

            MainScreen(
                bottomNavController = bottomNavController,
                chatsNavController = navController,
                profileViewModel = profileViewModel,
                feedViewModel = feedViewModel,
                chatsViewModel = chatsViewModel,
            )
        }

        composable(MainNavItem.Matches.screenRoute) {
            MatchScreen(
                matchViewModel = matchViewModel,
                navController = navController
            )
        }

        composable(MainNavItem.Interests.screenRoute) {
            InterestsScreen(
                interestsViewModel = interestsViewModel,
                navController = navController,
            )
        }

        composable(
            route = MainNavItem.SingleChat.screenRoute +
                    "/{$CHAT_ID_ARGUMENT}" +
                    "/{${USER_ID_ARGUMENT}}" +
                    "/{${USER_NAME_ARGUMENT}}" +
                    "?imageUrl={${USER_IMAGE_URL_ARGUMENT}}",
            arguments = listOf(
                navArgument(CHAT_ID_ARGUMENT) { type = NavType.LongType },
                navArgument(USER_ID_ARGUMENT) { type = NavType.LongType },
                navArgument(USER_IMAGE_URL_ARGUMENT) { type = NavType.StringType },
                navArgument(USER_NAME_ARGUMENT) { type = NavType.StringType },
            )
        ) {
            val viewModel = remember {
                DaggerSingleChatComponent.factory()
                    .create(
                        chatId = it.arguments?.getLong(CHAT_ID_ARGUMENT) ?: 0,
                        userId = it.arguments?.getLong(USER_ID_ARGUMENT) ?: 0,
                        dependencies = singleChatDependencies,
                    ).singleChatViewModel
            }

            SingleChatScreen(
                singleChatViewModel = viewModel,
                navController = navController,
                config = SingleChatScreenConfig(
                    userId = it.arguments?.getLong(USER_ID_ARGUMENT) ?: 0,
                    userName = it.arguments?.getString(USER_NAME_ARGUMENT) ?: "",
                    userImageUrl = it.arguments?.getString(USER_IMAGE_URL_ARGUMENT) ?: ""
                )
            )
        }

        composable(
            route = MainNavItem.OtherProfile.screenRoute +
                    "/{$USER_ID_ARGUMENT}",
            arguments = listOf(navArgument(USER_ID_ARGUMENT) { type = NavType.LongType })
        ) {
            val viewModel = remember {
                otherProfileViewModelFactory.create(
                    userId = it.arguments?.getLong(USER_ID_ARGUMENT) ?: 0,
                )
            }

            OtherProfileScreen(
                otherProfileViewModel = viewModel,
                navController = navController
            )
        }
    }
}
