package ru.ulyanaab.lifemates

import android.app.Application
import ru.ulyanaab.lifemates.di.AppComponent
import ru.ulyanaab.lifemates.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent =
        DaggerAppComponent
            .factory()
            .create("", applicationContext)
}
