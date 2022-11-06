package ru.ulyanaab.lifemates.ui.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.common.interactor.UploadPhotoInteractor
import ru.ulyanaab.lifemates.domain.common.model.GenderModel
import ru.ulyanaab.lifemates.domain.user_info.interactor.UserInfoInteractor
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userInfoInteractor: UserInfoInteractor,
    private val profileMapper: ProfileMapper,
    private val authInteractor: AuthInteractor,
    uploadPhotoInteractor: UploadPhotoInteractor,
) : UploadPhotoViewModel(uploadPhotoInteractor) {

    private val _profileState: MutableStateFlow<ProfileUiModel?> = MutableStateFlow(null)
    val profileState: StateFlow<ProfileUiModel?> = _profileState.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isModelReady = MutableStateFlow(false)
    val isModelReady: StateFlow<Boolean> = _isModelReady.asStateFlow()

    fun attach() {
        CoroutineScope(Dispatchers.IO).launch {
            _isModelReady.value = false
            _isLoading.value = true
            val userInfoModel = userInfoInteractor.getUserInfo()
            val uiModel = userInfoModel?.let {
                profileMapper.mapToUiModel(userInfoModel)
            }
            _profileState.value = uiModel
            _isModelReady.value = true
            _isLoading.value = false
        }
    }

    fun detach() {
        _profileState.value = null
    }

    fun onSaveClick(
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
        val uiModel = ProfileUiModel(
            name = name,
            description = description,
            gender = mapGender(gender),
            birthday = birthday,
            showingGender = mapGender(showingGender),
            telegram = telegram,
            vk = vk,
            viber = viber,
            whatsapp = whatsapp,
            instagram = instagram,
            imageUrl = linkStateFlow.value ?: _profileState.value?.imageUrl
        )
        val updateModel = profileMapper.mapToUpdateModel(uiModel)
        userInfoInteractor.updateUserInfo(updateModel)
    }

    fun onExitClick() {
        CoroutineScope(Dispatchers.IO).launch {
            authInteractor.logOut()
        }
    }

    fun getGenderModels(): List<RoundedBlockUiModel> {
        val gender = _profileState.value?.gender
        return listOf(
            RoundedBlockUiModel("Мужской", gender?.let(::mapGender) == "Мужской"),
            RoundedBlockUiModel("Женский", gender?.let(::mapGender) == "Женский"),
            RoundedBlockUiModel("Не бинарный", gender?.let(::mapGender) == "Не бинарный"),
        )
    }

    fun getShowingGenderModels(): List<RoundedBlockUiModel> {
        val gender = _profileState.value?.showingGender
        return listOf(
            RoundedBlockUiModel("Мужской", gender?.let(::mapGender) == "Мужской"),
            RoundedBlockUiModel("Женский", gender?.let(::mapGender) == "Женский"),
            RoundedBlockUiModel("Не бинарный", gender?.let(::mapGender) == "Не бинарный"),
        )
    }

    // TODO map beautiful
    private fun mapGender(uiModel: RoundedBlockUiModel?): GenderModel {
        return when (uiModel?.text) {
            "Мужской" -> GenderModel.MAN
            "Женский" -> GenderModel.WOMAN
            else -> GenderModel.NON_BINARY
        }
    }

    private fun mapGender(model: GenderModel): String {
        return when (model) {
            GenderModel.MAN -> "Мужской"
            GenderModel.WOMAN -> "Женский"
            GenderModel.NON_BINARY -> "Не бинарный"
        }
    }
}
