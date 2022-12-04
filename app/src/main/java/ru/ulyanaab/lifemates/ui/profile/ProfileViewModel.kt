package ru.ulyanaab.lifemates.ui.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.auth.interactor.AuthInteractor
import ru.ulyanaab.lifemates.domain.common.interactor.UploadPhotoInteractor
import ru.ulyanaab.lifemates.domain.user_info.interactor.UserInfoInteractor
import ru.ulyanaab.lifemates.ui.common.UploadPhotoViewModel
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.MAN
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.NON_BINARY
import ru.ulyanaab.lifemates.ui.common.mapper.GenderMapper.Companion.WOMAN
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import ru.ulyanaab.lifemates.ui.interests.InterestsRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userInfoInteractor: UserInfoInteractor,
    private val profileMapper: ProfileMapper,
    private val authInteractor: AuthInteractor,
    private val genderMapper: GenderMapper,
    private val interestsRepository: InterestsRepository,
    uploadPhotoInteractor: UploadPhotoInteractor,
) : UploadPhotoViewModel(uploadPhotoInteractor) {

    private val _profileState: MutableStateFlow<ProfileUiModel?> = MutableStateFlow(null)
    val profileState: StateFlow<ProfileUiModel?> = _profileState.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isModelReady = MutableStateFlow(false)
    val isModelReady: StateFlow<Boolean> = _isModelReady.asStateFlow()

    fun attach() {
        if (_profileState.value == null) {
            CoroutineScope(Dispatchers.IO).launch {
                _isModelReady.value = false
                _isLoading.value = true
                val userInfoModel = userInfoInteractor.getUserInfo()
                interestsRepository.chosenInterests = userInfoModel?.interests ?: emptyList()
                val uiModel = userInfoModel?.let {
                    profileMapper.mapToUiModel(userInfoModel)
                }
                _profileState.value = uiModel
                _isModelReady.value = true
                _isLoading.value = false
            }
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
            interests = interestsRepository.chosenInterests.map { it.id },
        )
        val updateModel = profileMapper.mapToUpdateModel(uiModel)
        userInfoInteractor.updateUserInfo(updateModel)
        _profileState.value = uiModel
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

    fun getChosenInterests(): List<RoundedBlockUiModel> {
        return interestsRepository.chosenInterests.map {
            RoundedBlockUiModel(
                text = it.value,
                isChosen = true,
                id = it.id,
            )
        }
    }
}
