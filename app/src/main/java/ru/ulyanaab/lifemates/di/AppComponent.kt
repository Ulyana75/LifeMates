package ru.ulyanaab.lifemates.di

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.BindsInstance
import dagger.Component
import ru.ulyanaab.lifemates.MainActivity
import javax.inject.Named

const val BASE_URL = "baseUrl"

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    @ExperimentalPagerApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Named(BASE_URL) baseUrl: String,
            @BindsInstance context: Context,
        ): AppComponent
    }
}
