package ru.ulyanaab.lifemates.ui.presenter

import ru.ulyanaab.lifemates.domain.interactors.AuthInteractor
import ru.ulyanaab.lifemates.domain.model.LoginModel
import javax.inject.Inject

class AuthPresenter @Inject constructor(
    private val authInteractor: AuthInteractor
) {
    fun login(login: String, password: String) {
        authInteractor.login(LoginModel(login, password))
    }
}
