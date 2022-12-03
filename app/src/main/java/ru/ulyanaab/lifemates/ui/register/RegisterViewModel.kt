package ru.ulyanaab.lifemates.ui.register

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.common.interactor.InterestsInteractor
import ru.ulyanaab.lifemates.domain.common.interactor.UploadPhotoInteractor
import ru.ulyanaab.lifemates.domain.common.model.InterestModel
import ru.ulyanaab.lifemates.domain.common.state_holders.RegisterEventsStateHolder
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.MAN
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.NON_BINARY
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.WOMAN
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val registerMapper: RegisterMapper,
    private val interestsInteractor: InterestsInteractor,
    registerEventsStateHolder: RegisterEventsStateHolder,
    uploadPhotoInteractor: UploadPhotoInteractor,
) : UploadPhotoViewModel(uploadPhotoInteractor) {

    val registerEventsFlow = registerEventsStateHolder.registerEventsFlow

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _interestsFlow: MutableStateFlow<List<InterestModel>> = MutableStateFlow(emptyList())
    val interestsFlow: StateFlow<List<InterestModel>> = _interestsFlow.asStateFlow()

    private var login = ""
    private var password = ""

    var savedRegisterModel: RegisterUiModel? = null
        private set

    fun attach() {
        getInterests()
    }

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
        instagram: String,
        interests: List<RoundedBlockUiModel>?,
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
                imageUrl = linkStateFlow.value,
                interests = interests,
            )
            savedRegisterModel = model
            authInteractor.register(registerMapper.mapToDomainModel(model))

            _isLoading.value = false
        }
    }

    fun getGenderModels(): List<RoundedBlockUiModel> {
        val gender = savedRegisterModel?.gender
        return listOf(
            RoundedBlockUiModel(MAN, gender?.text == MAN),
            RoundedBlockUiModel(WOMAN, gender?.text == WOMAN),
            RoundedBlockUiModel(NON_BINARY, gender?.text == NON_BINARY),
        )
    }

    fun getShowingGenderModels(): List<RoundedBlockUiModel> {
        val gender = savedRegisterModel?.showingGender
        return listOf(
            RoundedBlockUiModel(MAN, gender?.text == MAN),
            RoundedBlockUiModel(WOMAN, gender?.text == WOMAN),
            RoundedBlockUiModel(NON_BINARY, gender?.text == NON_BINARY),
        )
    }

    fun getInterestsModels(): List<RoundedBlockUiModel> {
        val interests = savedRegisterModel?.interests
        return _interestsFlow.value.map { model ->
            RoundedBlockUiModel(
                text = model.value,
                isChosen = interests?.map { it.id }?.contains(model.id) ?: false,
                id = model.id,
            )
        }
    }

    fun isPhotoUploaded(): Boolean {
        return linkStateFlow.value != null
    }

    private fun getInterests() {
        CoroutineScope(Dispatchers.IO).launch {
            val interests = interestsInteractor.getInterests()
            _interestsFlow.value = interests
        }
    }
}
