package ru.ulyanaab.lifemates

import android.app.Application
import ru.ulyanaab.lifemates.di.AppComponent
import ru.ulyanaab.lifemates.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create("http://test.u1790484.plsk.regruhosting.ru", applicationContext)
    }
}
