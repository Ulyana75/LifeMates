package ru.ulyanaab.lifemates.ui.register

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.common.interactor.UploadPhotoInteractor
import ru.ulyanaab.lifemates.domain.common.state_holders.RegisterEventsStateHolder
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val registerMapper: RegisterMapper,
    registerEventsStateHolder: RegisterEventsStateHolder,
    uploadPhotoInteractor: UploadPhotoInteractor,
) : UploadPhotoViewModel(uploadPhotoInteractor) {

    val registerEventsFlow = registerEventsStateHolder.registerEventsFlow

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var login = ""
    private var password = ""

    var savedRegisterModel: RegisterUiModel? = null
        private set

    fun detach() {
        savedRegisterModel = null
        login = ""
        password = ""
    }

    fun saveLoginAndPassword(login: String, password: String) {
        this.login = login
        this.password = password
    }

    fun getLoginAndPassword(): Pair<String, String> {
        return Pair(login, password)
    }

    fun onRegisterClick(
        name: String,
        birthday: String,
        gender: RoundedBlockUiModel?,
        showingGender: RoundedBlockUiModel?,
        description: String,
        telegram: String,
        vk: String,
        viber: String,
        whatsapp: String,
        instagram: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true

            val model = RegisterUiModel(
                email = login,
                password = password,
                name = name,
                description = description,
                gender = gender,
                showingGender = showingGender,
                birthday = birthday,
                telegram = telegram,
                vk = vk,
                viber = viber,
                whatsapp = whatsapp,
                instagram = instagram,
                imageUrl = linkStateFlow.value
            )
            savedRegisterModel = model
            authInteractor.register(registerMapper.mapToDomainModel(model))

            _isLoading.value = false
        }
    }

    fun getGenderModels(): List<RoundedBlockUiModel> {
        val gender = savedRegisterModel?.gender
        return listOf(
            RoundedBlockUiModel("Мужской", gender?.text == "Мужской"),
            RoundedBlockUiModel("Женский", gender?.text == "Женский"),
            RoundedBlockUiModel("Не бинарный", gender?.text == "Не бинарный"),
        )
    }

    fun getShowingGenderModels(): List<RoundedBlockUiModel> {
        val gender = savedRegisterModel?.showingGender
        return listOf(
            RoundedBlockUiModel("Мужской", gender?.text == "Мужской"),
            RoundedBlockUiModel("Женский", gender?.text == "Женский"),
            RoundedBlockUiModel("Не бинарный", gender?.text == "Не бинарный"),
        )
    }
}
