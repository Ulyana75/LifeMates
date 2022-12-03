package ru.ulyanaab.lifemates.ui.profile

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
import ru.ulyanaab.lifemates.domain.user_info.interactor.UserInfoInteractor
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.MAN
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.NON_BINARY
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.WOMAN
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userInfoInteractor: UserInfoInteractor,
    private val interestsInteractor: InterestsInteractor,
    private val profileMapper: ProfileMapper,
    private val authInteractor: AuthInteractor,
    private val genderMapper: GenderMapper,
    uploadPhotoInteractor: UploadPhotoInteractor,
) : UploadPhotoViewModel(uploadPhotoInteractor) {

    private val _profileState: MutableStateFlow<ProfileUiModel?> = MutableStateFlow(null)
    val profileState: StateFlow<ProfileUiModel?> = _profileState.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _interestsFlow: MutableStateFlow<List<InterestModel>> = MutableStateFlow(emptyList())
    val interestsFlow: StateFlow<List<InterestModel>> = _interestsFlow.asStateFlow()

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
            getInterests()
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
        instagram: String,
        interests: List<RoundedBlockUiModel>?,
    ) {
        val uiModel = ProfileUiModel(
            name = name,
            description = description,
            gender = genderMapper.mapToModel(gender),
            birthday = birthday,
            showingGender = genderMapper.mapToModel(showingGender),
            telegram = telegram,
            vk = vk,
            viber = viber,
            whatsapp = whatsapp,
            instagram = instagram,
            imageUrl = linkStateFlow.value ?: _profileState.value?.imageUrl,
            interests = interests?.mapNotNull { it.id } ?: emptyList(),
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
            RoundedBlockUiModel(MAN, gender?.let(genderMapper::mapToText) == MAN),
            RoundedBlockUiModel(WOMAN, gender?.let(genderMapper::mapToText) == WOMAN),
            RoundedBlockUiModel(NON_BINARY, gender?.let(genderMapper::mapToText) == NON_BINARY),
        )
    }

    fun getShowingGenderModels(): List<RoundedBlockUiModel> {
        val gender = _profileState.value?.showingGender
        return listOf(
            RoundedBlockUiModel(MAN, gender?.let(genderMapper::mapToText) == MAN),
            RoundedBlockUiModel(WOMAN, gender?.let(genderMapper::mapToText) == WOMAN),
            RoundedBlockUiModel(NON_BINARY, gender?.let(genderMapper::mapToText) == NON_BINARY),
        )
    }

    fun getInterestsModels(): List<RoundedBlockUiModel> {
        val interests = _profileState.value?.interests
        return _interestsFlow.value.map { model ->
            RoundedBlockUiModel(
                text = model.value,
                isChosen = interests?.contains(model.id) ?: false,
                id = model.id,
            )
        }
    }

    private suspend fun getInterests() {
        val interests = interestsInteractor.getInterests()
        _interestsFlow.value = interests
    }
}
