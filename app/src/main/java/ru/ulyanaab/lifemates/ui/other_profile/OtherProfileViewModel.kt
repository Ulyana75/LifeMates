package ru.ulyanaab.lifemates.ui.other_profile

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.report.interactor.ReportsInteractor
import ru.ulyanaab.lifemates.domain.report.model.ReportType
import ru.ulyanaab.lifemates.domain.users.interactor.UsersInteractor
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import ru.ulyanaab.lifemates.ui.feed.OtherUserMapper

class OtherProfileViewModel @AssistedInject constructor(
    @Assisted private val userId: Long,
    private val usersInteractor: UsersInteractor,
    private val otherUserMapper: OtherUserMapper,
    private val reportsInteractor: ReportsInteractor,
) {
    private val _userStateFlow: MutableStateFlow<OtherUserUiModel?> = MutableStateFlow(null)
    val userStateFlow: StateFlow<OtherUserUiModel?> = _userStateFlow.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun attach() {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            val user = usersInteractor.getSingleUser(userId) ?: return@launch
            _userStateFlow.value = otherUserMapper.mapToUiModel(user)
            _isLoading.value = false
        }
    }

    fun onReportClick(reportType: ReportType) {
        reportsInteractor.report(userId, reportType)
    }
}
