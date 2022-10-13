package ru.ulyanaab.lifemates.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

const val BASE_URL = "baseUrl"

@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @Named(BASE_URL) baseUrl: String,
            @BindsInstance context: Context,
        ): AppComponent
    }
}
