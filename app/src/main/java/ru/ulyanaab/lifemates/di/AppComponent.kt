package ru.ulyanaab.lifemates.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ulyanaab.lifemates.MainActivity
import javax.inject.Named

const val BASE_URL = "baseUrl"

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Named(BASE_URL) baseUrl: String,
            @BindsInstance context: Context,
        ): AppComponent
    }
}
